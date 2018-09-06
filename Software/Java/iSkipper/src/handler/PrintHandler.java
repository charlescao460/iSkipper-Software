/**
 * 
 */
package handler;

import device.ReceivedPacketEvent;
import support.Transcoding;

/**
 * @author CSR
 *
 */
public class PrintHandler implements ReceivedPacketHandlerInterface
{

	private boolean shouldPrint = true;

	@Override
	public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent)
	{
		if (shouldPrint)
			System.out.print(Transcoding.bytesToString(packetEvent.getReceivedData()));
	}

	/**
	 * Stop print the data to Standard Out, just abandon them.
	 */
	public void stopPrint()
	{
		shouldPrint = false;
	}

	/**
	 * Start to print, to resume the stopping status by {@link #stopPrint()}.
	 */
	public void startPrint()
	{
		shouldPrint = true;
	}

}
