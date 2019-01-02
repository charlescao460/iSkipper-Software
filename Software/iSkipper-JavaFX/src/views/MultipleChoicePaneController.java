/**
 * 
 */
package views;

import java.util.ArrayList;

import com.jfoenix.effects.JFXDepthManager;

import emulator.Emulator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import support.Answer;
import support.AnswerPacketHashMap;
import support.IClickerChannel;

/**
 * @author CSR
 *
 */
public final class MultipleChoicePaneController
{
	@FXML
	private AnchorPane rootPane;

	/************* Status Pane *****************/
	private StatusPaneController statusPaneController;

	@FXML
	private AnchorPane statusPane;

	@FXML
	private Label statusLabel;

	@FXML
	private Label idLabel;

	@FXML
	private Label channelLabel;

	/************* Data Pane *****************/
	private DataPaneController dataPaneController;

	@FXML
	private AnchorPane dataPane;

	@FXML
	private Label idCountLabel;

	@FXML
	private Label resCountLabel;

	@FXML
	private Label ansALabel;

	@FXML
	private Label ansBLabel;

	@FXML
	private Label ansCLabel;

	@FXML
	private Label ansDLabel;

	@FXML
	private Label ansELabel;

	private Emulator emulator;

	/************* Functions *****************/
	@FXML
	private void initialize()
	{
		drawDepthShadow();
		statusPaneController = new StatusPaneController();
		dataPaneController = new DataPaneController();

	}

	/**
	 * @return the emulator
	 */
	public Emulator getEmulator()
	{
		return emulator;
	}

	/**
	 * @param emulator
	 *            the emulator to set
	 */
	public void setEmulator(Emulator emulator)
	{
		this.emulator = emulator;
	}

	/**
	 * @return the statusPaneController
	 */
	public StatusPaneController getStatusPaneController()
	{
		return statusPaneController;
	}

	/**
	 * @return the dataPaneController
	 */
	public DataPaneController getDataPaneController()
	{
		return dataPaneController;
	}

	private void drawDepthShadow()
	{
		JFXDepthManager.pop(statusPane);
		JFXDepthManager.pop(dataPane);
	}

	public class StatusPaneController
	{
		public StatusPaneController()
		{
			if (emulator != null)
			{
				setReady();
				idLabel.setText(emulator.getEmulatorID().toString());
				channelLabel.setText(emulator.getEmulatorChannel().name());
			}
		}

		public void setReady()
		{
			statusLabel.setText("Ready...");
		}

		public void setCapturing()
		{
			statusLabel.setText("Capturing");
		}

		public void setSending()
		{
			statusLabel.setText("Sending");
		}

		public void setChannel(IClickerChannel channel)
		{
			channelLabel.setText(channel.name());
		}
	}

	public class DataPaneController
	{
		private static final int FONT_SIZE_1 = 22;
		private static final int FONT_SIZE_2 = 25;
		private static final int FONT_SIZE_3 = 28;
		private static final int FONT_SIZE_4 = 31;
		private static final int FONT_SIZE_5 = 34;
		private final int FONT_SIZES[] =
		{ FONT_SIZE_1, FONT_SIZE_2, FONT_SIZE_3, FONT_SIZE_4, FONT_SIZE_5 };

		public DataPaneController()
		{
			initialize();
		}

		public void initialize()
		{
			idCountLabel.setText("0");
			resCountLabel.setText("0");
			ansALabel.setText("0");
			ansBLabel.setText("0");
			ansCLabel.setText("0");
			ansDLabel.setText("0");
			ansELabel.setText("0");
			ansALabel.getFont();
			// Set sizes
			ansALabel.setFont(Font.font(FONT_SIZE_1));
			ansBLabel.setFont(Font.font(FONT_SIZE_3));
			ansCLabel.setFont(Font.font(FONT_SIZE_5));
			ansDLabel.setFont(Font.font(FONT_SIZE_3));
			ansELabel.setFont(Font.font(FONT_SIZE_1));
		}

		public void update(AnswerPacketHashMap hashMap)
		{
			// Change texts
			idCountLabel.setText(String.valueOf(hashMap.getNumsTotalIDs()));
			resCountLabel.setText(String.valueOf(hashMap.getNumsTotalPacketRecieved()));
			ansALabel.setText(String.valueOf(hashMap.getAnswerCount(Answer.A)));
			ansBLabel.setText(String.valueOf(hashMap.getAnswerCount(Answer.B)));
			ansCLabel.setText(String.valueOf(hashMap.getAnswerCount(Answer.C)));
			ansDLabel.setText(String.valueOf(hashMap.getAnswerCount(Answer.D)));
			ansELabel.setText(String.valueOf(hashMap.getAnswerCount(Answer.E)));
			// Set fonts according to their numbers
			ArrayList<Label> labels = new ArrayList<Label>(5);
			labels.add(ansALabel);
			labels.add(ansBLabel);
			labels.add(ansCLabel);
			labels.add(ansDLabel);
			labels.add(ansELabel);
			labels.sort((arg0, arg1) -> Integer.valueOf(arg0.getText()) - Integer.valueOf(arg1.getText()));
			for (int i = 0; i < labels.size(); i++)
			{
				labels.get(i).setFont(Font.font(FONT_SIZES[i]));
			}
		}

	}
}
