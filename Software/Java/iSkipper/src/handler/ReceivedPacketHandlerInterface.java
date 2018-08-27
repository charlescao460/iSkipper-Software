/**
 * 
 */
package handler;

import java.util.EventListener;

import device.ReceivedPacketEvent;

/**
 * The interface of response packet Handler
 * 
 * @author CSR
 * @see java.util.EventListener
 *
 */
public interface ReceivedPacketHandlerInterface extends EventListener
{
	public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent);
}
