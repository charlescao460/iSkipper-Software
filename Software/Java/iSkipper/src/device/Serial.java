package device;

import com.fazecast.jSerialComm.SerialPort;

import handler.PrintHandler;
import handler.ReceivedPacketHandlerInterface;

/**
 * The class for all SerialPort communications.</br>
 * 
 * It aggregates with com.fazecast.jSerialComm.SerialPort
 * 
 * @see <a href=
 *      "http://fazecast.github.io/jSerialComm/javadoc/com/fazecast/jSerialComm/SerialPort.html"
 *      >com.fazecast.jSerialComm.SerialPort</a>
 * @author CSR
 *
 */
public class Serial
{
	private final static int WRITE_TIMEOUT = 10_000;
	private final static int READ_TIMEOUT = 10_000;
	private SerialPort serialPort;
	private SerialPort[] availablePorts;
	private SerialListener listener;
	private ReceivedPacketHandlerInterface packetHandler;

	public Serial()
	{
		serialPort = null;
		availablePorts = SerialPort.getCommPorts();
		listener = new SerialListener(new PrintHandler());
	}

	/**
	 * @return Available ports' names in a String array
	 */
	public String[] getAvailablePortsByNames()
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
	 *            of port in availablePorts array
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
	 * Set the parameters of port to be 115200,8,1,N
	 */
	public void initializePort()
	{
		if (serialPort == null)
			return;
		serialPort.setComPortParameters(/* BaudRate */115200, /* DataBits */ 8, /* StopBits */1,
				/* Parity */SerialPort.NO_PARITY);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, READ_TIMEOUT, WRITE_TIMEOUT);
		serialPort.addDataListener(listener);
	}

	/**
	 * @param toWrite
	 *            send data to this serial port
	 */
	public void writeBytes(byte[] toWrite)
	{
		serialPort.writeBytes(toWrite, toWrite.length);
	}

	/**
	 * @return the packetHandler
	 */
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
	public void setPacketHandler(ReceivedPacketHandlerInterface packetHandler)
	{
		if (packetHandler == null)
			throw new NullPointerException("Cannot use a null packetHandler!");
		this.packetHandler = packetHandler;
		listener.packetHandler = packetHandler;
	}

}
