package console;

import java.util.Scanner;

import device.SerialAdapter;
import support.Transcoding;

public class COMMonitor
{

	public static void main(String[] args)
	{
		SerialAdapter serial = new SerialAdapter();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Here are the available port:");
		String[] portNames = serial.getAllPortsByNames();
		for (int i = 0; i < portNames.length; i++)
		{
			System.out.println(i + " " + portNames[i]);
		}
		System.out.println("Please make a choice");
		int index = -1;
		while (index < 0 || index >= portNames.length)
		{
			try
			{
				index = scanner.nextInt();
			} catch (Exception e)
			{
				System.out.println("Illegal input!");
				scanner.nextLine();
			}
		}
		if (!serial.setSerialPort(index))
		{
			System.err.println("Cannot open this port!");
		}
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true)
		{
			String command = scanner.nextLine();
			if (command.contains("stop"))
				System.exit(0);
			serial.writeBytes(Transcoding.stringToBytes(command));
		}

	}

}
