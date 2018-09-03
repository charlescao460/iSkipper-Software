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
	 * <Answer>,<ID>\n
	 */
	private static final int CAPTURE_ANSWER_INDEX = 0;
	private static final int CAPTURE_ID_INDEX = 2;

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
		String response = Transcoding.bytesToString(packetEvent.getReceivedData());
		AnswerPacket packet = parseInput(response);
		if (packet == null)
		{
			System.err.println("This capture is corrupted.");
			return;
		}
		hashMap.put(packet);
		System.out.print(response);
		System.out.format("Current record: A:%d, B:&d, C:%d, D:%d, E:%d, Total:%d\n", hashMap.getAnswerCount(Answer.A),
				hashMap.getAnswerCount(Answer.B), hashMap.getAnswerCount(Answer.C), hashMap.getAnswerCount(Answer.D),
				hashMap.getAnswerCount(Answer.E), hashMap.size());
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
