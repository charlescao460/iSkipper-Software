package support;

/**
 * The class for storage of iClicker Answer Packet.</br>
 * 
 * Some useful methods (converters between int32 to byte[4], and a method to
 * check the validity of iClicker ID) are also included in this class.
 * 
 * @author CSR
 *
 */
public class AnswerPacket implements Cloneable
{
	private IClickerID iClickerID;
	private Answer answer;

	/**
	 * The default constructor. Set ID to be 0x00000000 and the answer to P (Ping)
	 */
	public AnswerPacket()
	{
		iClickerID = new IClickerID();
		answer = Answer.P;
	}

	/**
	 * @param answer
	 *            the iClicker Answer
	 * @param ID
	 *            the iClicker ID.
	 */
	public AnswerPacket(Answer answer, IClickerID id)
	{
		this.answer = answer;
		this.iClickerID = id;
	}

	/**
	 * @return the iClicker ID in this packet.
	 */
	public IClickerID getID()
	{
		return iClickerID;
	}

	/**
	 * @return the answer in this packet.
	 */
	public Answer getAnswer()
	{
		return answer;
	}

	/**
	 * @param answer
	 *            the answer to set in this packet.
	 */
	public void setAnswer(Answer answer)
	{
		this.answer = answer;
	}

	@Override
	public Object clone()
	{
		return new AnswerPacket(this.answer, ((IClickerID) this.iClickerID.clone()));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !obj.getClass().equals(this.getClass()))
			return false;
		AnswerPacket other = (AnswerPacket) obj;
		return other.iClickerID.equals(this.iClickerID) && other.answer.equals(this.answer);
	}

	/**
	 * @return the ID</br>
	 * 
	 *         Since each of the iClicker IDs should be unique in this world, we
	 *         could just directly use it as a hashCode.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.iClickerID.getID();
	}

}
