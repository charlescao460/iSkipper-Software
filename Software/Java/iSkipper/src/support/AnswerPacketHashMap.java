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
	 *            the AnswerPacket to put into this HashMap
	 * @return the previous answer of the ID of this packet (null if the ID is newly
	 *         added).
	 * @see java.util.HashMap#put(Object, Object)
	 */
	public Answer put(AnswerPacket answerPacket)
	{
		answerCounter.put(answerPacket.getAnswer(), answerCounter.get(answerPacket.getAnswer()) + 1);
		return super.put(answerPacket.hashCode(), answerPacket.getAnswer());
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

}
