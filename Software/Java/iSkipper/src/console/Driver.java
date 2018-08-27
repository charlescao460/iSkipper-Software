package console;

import java.util.Scanner;

import device.Serial;
import support.IClickerID;
import support.Transcoding;

public class Driver
{
	public static void main(String[] args) throws InterruptedException
	{
		Serial serial = new Serial();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Here are the available port:");
		String[] portNames = serial.getAvailablePortsByNames();
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
		/*
		 * while (true) { String command = scanner.nextLine(); if
		 * (command.contains("stop")) System.exit(0);
		 * serial.writeBytes(ASCII.stringToBytes(command)); }
		 */
		serial.writeBytes(Transcoding.stringToBytes("I"));
		Thread.sleep(100);
		serial.writeBytes(Transcoding.stringToBytes("S"));
		Thread.sleep(100);
		byte[] answers =
		{ 'A', 'B', 'C', 'D', 'E' };
		for (int i = 0; true; i = (i + 1) % 0x00_FF_FF_FF)
		{

			byte[] arr = Transcoding.intToByteArray(i << 8);
			arr[3] = (byte) ((byte) arr[0] ^ (byte) arr[1] ^ (byte) arr[2]);
			if (!IClickerID.isValidID(arr))
				continue;
			// byte[] arr = AnswerPacket.intToByteArray(0xCD_CD_CD_CD);
			String toSend = String.format("%c,%02X%02X%02X%02X", answers[i % 5], arr[0], arr[1], arr[2], arr[3]);
			// System.out.println(toSend);
			serial.writeBytes(Transcoding.stringToBytes(toSend));
			Thread.sleep(50);
			if (i % 10000 == 0)
			{
				System.gc();
				System.out.println(toSend);
			} else if (i % 20 == 0)
			{
				System.out.println();
			}
		}

	}
}
