package handler;

import device.ReceivedPacketEvent;
import support.AnswerPacketHashMap;

/**
 * @author CSR
 *
 *         The handler for Capture mode.
 */
public class CaptureHandler implements ReceivedPacketHandlerInterface
{

	private AnswerPacketHashMap hashMap;

	/**
	 * The default constructor, use a new empty AnswerPacketHashMap
	 */
	public CaptureHandler()
	{
		hashMap = new AnswerPacketHashMap();
	}

	/**
	 * @param hashMap
	 * 
	 *            Construct this handler with an existing AnswerPacketHashMap to
	 *            continue recording.
	 */
	public CaptureHandler(AnswerPacketHashMap hashMap)
	{
		if (hashMap == null)
			throw new NullPointerException();
		this.hashMap = hashMap;
	}

	@Override
	public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @return the AnswerPacketHashMap. Use it to get count of the answers.
	 */
	public AnswerPacketHashMap getHashMap()
	{
		return hashMap;
	}

}
