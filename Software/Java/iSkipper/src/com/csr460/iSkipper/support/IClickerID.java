/**
 * 
 */
package com.csr460.iSkipper.support;

import java.util.Random;

/**
 * 
 * The class for storage iClicker ID, which is an DWORD
 * (32-bits-unsigned-integer, uint32) with Xor verification.
 * 
 * @author CSR
 */
public class IClickerID implements Cloneable
{
	public static final int ID_HEX_STRING_LENGTH = 8;// iClicker ID could be represented as a string
														// of 8 chars
	private int ID;
	private byte[] arrID;

	/**
	 * The default constructor. Set ID to be 0x00000000.
	 * 
	 */
	public IClickerID()
	{
		ID = 0x00_00_00_00;
		arrID = Transcoding.intToByteArray(ID);
	}

	/**
	 * @param ID
	 *            the iClicker ID,represented as an uint32, must be valid.
	 */
	public IClickerID(int id)
	{
		if (!isValidID(ID))
		{
			IClickerID.onInvalidID();
			return;
		}
		this.ID = id;
		this.arrID = Transcoding.intToByteArray(ID);
	}

	/**
	 * * @param ID the iClicker ID,represented as an array of bytes, must be valid.
	 */
	public IClickerID(byte[] arrID)
	{
		if (!isValidID(arrID))
		{
			IClickerID.onInvalidID();
			return;
		}
		this.arrID = arrID;
		this.ID = Transcoding.byteArrayToInt(arrID);
	}

	/**
	 * @param id
	 *            the ID to set, as an uint32, must be valid.
	 */
	public void setID(int id)
	{
		if (!isValidID(id))
		{
			IClickerID.onInvalidID();
			return;
		}
		this.ID = id;
		arrID = Transcoding.intToByteArray(id);
	}

	/**
	 * @param arrID
	 *            the ID to set, as an array of bytes, must be valid.
	 */
	public void setID(byte[] arrID)
	{
		if (!isValidID(arrID))
		{
			IClickerID.onInvalidID();
			return;
		}
		this.arrID = arrID;
		this.ID = Transcoding.byteArrayToInt(arrID);
	}

	/**
	 * @return the iClicker ID, represented as an array of bytes.
	 */
	public byte[] getArrID()
	{
		return arrID;
	}

	/**
	 * @return the iClicker ID, represented as an uint32.
	 */
	public int getID()
	{
		return ID;
	}

	@Override
	public Object clone()
	{
		return new IClickerID(ID);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj.getClass().equals(this.getClass()) ? ((IClickerID) obj).getID() == this.ID : false;
	}

	/**
	 * @param arrID
	 *            the iClicker ID, represented as an uint32.
	 * @return if it is a valid iClicker ID
	 */
	public static boolean isValidID(int id)
	{
		return isValidID(Transcoding.intToByteArray(id));
	}

	/**
	 * @param arrID
	 *            the iClicker ID, represented as an array of bytes.
	 * @return if it is a valid iClicker ID
	 */
	public static boolean isValidID(byte[] arrID)
	{
		if (arrID.length != 4)
			return false;
		return ((arrID[0] ^ arrID[1] ^ arrID[2]) == arrID[3]);
	}

	/**
	 * The method that would be invoked when trying to set a invalid ID. Since we
	 * don't want some invalid IDs stop our program, we just print the information.
	 */
	static void onInvalidID()
	{
		IllegalArgumentException exception = new IllegalArgumentException("Invalid ID");
		exception.printStackTrace();
	}

	/**
	 * @param str
	 *            the ID in a string, as an hex number.
	 * @return the IClickerID. Null if the input cannot be parsed into a valid ID.
	 */
	public static IClickerID idFromString(String str)
	{
		try
		{
			int id = (int) Long.parseLong(str, 16);
			if (!isValidID(id))
				return null;
			return new IClickerID(id);
		} catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * @return A random IClickerID. Maybe useful for testing.
	 */
	public static IClickerID getRandomID()
	{
		Random random = new Random();
		byte arrID[] = new byte[4];
		random.nextBytes(arrID);
		arrID[3] = (byte) (arrID[0] ^ arrID[1] ^ arrID[2]);
		return new IClickerID(arrID);
	}

	/**
	 * Return the string of the ID.
	 */
	@Override
	public String toString()
	{
		return String.format("%02X%02X%02X%02X", arrID[0], arrID[1], arrID[2], arrID[3]);
	}

}
