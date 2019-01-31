/**
 * 
 */
package com.csr460.iSkipper.device;

import com.csr460.iSkipper.handler.ReceivedPacketHandlerInterface;

/**
 * The abstract class for serial adapter. Extend this class to use your own
 * serial library.
 * 
 * @author CSR
 *
 *
 */
public abstract class AbstractSerialAdapter
{
	protected volatile ReceivedPacketHandlerInterface packetHandler;

	/**
	 * @param packetHandler
	 *            the packetHandler to set
	 * 
	 * @throws NullPointerException
	 *             when the input was null
	 */
	public synchronized void setPacketHandler(ReceivedPacketHandlerInterface packetHandler)
	{
		if (packetHandler == null)
			throw new NullPointerException("Cannot use a null packetHandler!");
		this.packetHandler = packetHandler;
	}

	/**
	 * @return the packetHandler
	 */
	public ReceivedPacketHandlerInterface getPacketHandler()
	{
		return packetHandler;
	}

	/**
	 * @return whether this serial port is available for communication.
	 */
	abstract public boolean isAvailable();

	/**
	 * @return Whether successfully close the port.
	 */
	abstract public boolean close();

	/**
	 * @param toWrite
	 *            send data to this serial port
	 */
	abstract public void writeBytes(byte[] toWrite);

	/**
	 * @param toWrite
	 *            send one single byte to this serial port
	 */
	abstract public void writeByte(byte toWrite);

}
