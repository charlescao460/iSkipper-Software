package testPreferences;

import java.util.Random;

import com.csr460.iSkipper.support.IClickerID;

import application.utils.preference.SavedID;
import application.utils.preference.SavedID.SavedIDList;
import application.utils.preference.UserPreferences;

public class TestPreferencesList
{

	public static void main(String[] args)
	{
		Random random = new Random();
		System.out.println("Previous List:\n" + UserPreferences.getSavedIDList().toString());
		SavedIDList list = new SavedIDList();
		list.add(new SavedID(IClickerID.getRandomID(), null, null));
		list.add(new SavedID(IClickerID.getRandomID(), String.valueOf(random.nextInt()), null));
		list.add(new SavedID(IClickerID.getRandomID(), String.valueOf(random.nextInt()),
				String.valueOf(random.nextInt())));
		list.add(new SavedID(IClickerID.getRandomID(), null, String.valueOf(random.nextInt())));
		UserPreferences.setSavedIDList(list);
		System.out.println("New List:\n" + UserPreferences.getSavedIDList().toString());
	}

}
