package application.utils;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * The PrintStream to print the text on a dialog.
 * 
 * @author CSR
 *
 */
public class DialogPrintStream extends PrintStream
{

	private TextDialog dialog;

	public DialogPrintStream(OutputStream out, TextDialog dialog)
	{
		super(out);
		if (dialog == null)
			throw new NullPointerException("TextFlowDialog cannot be null!");
		this.dialog = dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#flush()
	 */
	@Override
	public void flush()
	{
		dialog.clear();
		super.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(boolean)
	 */
	@Override
	public void print(boolean b)
	{
		dialog.add(String.valueOf(b));
		super.print(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(char)
	 */
	@Override
	public void print(char c)
	{
		dialog.add(String.valueOf(c));
		super.print(c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(int)
	 */
	@Override
	public void print(int i)
	{
		dialog.add(String.valueOf(i));
		super.print(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(long)
	 */
	@Override
	public void print(long l)
	{
		dialog.add(String.valueOf(l));
		super.print(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(float)
	 */
	@Override
	public void print(float f)
	{
		dialog.add(String.valueOf(f));
		super.print(f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(double)
	 */
	@Override
	public void print(double d)
	{
		dialog.add(String.valueOf(d));
		super.print(d);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(char[])
	 */
	@Override
	public void print(char[] s)
	{
		dialog.add(String.valueOf(s));
		super.print(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(java.lang.String)
	 */
	@Override
	public void print(String s)
	{
		dialog.add(s, true);
		super.print(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#print(java.lang.Object)
	 */
	@Override
	public void print(Object obj)
	{
		dialog.add(obj.toString());
		super.print(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.PrintStream#println()
	 */
	@Override
	public void println()
	{
		dialog.add("", true);
		super.println();
	}

}
