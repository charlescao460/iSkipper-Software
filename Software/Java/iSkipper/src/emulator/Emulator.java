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
	private static final int SERIAL_WAIT_TIME = 100;
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
			// This packet handler are called in another thread.
			String resopnse = Transcoding.bytesToString(event.getReceivedData());
			System.out.print(resopnse);
			if (mode == EmulatorModes.STANDBY)
				return;
			try
			{
				// There should be one line contains the fixed ID
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
		wait(SERIAL_WAIT_TIME);// Wait for above process in another thread.
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

}
