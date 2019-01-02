/**
 * 
 */
package views;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.effects.JFXDepthManager;

import device.ReceivedPacketEvent;
import emulator.Emulator;
import emulator.EmulatorModes;
import handler.CaptureHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import support.AnswerPacketHashMap;
import support.AnswerPacketHashMap.AnswerStats;
import support.IClickerChannel;
import views.PrimaryViewController.AbstractPrimaryViewToolbarEventsHandler;

/**
 * @author CSR
 *
 */
public final class MultipleChoicePaneController
{
	@FXML
	private AnchorPane rootPane;

	private PrimaryViewController primaryViewController;

	private AbstractPrimaryViewToolbarEventsHandler toolbarEventsHandler;

	private GUICapturingHandler capturingHandler;

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
		toolbarEventsHandler = new MultipleChoiceToolbarEventHandler();
		capturingHandler = new GUICapturingHandler(new AnswerPacketHashMap());

	}

	private void drawDepthShadow()
	{
		JFXDepthManager.pop(statusPane);
		JFXDepthManager.pop(dataPane);
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

	/**
	 * @param primaryViewController
	 *            the primaryViewController to set
	 */
	public void setPrimaryViewController(PrimaryViewController primaryViewController)
	{
		this.primaryViewController = primaryViewController;
	}

	/**
	 * @return the toolbarEventsHandler
	 */
	public AbstractPrimaryViewToolbarEventsHandler getToolbarEventsHandler()
	{
		return toolbarEventsHandler;
	}

	/**
	 * 
	 * The controller of status pane
	 * 
	 * @author CSR
	 *
	 */
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

		/**
		 * Set the status string to ready
		 */
		public void setReady()
		{
			statusLabel.setText("Ready...");
		}

		/**
		 * Set the status string to capturing
		 */
		public void setCapturing()
		{
			statusLabel.setText("Capturing");
		}

		/**
		 * Set the status string to sending
		 */
		public void setSending()
		{
			statusLabel.setText("Sending");
		}

		/**
		 * Set the status string to paused
		 * 
		 */
		public void setPaused()
		{
			statusLabel.setText("Paused");
		}

		/**
		 * @param channel
		 *            The iCkicker Channel to show
		 */
		public void setChannel(IClickerChannel channel)
		{
			channelLabel.setText(channel.name());
		}
	}

	/**
	 * The controller of data pane
	 * 
	 * @author CSR
	 *
	 */
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

		/**
		 * Initialize the data
		 */
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

		/**
		 * Update displayed data
		 * 
		 * @param stats
		 *            the hashMap stats contains answers
		 */
		public void update(AnswerStats stats)
		{
			if (stats.getNumsTotalPacketRecieved() == 0)
			{
				initialize();
				return;
			}
			// Change texts
			idCountLabel.setText(String.valueOf(stats.getIDCount()));
			resCountLabel.setText(String.valueOf(stats.getNumsTotalPacketRecieved()));
			ansALabel.setText(String.valueOf(stats.getNumsA()));
			ansBLabel.setText(String.valueOf(stats.getNumsB()));
			ansCLabel.setText(String.valueOf(stats.getNumsC()));
			ansDLabel.setText(String.valueOf(stats.getNumsD()));
			ansELabel.setText(String.valueOf(stats.getNumsE()));
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

	private class GUICapturingHandler extends CaptureHandler
	{
		public GUICapturingHandler(AnswerPacketHashMap hashMap)
		{
			super(hashMap, true/* Print Raw */, false);
		}

		@Override
		public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent)
		{
			super.onReceivedPacketEvent(packetEvent);
			Platform.runLater(() ->
			{
				// TODO: Add GUI changes here.
				dataPaneController.update(hashMap.getAnswerStats());
			});
		}
	}

	private class MultipleChoiceToolbarEventHandler
			extends PrimaryViewController.AbstractPrimaryViewToolbarEventsHandler
	{

		@Override
		public void OnToggleStart(ActionEvent e, JFXToggleNode startToggle)
		{
			if (emulator != null && emulator.getMode() == EmulatorModes.STANDBY)
			{
				primaryViewController.showProgressBar();
				(new Thread(() ->
				{
					emulator.startCapture(capturingHandler);
					Platform.runLater(() ->
					{
						primaryViewController.hideProgressBar();
						dataPaneController.update(capturingHandler.getHashMap().getAnswerStats());
						statusPaneController.setCapturing();
					});

				})).start();

			} else
			{
				startToggle.setSelected(false);
			}
		}

		@Override
		public void OnUntoggleStart(ActionEvent e, JFXToggleNode startToggle)
		{
			if (emulator != null && emulator.getMode() != EmulatorModes.STANDBY)
			{
				primaryViewController.showProgressBar();
				(new Thread(() ->
				{
					emulator.stopAndGoStandby();
					Platform.runLater(() ->
					{
						primaryViewController.hideProgressBar();
						statusPaneController.setPaused();
					});
				})).start();
			}

		}

		@Override
		public void OnActionButtonStop(ActionEvent e, JFXButton stopButton)
		{
			capturingHandler.getHashMap().clear();
			statusPaneController.setReady();
			primaryViewController.getStartToggleNode().setSelected(false);
			if (emulator != null && emulator.getMode() != EmulatorModes.STANDBY)
			{
				primaryViewController.showProgressBar();
				(new Thread(() ->
				{
					emulator.stopAndGoStandby();;
					Platform.runLater(() ->
					{
						primaryViewController.hideProgressBar();

					});
				})).start();
			}

		}

		@Override
		public void OnActionButtonReset(ActionEvent e, JFXButton resetButton)
		{
			capturingHandler.getHashMap().clear();
			dataPaneController.initialize();
		}

	}
}
