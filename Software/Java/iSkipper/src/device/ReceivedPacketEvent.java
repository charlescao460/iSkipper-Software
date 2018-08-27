package device;

import java.util.EventObject;

/**
 * The event that triggered when the SerialListener had received a complete
 * response packet(An ASCII string ended by '\0).
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

}
