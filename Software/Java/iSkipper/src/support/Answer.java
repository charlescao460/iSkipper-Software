package support;

/**
 * The enumeration class that represents iClicker answers
 * 
 * @author CSR
 * @see java.lang.Enum
 */
public enum Answer
{
	A('A'), B('B'), C('C'), D('D'), E('E'), P('P');
	@SuppressWarnings("unused")
	private char answer;

	private Answer(char answer)
	{
		this.answer = answer;
	}

	/**
	 * @param answer
	 *            the char of one of following:
	 *            'A','B','C',D',E','P','a','b','c','d','e','p'.
	 * @return The Answer Object represents the input char
	 * @throws IllegalArgumentException
	 *             when the input char cannot be resolved as an valid iClicker
	 *             answer.
	 * @see java.lang.Enum#valueOf(Class, String)
	 */
	public static Answer charAnswer(char answer) throws IllegalArgumentException
	{
		return Answer.valueOf(String.valueOf(answer).toUpperCase());
	}

}
