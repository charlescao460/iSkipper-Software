package support;

/**
 * Class for converting between different encodings. (e.g. ASCII and Unicode).
 * 
 * @author Charles Cao (CSR)
 *
 */
public final class Transcoding
{

	/**
	 * @param str
	 *            the input String, must not contains any non-ASCII character
	 * @return ASCII string, as an array of bytes represented ASCII values of the
	 *         input string. Notice that '\0' was added by this method
	 */
	public static byte[] stringToBytes(String str)
	{
		char[] arrChar = str.toCharArray();
		byte[] ret = new byte[arrChar.length + 1];
		for (int i = 0; i < arrChar.length; i++)
		{
			ret[i] = (byte) arrChar[i];
		}
		ret[arrChar.length] = '\0';
		return ret;
	}

	/**
	 * @param bytes
	 *            ASCII string, as an input array of bytes represented ASCII values
	 *            of the input string
	 * @return array of characters represented the input ASCII string
	 */
	public static char[] bytesToCharArray(byte[] bytes)
	{
		char[] arrChar = new char[bytes.length];
		for (int i = 0; i < arrChar.length; i++)
		{
			arrChar[i] = (char) bytes[i];
		}
		return arrChar;
	}

	/**
	 * @param bytes
	 *            ASCII string, as an input array of bytes represented ASCII values
	 *            of the input string
	 * @return Unicode string in Java
	 */
	public static String bytesToString(byte[] bytes)
	{
		return String.valueOf(bytesToCharArray(bytes));
	}

	/**
	 * @param input
	 *            an int32
	 * @return an array of bytes, representing the input int32.(Little Endian)
	 */
	public static byte[] intToByteArray(int input)
	{
		return new byte[]
		{ (byte) (input >> 24 & 0xFF), (byte) (input >> 16 & 0xFF), (byte) (input >> 8 & 0xFF), (byte) (input & 0xFF) };
	}

	/**
	 * @param input
	 *            an array of bytes.
	 * @return an int32, representing the input byte array.(Little Endian)
	 */
	public static int byteArrayToInt(byte[] input)
	{
		try
		{
			return input[3] & 0xFF | (input[2] & 0xFF) << 8 | (input[1] & 0xFF) << 16 | (input[0] & 0xFF) << 24;
		} catch (IndexOutOfBoundsException e)
		{
			System.err.println(e);
			e.printStackTrace();
		}
		return -1;
	}

}
