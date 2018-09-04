/**
 * 
 */
package emulator;

import device.SerialAdapter;
import handler.CaptureHandler;
import handler.ReceivedPacketHandlerInterface;
import support.AnswerPacketHashMap;
import support.IClickerID;

/**
 * @author CSR
 *
 *         The main emulator class.
 */
public class Emulator
{
	private static final int SERIAL_WAIT_TIME = 5000;
	private SerialAdapter serial;
	private volatile EmulatorModes mode;
	private volatile ReceivedPacketHandlerInterface handler;
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
		serial.setPacketHandler((packet) ->
		{
			// This packet handler are called in another thread.
			System.out.print(packet);
			if (mode == EmulatorModes.STANDBY)
				return;
			try
			{
				// There should be one line contains the fixed ID
				IClickerID id = IClickerID
						.idFromString(packet.toString().substring(0, IClickerID.ID_HEX_STRING_LENGTH));
				if (id != null)
				{
					emulatorID = id;
					mode = EmulatorModes.STANDBY;
					wakeEmulator();
				}
			} catch (Exception e)
			{
				// keep going
			}
		});
		waitForHandler();
		serial.setPacketHandler(handler);
		return mode == EmulatorModes.STANDBY;
	}

	public boolean startCapture(AnswerPacketHashMap storageHashMap)
	{
		if (mode != EmulatorModes.STANDBY)
			return false;
		serial.writeByte(SerialSymbols.OP_CAPTURE);
		serial.setPacketHandler((packet) ->
		{

			if (packet.dataContains(SerialSymbols.RES_SUCCESS))
			{
				handler = new CaptureHandler(storageHashMap);
				mode = EmulatorModes.CAPTURE;
				wakeEmulator();
				return;
			}
			System.out.print(packet);
		});
		waitForHandler();
		serial.setPacketHandler(handler);
		return mode == EmulatorModes.CAPTURE;
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
	 * Used to suspend the current thread to wait for handler to done its process.
	 */
	private synchronized void waitForHandler()
	{
		try
		{
			this.wait(SERIAL_WAIT_TIME);
		} catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Used in handler class when it finished its job and resume the thread.
	 */
	private synchronized void wakeEmulator()
	{
		this.notifyAll();
	}

}
