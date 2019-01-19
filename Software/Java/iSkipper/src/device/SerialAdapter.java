package device;

import com.fazecast.jSerialComm.SerialPort;

import handler.PrintHandler;
import handler.ReceivedPacketHandlerInterface;

/**
 * The class for all SerialPort communications.</br>
 * 
 * An adapter class for com.fazecast.jSerialComm.SerialPort
 * 
 * @see <a href=
 *      "http://fazecast.github.io/jSerialComm/javadoc/com/fazecast/jSerialComm/SerialPort.html"
 *      >com.fazecast.jSerialComm.SerialPort</a>
 * @author CSR
 *
 */
public class SerialAdapter extends AbstractSerialAdapter
{
	private final static int OPEN_SERIAL_WAIT_TIME = 1500;
	private final static int WRITE_TIMEOUT = 10_000;
	private final static int READ_TIMEOUT = 10_000;
	/* 115200,8,1,N for Arduino's port */
	private final static int SERIAL_PORT_BAUD_RATE = 115200;
	private final static int SERIAL_PORT_DATA_BITS = 8;
	private final static int SERIAL_PORT_STOP_BITS = 1;
	private final static int SERIAL_PORT_PARITY = SerialPort.NO_PARITY;
	private SerialPort serialPort;
	private SerialPort[] availablePorts;
	private SerialListener listener;
	private ReceivedPacketHandlerInterface packetHandler;

	public SerialAdapter()
	{
		serialPort = null;
		availablePorts = SerialPort.getCommPorts();
		listener = new SerialListener(new PrintHandler());
	}

	/**
	 * @return All local serial ports' names in String array.
	 */
	public String[] getAllPortsByNames()
	{
		String[] ret = new String[availablePorts.length];

		for (int i = 0; i < ret.length; i++)
		{
			ret[i] = availablePorts[i].getDescriptivePortName();
		}

		return ret;
	}

	/**
	 * @param index
	 *            of port in the array returned by
	 *            {@link SerialAdapter#getAvailablePortsByNames}
	 * @return if the port was successfully opened
	 */
	public boolean setSerialPort(int index)
	{
		if (index < 0 || index >= availablePorts.length)
			return false;
		serialPort = availablePorts[index];
		serialPort.closePort();// close and re-open to avoiding potential conflict
		if (serialPort.openPort())
		{
			initializePort();
			return true;
		} else
		{
			serialPort = null;
			return false;
		}
	}

	/**
	 * @return whether this serial port is available for communication.
	 */
	@Override
	public boolean isAvailable()
	{
		return serialPort != null ? serialPort.isOpen() : false;
	}

	/**
	 * Close the selected port.
	 * 
	 * @return Whether successfully close the port.
	 */
	@Override
	public boolean close()
	{
		if (serialPort != null)
		{
			return serialPort.closePort();
		}
		return false;
	}

	/**
	 * @param toWrite
	 *            send data to this serial port
	 */
	@Override
	public void writeBytes(byte[] toWrite)
	{
		serialPort.writeBytes(toWrite, toWrite.length);
	}

	/**
	 * @param toWrite
	 *            send one single byte to this serial port
	 */
	@Override
	public void writeByte(byte toWrite)
	{
		serialPort.writeBytes(new byte[]
		{ toWrite }, 1);
	}

	/**
	 * @return the packetHandler
	 */
	@Override
	public ReceivedPacketHandlerInterface getPacketHandler()
	{
		return packetHandler;
	}

	/**
	 * @param packetHandler
	 *            the packetHandler to set
	 * 
	 * @throws NullPointerException
	 *             when the input was null
	 */
	@Override
	public void setPacketHandler(ReceivedPacketHandlerInterface packetHandler)
	{
		if (packetHandler == null)
			throw new NullPointerException("Cannot use a null packetHandler!");
		this.packetHandler = packetHandler;
		listener.packetHandler = packetHandler;
	}

	/**
	 * Set the parameters of port.
	 */
	private void initializePort()
	{
		if (serialPort == null)
			return;
		serialPort.setComPortParameters(/* BaudRate */SERIAL_PORT_BAUD_RATE, /* DataBits */ SERIAL_PORT_DATA_BITS,
				/* StopBits */SERIAL_PORT_STOP_BITS, /* Parity */SERIAL_PORT_PARITY);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, READ_TIMEOUT, WRITE_TIMEOUT);
		serialPort.addDataListener(listener);
		try
		{
			// Must wait for a while after the port was opened.
			// It is weird but I can't fix it.
			// The jSerialComm's documents wasn't helpful for this problem.
			Thread.sleep(OPEN_SERIAL_WAIT_TIME);
		} catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}
}
