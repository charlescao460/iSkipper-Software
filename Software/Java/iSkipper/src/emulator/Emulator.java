/**
 * 
 */
package emulator;

import device.SerialAdapter;
import handler.ReceivedPacketHandlerInterface;
import support.IClickerID;
import support.Transcoding;

/**
 * @author CSR
 *
 *         The main emulator class.
 */
public class Emulator
{
	private int SERIAL_WAIT_TIME = 1000;
	private SerialAdapter serial;
	private EmulatorModes mode;
	private ReceivedPacketHandlerInterface handler;
	private IClickerID emulatorID;

	public Emulator(SerialAdapter serialPort, ReceivedPacketHandlerInterface handler)
	{
		if (serialPort == null)
			throw new NullPointerException("Serial port object is null when constructing an Emulator object.\n");
		if (!serialPort.isAvailable())
			throw new IllegalArgumentException("Serial port is not available when constructing an Emulator object.\\n");
		if (handler == null)
			throw new NullPointerException("A handler is null when constructing an Emulator object.\n");
		this.serial = serialPort;
		this.mode = EmulatorModes.DISCONNECTED;
		this.handler = handler;
		this.emulatorID = new IClickerID();
	}

	/**
	 * The method to confirm connection with the iSkipper hardware.
	 * 
	 * @return whether successfully initialized
	 */
	public boolean initialize()
	{
		if (mode != EmulatorModes.DISCONNECTED)
			return false;
		serial.writeByte(SerialSymbols.OP_COMFIRM_CONNECTION);// Send command
		serial.setPacketHandler((event) ->
		{
			String resopnse = Transcoding.bytesToString(event.getReceivedData());
			System.out.print(resopnse);
			if (mode == EmulatorModes.STANDBY)
				return;
			try
			{
				// There should be one line contains the ID
				int id = Integer.parseUnsignedInt(resopnse.substring(0, IClickerID.ID_HEX_STRING_LENGTH), 16);
				if (IClickerID.isValidID(id))
				{
					emulatorID = new IClickerID(id);
					mode = EmulatorModes.STANDBY;
				}
			} catch (Exception e)
			{
				// keep going
			}
		});
		wait(SERIAL_WAIT_TIME);
		serial.setPacketHandler(handler);
		return mode == EmulatorModes.STANDBY;
	}

	/**
	 * @return the serial adapter
	 */
	public SerialAdapter getSerial()
	{
		return serial;
	}

	/**
	 * @param serial
	 *            the serial port to set
	 */
	public void setSerial(SerialAdapter serial)
	{
		this.serial = serial;
	}

	/**
	 * @return the mode
	 */
	public EmulatorModes getMode()
	{
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(EmulatorModes mode)
	{
		this.mode = mode;
	}

	/**
	 * @return the serial packet handler
	 */
	public ReceivedPacketHandlerInterface getHandler()
	{
		return handler;
	}

	/**
	 * @param handler
	 *            the serial packet handler to set
	 */
	public void setHandler(ReceivedPacketHandlerInterface handler)
	{
		this.handler = handler;
	}

	/**
	 * @return the fixed iClickerID of this emulator from the hardware.
	 */
	public IClickerID getEmulatorID()
	{
		return emulatorID;
	}

	/**
	 * The internal function to suspend the current thread to wait for serial
	 * response.
	 * 
	 * @param millis
	 *            time to wait.
	 */
	private void wait(int millis)
	{
		try
		{
			Thread.sleep(millis);
		} catch (InterruptedException e)
		{
			// keep going
		}
	}

	@SuppressWarnings("unused")
	private static final class SerialSymbols
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

		/* Output respons from Arduino */
		public static final byte RES_COMFIRM_CONNECTION = 0x06;// ASCII ACK
		public static final byte RES_INVALID_OPERATION = 0x15;
		public static final byte RES_SUCCESS = 0x06;// ACK
		public static final byte RES_FAIL = (byte) 0xFF;
		public static final byte RES_TIMEOUT = (byte) 0xFE;
	}

}
