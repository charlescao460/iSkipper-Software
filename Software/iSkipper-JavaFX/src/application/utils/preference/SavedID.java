package application.utils.preference;

import java.util.ArrayList;

import support.IClickerID;

/**
 * The class to storage ID related information on the GUI.
 * 
 * @author CSR
 *
 */
public class SavedID
{
	private IClickerID id;
	private String name;
	private String note;

	/**
	 * @param id
	 * @param name
	 * @param note
	 */
	public SavedID(IClickerID id, String name, String note)
	{
		if (id == null)
			throw new NullPointerException("ID Cannot be Null!");
		this.id = id;
		this.name = name;
		this.note = note;
	}

	/**
	 * @return the id
	 */
	public IClickerID getId()
	{
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(IClickerID id)
	{
		if (id == null)
			throw new NullPointerException("ID Cannot be Null!");
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the note
	 */
	public String getNote()
	{
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note)
	{
		this.note = note;
	}

	/**
	 * The ArrayList to storage saved IDs.
	 * 
	 * @author CSR
	 *
	 */
	public static class SavedIDList extends ArrayList<SavedID>
	{
		private static final long serialVersionUID = -1764507006360279163L;

		/**
		 * Return the saved IDs in CSV string..
		 * 
		 * @see java.util.AbstractCollection#toString()
		 */
		@Override
		public String toString()
		{
			StringBuilder builder = new StringBuilder();
			for (SavedID id : this)
			{
				builder.append(id.getId().toString());
				builder.append(',');
				builder.append(id.getName());
				builder.append(',');
				builder.append(id.getNote());
				builder.append(',');
				builder.append('\n');
			}
			return builder.toString();
		}

		/**
		 * @return The length of the CSV string.
		 */
		public long csvLength()
		{
			long ret = 0;
			for (SavedID id : this)
			{
				ret += IClickerID.ID_HEX_STRING_LENGTH; // Hexadecimal id length
				if (id.getName() != null)
					ret += id.getName().length();
				else
					ret += 4; // "null"
				if (id.getNote() != null)
					ret += id.getNote().length();
				else
					ret += 4;
				ret += 4; // Commas and '\n'
			}
			return ret;
		}

		/**
		 * Construct a list from CSV string.
		 * 
		 * @param csv
		 *            The CSV string containing the saved IDs
		 * @throws @{@link
		 * 			IndexOutOfBoundsException} If incorrect format.
		 * 
		 * @throws @{@link
		 * 			NullPointerException} If there is an invalid ID.
		 */
		SavedIDList(String csv)
		{
			String strIDs[] = csv.split("\n");
			for (String strID : strIDs)
			{
				String info[] = strID.split(",");
				IClickerID id = IClickerID.idFromString(info[0]);
				if (id == null)
					throw new NullPointerException();
				String name = info[1].equals("null") ? null : info[1];
				String note = info[2].equals("null") ? null : info[2];
				this.add(new SavedID(id, name, note));
			}
		}

		/**
		 * Default constructor.
		 */
		public SavedIDList()
		{
			super();
		}

	}

}
