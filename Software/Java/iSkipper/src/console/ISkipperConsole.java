package console;

import java.util.Scanner;

import device.SerialAdapter;
import emulator.Emulator;
import emulator.EmulatorModes;
import handler.AttackHandler;
import handler.PrintHandler;
import support.Answer;
import support.AnswerPacketHashMap;
import support.IClickerChannel;

public class ISkipperConsole
{
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
						"\nWhat do you want to do?\n1. (C)apture\n2. (CC)Change Channel\n3. (A)nswer\n4. (AT)tack\nOr input \"exit\" to exit.");
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
			case "A":
				answerTheQuestion(emulator, scanner);
				break;
			case "AT":
				startAttack(emulator, scanner);
				break;
			case "exit":
				break outLoop;
			default:
				break;
			}
		}
		scanner.close();
	}

	public static void startAttack(Emulator emulator, Scanner scanner)
	{
		System.out.println("Which answer do you want to attack?(A,B,C,D,E,or R for random.)");
		String strAnswer = scanner.next();
		Answer answer = Answer.P;
		try
		{
			answer = Answer.charAnswer(strAnswer.charAt(0));
		} catch (Exception e)
		{
		}
		System.out.println("How many times would you like to submit?");
		long count = scanner.nextLong();
		emulator.startAttack(answer == Answer.P ? null : answer, count, new AttackHandler(true));
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

	private static void answerTheQuestion(Emulator emulator, Scanner scanner)
	{
		if (emulator.getMode() != EmulatorModes.STANDBY)
			return;
		System.out.println("Please key in your answer:");
		String input = scanner.next();
		try
		{
			emulator.submitAnswer(Answer.valueOf(input));
		} catch (Exception e)
		{
			System.out.println("Illegal input!");
			return;
		}
		System.out.println("Submit done!");

	}

}
