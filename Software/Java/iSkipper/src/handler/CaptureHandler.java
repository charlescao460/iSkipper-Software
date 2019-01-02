package handler;

import device.ReceivedPacketEvent;
import support.Answer;
import support.AnswerPacket;
import support.AnswerPacketHashMap;
import support.IClickerID;
import support.Transcoding;

/**
 * @author CSR
 *
 *         The handler for Capture mode.
 */
public class CaptureHandler implements ReceivedPacketHandlerInterface
{
	/*
	 * Output in capture mode:
	 * 
	 * Captured,<Answer>,<ID>\n
	 */
	private static final int CAPTURE_ANSWER_INDEX = 9;
	private static final int CAPTURE_ID_INDEX = 11;

	protected AnswerPacketHashMap hashMap;
	private boolean shouldPrintRaw;
	private boolean shouldPrintStatis;

	/**
	 * The default constructor, use a new empty AnswerPacketHashMap and print
	 * everything.
	 */
	public CaptureHandler()
	{
		hashMap = new AnswerPacketHashMap();
	}

	/**
	 * @param hashMap
	 *            Construct this handler with an existing AnswerPacketHashMap to
	 *            continue recording.
	 * @param shouldPrintRaw
	 *            Whether print the raw data when receiving a response or not.
	 * @param shouldPrintStatis
	 *            Whether print the statistic of the answers when receiving a
	 *            response or not.
	 */
	public CaptureHandler(AnswerPacketHashMap hashMap, boolean shouldPrintRaw, boolean shouldPrintStatis)
	{
		if (hashMap == null)
			throw new NullPointerException();
		this.hashMap = hashMap;
		this.shouldPrintRaw = shouldPrintRaw;
		this.shouldPrintStatis = shouldPrintStatis;
	}

	@Override
	public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent)
	{
		String response = Transcoding.bytesToString(packetEvent.getReceivedData());
		AnswerPacket packet = parseInput(response);
		if (packet == null)
		{
			if (shouldPrintRaw || shouldPrintStatis)
				System.err.println("This capture is corrupted.");
			return;
		}
		hashMap.put(packet);
		if (shouldPrintRaw)
			System.out.print(response);
		if (shouldPrintStatis)
			System.out.format("Current record: A:%d, B:%d, C:%d, D:%d, E:%d, CountIDs:%d, CountPackets:%d\n",
					hashMap.getAnswerCount(Answer.A), hashMap.getAnswerCount(Answer.B),
					hashMap.getAnswerCount(Answer.C), hashMap.getAnswerCount(Answer.D),
					hashMap.getAnswerCount(Answer.E), hashMap.getNumsTotalIDs(), hashMap.getNumsTotalPacketRecieved());
	}

	/**
	 * @return the AnswerPacketHashMap. Use it to get count of the answers.
	 */
	public AnswerPacketHashMap getHashMap()
	{
		return hashMap;
	}

	/**
	 * 
	 * @param line
	 * @return the answer and ID in the line as an AnswerPacket. Null if the input
	 *         cannot be parsed into a valid AnswerPacket.
	 */
	public AnswerPacket parseInput(String line)
	{
		try
		{
			IClickerID id = IClickerID
					.idFromString(line.substring(CAPTURE_ID_INDEX, CAPTURE_ID_INDEX + IClickerID.ID_HEX_STRING_LENGTH));
			if (id == null)
				return null;
			return new AnswerPacket(Answer.charAnswer(line.charAt(CAPTURE_ANSWER_INDEX)), id);
		} catch (Exception e)
		{
			return null;
		}
	}

}
