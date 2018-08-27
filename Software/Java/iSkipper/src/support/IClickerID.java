/**
 * 
 */
package support;

/**
 * 
 * The class for storage iClicker ID, which is an DWORD
 * (32-bits-unsigned-integer, uint32) with Xor verification.
 * 
 * @author CSR
 */
public class IClickerID implements Cloneable
{
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
		if (arrID.length < 4)
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

}
