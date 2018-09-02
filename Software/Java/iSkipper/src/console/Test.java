package console;

import java.util.Scanner;

import device.SerialAdapter;
import emulator.Emulator;
import handler.PrintHandler;

public class Test
{

	public static void main(String[] args) throws InterruptedException
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
		Thread.sleep(2000);

		Emulator emulator = new Emulator(serial, new PrintHandler());
		boolean initialized = emulator.initialize();
		System.out.println(initialized);
		while (true)
		{
			scanner.nextLine();
		}
	}

}
