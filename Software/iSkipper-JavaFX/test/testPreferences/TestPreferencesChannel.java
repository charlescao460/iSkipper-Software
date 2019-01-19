package testPreferences;

import java.util.Scanner;

import com.csr460.iSkipper.support.IClickerChannel;

import application.utils.preference.UserPreferences;

public class TestPreferencesChannel
{

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Current Channel: " + UserPreferences.getChannel().toString());
		System.out.println("Input new Channel: ");
		String strCh = scanner.next();
		UserPreferences.setChannel(IClickerChannel.valueOf(strCh));
		scanner.close();
	}

}
