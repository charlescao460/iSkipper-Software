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

	@Override
	public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent)
	{
		System.out.print(Transcoding.bytesToString(packetEvent.getReceivedData()));
	}

}
