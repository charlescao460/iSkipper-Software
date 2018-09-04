/**
 * 
 */
package support;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Utility class to storage Answer Packets in a HashMap. Since each of the
 * iClicker IDs is unique, we use it as the hashCode.
 * 
 * @author CSR
 */
public class AnswerPacketHashMap extends HashMap<Integer, Answer>
{

	private static final long serialVersionUID = 3016073673619431412L;
	private static final int INITIAL_PACKET_HASHMAP_SIZE = 256;
	private static final float INITIAL_PACKET_HASHMAP_LOADFACTOR = 0.80f;

	private Hashtable<Answer, Integer> answerCounter;
	private int numsTotalPacketRecieved;

	public AnswerPacketHashMap()
	{
		super(INITIAL_PACKET_HASHMAP_SIZE, INITIAL_PACKET_HASHMAP_LOADFACTOR);
		Answer[] answers = Answer.values();
		answerCounter = new Hashtable<>(answers.length);
		for (Answer a : answers)
		{
			answerCounter.put(a, 0);
		}
	}

	/**
	 * @param answerPacket
	 *            the AnswerPacket to put into this HashMap.
	 * @return the previous answer of the ID of this packet (null if the ID is newly
	 *         added).
	 * @throws NullPointerException
	 *             if the answerPacket is null.
	 * @see java.util.HashMap#put(Object, Object)
	 */
	public Answer put(AnswerPacket answerPacket)
	{
		if (answerPacket == null)
			throw new NullPointerException("Cannot put a null packet into the HashMap.");
		Answer prevAnswer = super.put(answerPacket.hashCode(), answerPacket.getAnswer());
		if (prevAnswer != null && prevAnswer != answerPacket.getAnswer())
		{
			answerCounter.put(prevAnswer, answerCounter.get(prevAnswer) - 1);
			answerCounter.put(answerPacket.getAnswer(), answerCounter.get(answerPacket.getAnswer()) + 1);
		} else if (prevAnswer == null)
			answerCounter.put(answerPacket.getAnswer(), answerCounter.get(answerPacket.getAnswer()) + 1);
		numsTotalPacketRecieved++;
		return prevAnswer;
	}

	/**
	 * @param ID
	 *            the iClicker ID
	 * 
	 * @return the answer of input iClicker ID, null if the ID doesn't exit in this
	 *         HashMap
	 * @see java.util.HashMap#get(Object)
	 */
	public Answer get(Integer ID)
	{
		return super.get(ID);
	}

	/**
	 * @param answer
	 * @return the count of the input answer
	 */
	public int getAnswerCount(Answer answer)
	{
		return answerCounter.get(answer);
	}

	/**
	 * @return the total number of the packet that have been received in this
	 *         HashMap.
	 */
	public int getNumsTotalPacketRecieved()
	{
		return numsTotalPacketRecieved;
	}

	/**
	 * @return the total number of IDs in this HashMap, which could also indicate
	 *         the total number of the people in the class.
	 */
	public int getNumsTotalIDs()
	{
		return this.size();
	}

}
