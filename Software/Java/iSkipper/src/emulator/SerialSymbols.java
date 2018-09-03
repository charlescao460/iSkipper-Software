package emulator;

/**
 * @author CSR
 *
 *         Same as "SerialSymbols.h" in Arduino file.
 */
final class SerialSymbols
{
	/* Input Command Operation for Arduino */
	public static final byte OP_COMFIRM_CONNECTION = 0x49;// 'I'
	public static final byte OP_INVALID_OPERATION = 0x15;// ASCII NAK
	public static final byte OP_STOP = 0x73;// 's'
	public static final byte OP_RESET = 0x52;// 'R'
	public static final byte OP_CHANGE_CHANNEL = 0x63;// 'c'
	public static final byte OP_CAPTURE = 0x43;// 'C'
	public static final byte OP_SUBMIT = 0x53;// 'S'
	public static final byte OP_ATTACK = 0x41;// 'A'
	public static final byte OP_ANSWER = 0x61;// 'a'
	public static final byte OP_REPLACE = 0x72; // 'r'

	/* Output response from Arduino */
	public static final byte RES_COMFIRM_CONNECTION = 0x06;// ASCII ACK
	public static final byte RES_INVALID_OPERATION = 0x15;
	public static final byte RES_SUCCESS = 0x06;// ACK
	public static final byte RES_FAIL = (byte) 0xFF;
	public static final byte RES_TIMEOUT = (byte) 0xFE;
}
