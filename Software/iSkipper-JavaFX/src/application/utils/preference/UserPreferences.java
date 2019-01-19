/**
 * 
 */
package application.utils.preference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.csr460.iSkipper.support.IClickerChannel;

import application.utils.preference.SavedID.SavedIDList;

/**
 * The Class to storage user's settings and preferences. (E.g. Channel, IDs)
 * 
 * @author CSR
 *
 */
public final class UserPreferences
{
	private static Preferences preferences;
	private static IClickerChannel channel;
	private final static String KEY_CHANNEL = "KEY_CHANNEL";
	private static String listCSVLocation;
	private final static String PREFERENCE_FOLDER_NAME = "iSkipper";
	private final static String CSV_FILE_NAME = "iSkipperIDs.csv";
	private static SavedID.SavedIDList savedIDList;

	static
	{
		readPreferences();
	}

	private static void readPreferences()
	{
		preferences = Preferences.userNodeForPackage(UserPreferences.class);
		channel = readChannel();
		listCSVLocation = readCSVFilePath();
		savedIDList = readList();
	}

	private static SavedIDList readList()
	{
		File csvFile = new File(listCSVLocation);
		if (csvFile.length() > 0)
		{
			try
			{
				String strCSV = new String(Files.readAllBytes(csvFile.toPath()));
				return new SavedID.SavedIDList(strCSV);

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return new SavedID.SavedIDList();
	}

	private static String readCSVFilePath()
	{
		String strUserHome = System.getProperty("user.home");
		File folder = new File(strUserHome, PREFERENCE_FOLDER_NAME);
		if (!folder.exists())
		{// Then we create it
			folder.mkdir();
		}
		File csvFile = new File(folder, CSV_FILE_NAME);
		if (!csvFile.exists())
		{
			try
			{
				csvFile.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return csvFile.toString();
	}

	private static IClickerChannel readChannel()
	{
		try
		{
			// Read channel
			String strChannel = preferences.get(KEY_CHANNEL, null);
			if (strChannel == null)
			{
				// If no previous record
				preferences.put(KEY_CHANNEL, IClickerChannel.AA.toString());
				return IClickerChannel.AA;
			} else
			{
				return IClickerChannel.valueOf(strChannel);
			}

		} catch (Exception e)
		{
			// Then the record is corrupted。
			e.printStackTrace();
			try
			{
				preferences.clear();// So, we trash it.
				setAllToDefault();
			} catch (BackingStoreException e1)
			{
				e1.printStackTrace();
			}
		}
		return IClickerChannel.AA;
	}

	private static void setAllToDefault()
	{
		String strUserHome = System.getenv("user.home");
		File folder = new File(strUserHome, PREFERENCE_FOLDER_NAME);
		folder.mkdir();
		File csvFile = new File(folder, CSV_FILE_NAME);
		csvFile.delete();
		try
		{
			csvFile.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the savedIDList
	 */
	public static SavedID.SavedIDList getSavedIDList()
	{
		return savedIDList;
	}

	/**
	 * @param savedIDList
	 *            the savedIDList to set
	 */
	public static void setSavedIDList(SavedID.SavedIDList savedIDList)
	{
		if (savedIDList == null)
			throw new NullPointerException("savedIDList cannot set to be null!");
		UserPreferences.savedIDList = savedIDList;

		try
		{
			FileWriter writer = new FileWriter(listCSVLocation, false);
			writer.write(UserPreferences.savedIDList.toString());// Overwrite
			writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * @return the channel
	 */
	public static IClickerChannel getChannel()
	{
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public static void setChannel(IClickerChannel channel)
	{
		if (channel == null)
			throw new NullPointerException("Channel cannot set to be null!");
		UserPreferences.channel = channel;
		preferences.put(KEY_CHANNEL, UserPreferences.channel.toString());
	}

}
