/**
 * 
 */
package views;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.effects.JFXDepthManager;

import device.ReceivedPacketEvent;
import emulator.Emulator;
import emulator.EmulatorModes;
import handler.CaptureHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
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

	/************* Area Chart *****************/
	@FXML
	private AnchorPane areaChartPane;

	private AreaChart<Number, Number> areaChart;

	private AreaChartController areaChartController;

	/************* Tab Pane *****************/
	@FXML
	private AnchorPane tabAnchorPane;

	@FXML
	private JFXTabPane tabPane;

	private TabPaneController tabPaneController;

	/************* Functions *****************/
	@FXML
	private void initialize()
	{
		drawDepthShadow();
		statusPaneController = new StatusPaneController();
		dataPaneController = new DataPaneController();
		toolbarEventsHandler = new MultipleChoiceToolbarEventHandler();
		capturingHandler = new GUICapturingHandler(new AnswerPacketHashMap());
		areaChartController = new AreaChartController();
		tabPaneController = new TabPaneController();
	}

	private void drawDepthShadow()
	{
		JFXDepthManager.pop(statusPane);
		JFXDepthManager.pop(dataPane);
		JFXDepthManager.pop(areaChartPane);
		JFXDepthManager.pop(tabAnchorPane);
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
				if (i >= 1 && labels.get(i).getText().equals(labels.get(i - 1).getText()))
					labels.get(i).setFont(Font.font(FONT_SIZES[i - 1]));
			}
		}
	}

	/**
	 * 
	 * The class to control Area Chart.
	 * 
	 * @author CSR
	 *
	 */
	public class AreaChartController
	{
		private XYChart.Series<Number, Number> seriesA;
		private XYChart.Series<Number, Number> seriesB;
		private XYChart.Series<Number, Number> seriesC;
		private XYChart.Series<Number, Number> seriesD;
		private XYChart.Series<Number, Number> seriesE;
		private NumberAxis xAxis;
		private NumberAxis yAxis;

		public AreaChartController()
		{
			creatChart();
			seriesA = new Series<>();
			seriesB = new Series<>();
			seriesC = new Series<>();
			seriesD = new Series<>();
			seriesE = new Series<>();
			seriesA.setName("A");
			seriesB.setName("B");
			seriesC.setName("C");
			seriesD.setName("D");
			seriesE.setName("E");
			areaChart.getData().add(seriesA);
			areaChart.getData().add(seriesB);
			areaChart.getData().add(seriesC);
			areaChart.getData().add(seriesD);
			areaChart.getData().add(seriesE);

		}

		private void creatChart()
		{
			xAxis = new NumberAxis();
			yAxis = new NumberAxis();
			areaChart = new AreaChart<>(xAxis, yAxis)
			{
				@Override
				protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item)
				{
					// Override it so no point mark.
				}
			};
			areaChartPane.getChildren().add(areaChart);
			AnchorPane.setBottomAnchor(areaChart, 0.0);
			AnchorPane.setTopAnchor(areaChart, 0.0);
			AnchorPane.setLeftAnchor(areaChart, 0.0);
			AnchorPane.setRightAnchor(areaChart, 0.0);
		}

		/**
		 * Clear the chart
		 */
		public void clear()
		{
			seriesA.getData().clear();
			seriesB.getData().clear();
			seriesC.getData().clear();
			seriesD.getData().clear();
			seriesE.getData().clear();
		}

		/**
		 * Update the area chart.
		 * 
		 * @param stats
		 *            the hashMap stats contains answers
		 */
		public void update(AnswerStats stats)
		{
			int resCount = stats.getNumsTotalPacketRecieved();
			seriesA.getData().add(new XYChart.Data<Number, Number>(resCount, stats.getNumsA()));
			seriesB.getData().add(new XYChart.Data<Number, Number>(resCount, stats.getNumsB()));
			seriesC.getData().add(new XYChart.Data<Number, Number>(resCount, stats.getNumsC()));
			seriesD.getData().add(new XYChart.Data<Number, Number>(resCount, stats.getNumsD()));
			seriesE.getData().add(new XYChart.Data<Number, Number>(resCount, stats.getNumsE()));
		}

	}

	public class TabPaneController
	{
		private BarChart<String, Number> barChart;
		private CategoryAxis barChartXAxis;
		private NumberAxis barChartYAxis;
		private Series<String, Number> seriesA;
		private Series<String, Number> seriesB;
		private Series<String, Number> seriesC;
		private Series<String, Number> seriesD;
		private Series<String, Number> seriesE;
		private PieChart pieChart;
		private static final String LETTER_A = "A";
		private static final String LETTER_B = "B";
		private static final String LETTER_C = "C";
		private static final String LETTER_D = "D";
		private static final String LETTER_E = "E";

		@SuppressWarnings("unchecked")
		public TabPaneController()
		{
			// Create component
			Tab barChartTab = new Tab("Bar Chart");
			Tab pieChartTab = new Tab("Pie Chart");
			// Bar Chart
			barChartXAxis = new CategoryAxis();
			barChartYAxis = new NumberAxis();
			barChart = new BarChart<String, Number>(barChartXAxis, barChartYAxis);
			barChart.setCategoryGap(0.0);
			barChart.setBarGap(0.0);

			// Pie Chart
			pieChart = new PieChart();
			barChartTab.setContent(barChart);
			pieChartTab.setContent(pieChart);
			tabPane.getTabs().addAll(barChartTab, pieChartTab);
			// Create data object
			seriesA = new Series<>();
			seriesB = new Series<>();
			seriesC = new Series<>();
			seriesD = new Series<>();
			seriesE = new Series<>();
			seriesA.setName(LETTER_A);
			seriesB.setName(LETTER_B);
			seriesC.setName(LETTER_C);
			seriesD.setName(LETTER_D);
			seriesE.setName(LETTER_E);
			seriesA.getData().add(new XYChart.Data<>(LETTER_A, 0));
			seriesB.getData().add(new XYChart.Data<>(LETTER_B, 0));
			seriesC.getData().add(new XYChart.Data<>(LETTER_C, 0));
			seriesD.getData().add(new XYChart.Data<>(LETTER_D, 0));
			seriesE.getData().add(new XYChart.Data<>(LETTER_E, 0));
			// Add to charts
			barChart.getData().addAll(seriesA, seriesB, seriesC, seriesD, seriesE);
			pieChart.getData().addAll(new PieChart.Data(LETTER_A, 0), new PieChart.Data(LETTER_B, 0),
					new PieChart.Data(LETTER_C, 0), new PieChart.Data(LETTER_D, 0), new PieChart.Data(LETTER_E, 0));

		}

		public void clear()
		{
			seriesA.getData().get(0).setYValue(0);
			seriesB.getData().get(0).setYValue(0);
			seriesC.getData().get(0).setYValue(0);
			seriesD.getData().get(0).setYValue(0);
			seriesE.getData().get(0).setYValue(0);
			for (PieChart.Data data : pieChart.getData())
			{
				switch (data.getName())
				{
				case LETTER_A:
					data.setPieValue(0.0);
					break;
				case LETTER_B:
					data.setPieValue(0.0);
					break;
				case LETTER_C:
					data.setPieValue(0.0);
					break;
				case LETTER_D:
					data.setPieValue(0.0);
					break;
				case LETTER_E:
					data.setPieValue(0.0);
					break;
				}
			}
		}

		public void update(AnswerStats stats)
		{
			int numsA = stats.getNumsA();
			int numsB = stats.getNumsB();
			int numsC = stats.getNumsC();
			int numsD = stats.getNumsD();
			int numsE = stats.getNumsE();
			seriesA.getData().get(0).setYValue(numsA);
			seriesB.getData().get(0).setYValue(numsB);
			seriesC.getData().get(0).setYValue(numsC);
			seriesD.getData().get(0).setYValue(numsD);
			seriesE.getData().get(0).setYValue(numsE);
			for (PieChart.Data data : pieChart.getData())
			{
				switch (data.getName())
				{
				case LETTER_A:
					data.setPieValue(numsA);
					break;
				case LETTER_B:
					data.setPieValue(numsB);
					break;
				case LETTER_C:
					data.setPieValue(numsC);
					break;
				case LETTER_D:
					data.setPieValue(numsD);
					break;
				case LETTER_E:
					data.setPieValue(numsE);
					break;
				}
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
				AnswerStats stats = hashMap.getAnswerStats();
				// Add GUI changes here.
				dataPaneController.update(stats);
				areaChartController.update(stats);
				tabPaneController.update(stats);
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
				AnswerStats stats = capturingHandler.getHashMap().getAnswerStats();
				// Update
				dataPaneController.update(stats);
				areaChartController.update(stats);
				(new Thread(() ->
				{
					emulator.startCapture(capturingHandler);
					Platform.runLater(() ->
					{
						primaryViewController.hideProgressBar();
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
			if (emulator != null)
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

			if (emulator != null && emulator.getMode() != EmulatorModes.STANDBY)
			{
				primaryViewController.showProgressBar();
				(new Thread(() ->
				{
					emulator.stopAndGoStandby();
					Platform.runLater(() ->
					{
						primaryViewController.hideProgressBar();
						capturingHandler.getHashMap().clear();
						areaChartController.clear();
						tabPaneController.clear();
						statusPaneController.setReady();
						primaryViewController.getStartToggleNode().setSelected(false);
					});
					System.gc();
				})).start();
			}

		}

		@Override
		public void OnActionButtonReset(ActionEvent e, JFXButton resetButton)
		{
			capturingHandler.getHashMap().clear();
			AnswerStats stats = capturingHandler.getHashMap().getAnswerStats();
			Platform.runLater(() ->
			{
				dataPaneController.initialize();
				areaChartController.clear();
				areaChartController.update(stats);
				tabPaneController.clear();
				tabPaneController.update(stats);
			});
			System.gc();
		}

	}

}
