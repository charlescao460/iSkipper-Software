package device;

import java.util.EventObject;

import support.Transcoding;

/**
 * The event that triggered when the SerialListener had received a complete
 * response packet(An ASCII string ended by '\0').
 * 
 * @author CSR
 * @see java.util.EventObject
 */
public class ReceivedPacketEvent extends EventObject
{

	private static final long serialVersionUID = 5027031631883402646L;
	private byte[] data;

	/**
	 * @param source
	 *            the event's sender, normally just use "this"
	 * @param data
	 *            the data that received by SerialListner
	 */
	public ReceivedPacketEvent(Object source, byte[] data)
	{
		super(source);
		this.data = data;
	}

	/**
	 * @return the received response data in this packet.
	 */
	public byte[] getReceivedData()
	{
		return data;
	}

	/**
	 * Search the specified array in the packet data.
	 * 
	 * @param toSearch
	 * @return Whether the packet data contains this array.
	 */
	public boolean dataContains(byte[] toSearch)
	{
		traverseData:
		for (int iData = 0; iData < data.length; iData++)
		{
			if (iData + toSearch.length > data.length)
				break traverseData;
			for (int iToSearch = 0; iToSearch < toSearch.length; iToSearch++)
			{
				if (data[iData + iToSearch] != toSearch[iToSearch])
					continue traverseData;
			}
			return true;
		}
		return false;
	}

	/**
	 * Search the specified byte in the packet data.
	 * 
	 * @param toSearch
	 * @return Whether the packet data contains this byte.
	 */
	public boolean dataContains(byte toSearch)
	{
		for (byte b : data)
		{
			if (b == toSearch)
				return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return Transcoding.bytesToString(data);
	}

}
