#pragma once

/*Input Command Operation for Arduino*/
const char OP_COMFIRM_CONNECTION = 0x49;//'I'
const char OP_INVALID_OPERATION = 0x15;//ASCII NAK
const char OP_STOP = 0x73;//'s'
const char OP_RESET = 0x52;//'R'
const char OP_CHANGE_CHANNEL = 0x63;//'c'
const char OP_CAPTURE= 0x43;//'C'
const char OP_SUBMIT = 0x53;//'S'
const char OP_ATTACK = 0x41;//'A'
const char OP_ANSWER = 0x61;//'a'


/*Output respons from Arduino*/
const char RES_COMFIRM_CONNECTION = 0x06;//ASCII ACK
const char RES_INVALID_OPERATION = 0x15;
const char RES_SUCCESS = 0x06;//ACK
const char RES_FAIL = 0xFF;
const char RES_TIMEOUT = 0xFE;