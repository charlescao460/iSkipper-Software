package console;

import java.util.Scanner;

import device.SerialAdapter;
import emulator.Emulator;
import emulator.EmulatorModes;
import handler.PrintHandler;
import support.AnswerPacketHashMap;
import support.IClickerChannel;

public class Test
{

	private static final int SERIAL_OPEN_WAIT_TIME = 1500;

	public static void main(String[] args) throws InterruptedException
	{
		Scanner scanner = new Scanner(System.in);
		SerialAdapter serial = askForSerialPort(scanner);
		Emulator emulator = new Emulator(serial);
		while (!emulator.initialize())
		{
			System.err.println("Cannot communicate with the hardware, please reselect the prot!");
			serial = askForSerialPort(scanner);
		}
		System.out.println("Successfully Connected!");
		outLoop:
		while (true)
		{
			if (emulator.getMode() == EmulatorModes.STANDBY)
			{
				PrintHandler handler = (PrintHandler) emulator.getHandler();
				handler.stopPrint();
				System.out.println(
						"\nWhat do you want to do?\n1. (C)apture\n2. (CC)Change Channel\n3. (A)nswer\nOr input \"exit\" to exit.");
			}
			String input = scanner.next();
			switch (input)
			{
			case "C":
				startCapture(emulator, scanner);
				break;
			case "CC":
				changeChannel(emulator, scanner);
				break;
			case "s":
				emulator.stopAndGoStandby();
				break;
			case "exit":
				break outLoop;
			default:
				break;
			}
		}
		scanner.close();
	}

	public static SerialAdapter askForSerialPort(Scanner scanner) throws InterruptedException
	{
		SerialAdapter serial = new SerialAdapter();
		System.out.println("Here are the available port:");
		String[] portNames = serial.getAllPortsByNames();
		for (int i = 0; i < portNames.length; i++)
		{
			System.out.println(i + " " + portNames[i]);
		}
		System.out.println("Please make a choice (input the index number):");
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
		Thread.sleep(SERIAL_OPEN_WAIT_TIME);
		return serial;
	}

	private static void startCapture(Emulator emulator, Scanner scanner)
	{
		if (emulator.getMode() != EmulatorModes.STANDBY)
			return;
		System.out.println("Do you want to see the raw capture data(Y/N)?");
		String input = scanner.next();
		boolean showRaw;
		switch (input)
		{
		case "Y":
		case "y":
			showRaw = true;
			break;
		default:
			showRaw = false;
			break;
		}
		if (emulator.startCapture(new AnswerPacketHashMap(), showRaw, true))
			System.out.println("Start capturing, if you want to stop, input 's'.");
	}

	private static void changeChannel(Emulator emulator, Scanner scanner)
	{
		if (emulator.getMode() != EmulatorModes.STANDBY)
			return;
		System.out.println("Please input your target channel:");
		String input = scanner.next();
		try
		{
			emulator.changeChannel(IClickerChannel.valueOf(input));
		} catch (Exception e)
		{
			System.out.println("Illegal input!");
			return;
		}
		if (emulator.getEmulatorChannel() == IClickerChannel.valueOf(input))
			System.out.println("Successfully change to " + input + ".");
		else
			System.out.println("Fail.");
	}

}
