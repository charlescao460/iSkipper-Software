/**
 * 
 */
package com.csr460.iSkipper.handler;

import java.util.EventListener;

import com.csr460.iSkipper.device.ReceivedPacketEvent;

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
