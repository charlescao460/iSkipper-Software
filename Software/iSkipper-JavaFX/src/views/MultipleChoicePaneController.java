/**
 * 
 */
package views;

import java.util.ArrayList;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.effects.JFXDepthManager;

import application.utils.preference.SavedID;
import application.utils.preference.SavedID.SavedIDList;
import application.utils.preference.UserPreferences;
import device.ReceivedPacketEvent;
import emulator.Emulator;
import emulator.EmulatorModes;
import handler.CaptureHandler;
import handler.PrintHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import support.Answer;
import support.AnswerPacketHashMap;
import support.AnswerPacketHashMap.AnswerStats;
import support.IClickerChannel;
import support.IClickerID;
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

	/************* Sending Pane *****************/
	@FXML
	private AnchorPane sendingPane;

	@FXML
	private JFXTreeTableView<SendingPaneController.IDTreeObject> treeTableView;

	@FXML
	private JFXButton sendingButton;

	@FXML
	private JFXButton stopSendingButton;

	@FXML
	private JFXToggleButton autoSelectToggle;

	@FXML
	private JFXToggleButton sendListToggle;

	@FXML
	private JFXSlider sendingSlider;

	@FXML
	private JFXRadioButton sendingRadioA;

	@FXML
	private JFXRadioButton sendingRadioB;

	@FXML
	private JFXRadioButton sendingRadioC;

	@FXML
	private JFXRadioButton sendingRadioD;

	@FXML
	private JFXRadioButton sendingRadioE;

	@FXML
	private JFXProgressBar sendingProgressBar;

	@FXML
	private Label sendingProgressLabel;

	private ToggleGroup sendingToggleGroup;

	private SendingPaneController sendingPaneController;

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
		sendingPaneController = new SendingPaneController();
	}

	private void drawDepthShadow()
	{
		JFXDepthManager.pop(statusPane);
		JFXDepthManager.pop(dataPane);
		JFXDepthManager.pop(areaChartPane);
		JFXDepthManager.pop(tabAnchorPane);
		JFXDepthManager.pop(sendingPane);
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
		private Series<String, Number> series;
		private PieChart pieChart;
		private static final String LETTER_A = "A";
		private static final String LETTER_B = "B";
		private static final String LETTER_C = "C";
		private static final String LETTER_D = "D";
		private static final String LETTER_E = "E";

		public TabPaneController()
		{
			// Create component
			Tab barChartTab = new Tab("Bar Chart");
			Tab pieChartTab = new Tab("Pie Chart");
			// Bar Chart
			barChartXAxis = new CategoryAxis();
			barChartXAxis.setAnimated(false);
			barChartYAxis = new NumberAxis();
			barChart = new BarChart<String, Number>(barChartXAxis, barChartYAxis)
			{
				// Add value texts on the bar
				@Override
				protected void layoutPlotChildren()
				{
					super.layoutPlotChildren();
					for (Series<String, Number> series : getData())
					{
						for (Data<String, Number> data : series.getData())
						{
							StackPane bar = (StackPane) data.getNode();
							Text text = new Text(String.valueOf(data.getYValue()));
							text.setFill(Color.WHITE);
							if (bar.getChildren().isEmpty())
							{
								VBox vBox = new VBox();
								vBox.setAlignment(Pos.TOP_CENTER);
								bar.getChildren().add(vBox);
							}
							VBox vBox = (VBox) bar.getChildren().get(0);
							vBox.getChildren().clear();
							vBox.getChildren().add(text);
						}
					}
				}
			};
			barChart.setLegendVisible(false);
			// Pie Chart
			pieChart = new PieChart()
			{
				@Override
				protected void layoutChartChildren(double top, double left, double contentWidth, double contentHeight)
				{
					super.layoutChartChildren(top, left, contentWidth, contentHeight);
					if (getLabelsVisible())
					{
						double sum = getData().stream().mapToDouble(d -> d.getPieValue()).sum();
						getData().forEach(d ->
						{
							this.lookupAll(".chart-pie-label").forEach(l ->
							{
								Text text = (Text) l;
								if (text.getText().contains((d.getName())))
								{
									text.setText(d.getName() + ", " + String.format("%.02f%%(%d)",
											d.getPieValue() / sum * 100.0, (int) d.getPieValue()));
								}
							});
						});
					}

				}
			};
			barChartTab.setContent(barChart);
			pieChartTab.setContent(pieChart);
			tabPane.getTabs().addAll(barChartTab, pieChartTab);
			// Create data object
			series = new Series<>();
			series.getData().add(new XYChart.Data<>(LETTER_A, 0));
			series.getData().add(new XYChart.Data<>(LETTER_B, 0));
			series.getData().add(new XYChart.Data<>(LETTER_C, 0));
			series.getData().add(new XYChart.Data<>(LETTER_D, 0));
			series.getData().add(new XYChart.Data<>(LETTER_E, 0));
			// Add to charts
			barChart.getData().add(series);
			pieChart.getData().addAll(new PieChart.Data(LETTER_A, 0), new PieChart.Data(LETTER_B, 0),
					new PieChart.Data(LETTER_C, 0), new PieChart.Data(LETTER_D, 0), new PieChart.Data(LETTER_E, 0));
			// set bar colors of bar chart
			barChart.lookup(".data0.chart-bar").setStyle("-fx-bar-fill:#f3622d");
			barChart.lookup(".data1.chart-bar").setStyle("-fx-bar-fill:#fba71b");
			barChart.lookup(".data2.chart-bar").setStyle("-fx-bar-fill:#57b757");
			barChart.lookup(".data3.chart-bar").setStyle("-fx-bar-fill:#41a9c9");
			barChart.lookup(".data4.chart-bar").setStyle("-fx-bar-fill:#4258c9");
		}

		public void clear()
		{
			for (XYChart.Data<String, Number> data : series.getData())
			{
				switch (data.getXValue())
				{
				case LETTER_A:
					data.setYValue(0);
					break;
				case LETTER_B:
					data.setYValue(0);
					break;
				case LETTER_C:
					data.setYValue(0);
					break;
				case LETTER_D:
					data.setYValue(0);
					break;
				case LETTER_E:
					data.setYValue(0);
					break;
				}
			}
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

			for (XYChart.Data<String, Number> data : series.getData())
			{
				switch (data.getXValue())
				{
				case LETTER_A:
					data.setYValue(numsA);
					break;
				case LETTER_B:
					data.setYValue(numsB);
					break;
				case LETTER_C:
					data.setYValue(numsC);
					break;
				case LETTER_D:
					data.setYValue(numsD);
					break;
				case LETTER_E:
					data.setYValue(numsE);
					break;
				}
			}
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

	public class SendingPaneController
	{
		private static final double FIXED_TREE_TABLE_CELL_SIZE = 30.0;
		private static final long SUBMIT_WAIT_TIME = 50;
		private JFXTreeTableColumn<IDTreeObject, String> nameColumn;
		private JFXTreeTableColumn<IDTreeObject, String> idColumn;
		private JFXTreeTableColumn<IDTreeObject, String> noteColumn;
		private ObservableList<IDTreeObject> idList;
		private boolean isCapturing;

		public SendingPaneController()
		{
			initializeRadios();
			initializeAutoSelectToggle();
			initializeSlider();
			initializeSendListToggle();
			initilaizeTreeTable();
			initializeSendButton();
			initializeProgress();
			initializeStopButton();

		}

		private void initializeRadios()
		{
			// Binding in group
			sendingToggleGroup = new ToggleGroup();
			sendingRadioA.setToggleGroup(sendingToggleGroup);
			sendingRadioB.setToggleGroup(sendingToggleGroup);
			sendingRadioC.setToggleGroup(sendingToggleGroup);
			sendingRadioD.setToggleGroup(sendingToggleGroup);
			sendingRadioE.setToggleGroup(sendingToggleGroup);
			sendingToggleGroup.selectToggle(sendingRadioA);
			// Set colors
			sendingRadioA.setStyle(
					"-fx-text-fill: #f3622d;" + "-jfx-selected-color: #f3622d;" + "-jfx-unselected-color: #f3622d;");
			sendingRadioB.setStyle(
					"-fx-text-fill: #fba71b;" + "-jfx-selected-color: #fba71b;" + "-jfx-unselected-color: #fba71b;");
			sendingRadioC.setStyle(
					"-fx-text-fill: #57b757;" + "-jfx-selected-color: #57b757;" + "-jfx-unselected-color: #57b757;");
			sendingRadioD.setStyle(
					"-fx-text-fill: #41a9c9;" + "-jfx-selected-color: #41a9c9;" + "-jfx-unselected-color: #41a9c9;");
			sendingRadioE.setStyle(
					"-fx-text-fill: #4258c9;" + "-jfx-selected-color: #4258c9;" + "-jfx-unselected-color: #4258c9;");
		}

		private void initializeAutoSelectToggle()
		{
			autoSelectToggle.setOnAction(e ->
			{
				if (autoSelectToggle.isSelected())
				{
					sendingToggleGroup.getToggles().forEach(t -> ((Node) t).setDisable(true));
				} else
				{
					sendingToggleGroup.getToggles().forEach(t -> ((Node) t).setDisable(false));
				}
			});
			autoSelectToggle.setSelected(true);
			autoSelectToggle.getOnAction().handle(null);// Disable all in default
		}

		private void initializeSlider()
		{
			sendingSlider.setValue(sendingSlider.getMax());
			sendingSlider.setDisable(true);
		}

		private void initializeSendListToggle()
		{
			sendListToggle.setOnAction(e ->
			{
				if (sendListToggle.isSelected())
				{
					sendingSlider.setValue(5);
					sendingSlider.setDisable(false);
				} else
				{
					initializeSlider();
				}
			});
		}

		@SuppressWarnings("unchecked")
		private void initilaizeTreeTable()
		{
			/************************ Name Column ***********************/
			nameColumn = new JFXTreeTableColumn<>("Name");
			nameColumn.setCellValueFactory(param -> param.getValue().getValue().name);
			nameColumn.setCellFactory(p -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
			nameColumn.setOnEditCommit(t ->
			{
				t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().name
						.set(t.getNewValue());
				saveTreeTable();
			});
			nameColumn.setSortable(false);

			/************************ ID Column ***********************/
			idColumn = new JFXTreeTableColumn<>("ID");
			idColumn.setCellValueFactory(param -> param.getValue().getValue().id);
			idColumn.setCellFactory(p -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
			idColumn.setOnEditCommit(t ->
			{
				if (t.getNewValue() == null || t.getNewValue().equals(""))// Delete
				{
					for (IDTreeObject id : idList)
					{
						if (id.id.getValue().equals(t.getOldValue()))
						{
							idList.remove(id);
							saveTreeTable();
							return;
						}
					}
				}
				if (IClickerID.idFromString(t.getNewValue()) == null)
				{
					JFXAlert<Void> alert = new JFXAlert<Void>((Stage) rootPane.getScene().getWindow());
					JFXDialogLayout layout = new JFXDialogLayout();
					layout.setBody(new Label(
							"The ID you entered is not valid, please try again.\n(Click anywhere outside this alert to close it.)"));
					alert.setTitle("Invalid ID");
					alert.setOverlayClose(true);
					alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
					alert.setContent(layout);
					alert.initModality(Modality.NONE);
					alert.show();
					treeTableView.refresh();
					return;
				}
				t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().id.set(t.getNewValue());
				saveTreeTable();
				if (idColumn.getCellData(t.getTreeTablePosition().getRow() + 1) == null)// if last row
				{
					idList.add(new IDTreeObject(null, null, null));
				}
			});
			idColumn.setSortable(false);

			/************************ Note Column ***********************/
			noteColumn = new JFXTreeTableColumn<>("Note");
			noteColumn.setCellValueFactory(param -> param.getValue().getValue().note);
			noteColumn.setCellFactory(p -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
			noteColumn.setOnEditCommit(t ->
			{
				t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().note
						.set(t.getNewValue());
				saveTreeTable();
			});

			noteColumn.setSortable(false);

			/************************ Add items ***********************/
			idList = FXCollections.observableArrayList();
			idList.add(new IDTreeObject("FIXED ID", emulator == null ? "null" : emulator.getEmulatorID().toString(),
					"FIXED"));
			for (SavedID id : UserPreferences.getSavedIDList())
			{
				idList.add(new IDTreeObject(id.getName(), id.getId().toString(), id.getNote()));
			}
			idList.add(new IDTreeObject(null, null, null));
			treeTableView.setRoot(new RecursiveTreeItem<>(idList, RecursiveTreeObject::getChildren));
			treeTableView.setShowRoot(false);
			treeTableView.setEditable(true);
			treeTableView.getColumns().setAll(nameColumn, idColumn, noteColumn);
			treeTableView.setFixedCellSize(FIXED_TREE_TABLE_CELL_SIZE);// Fixed Height

		}

		private void saveTreeTable()
		{
			SavedID.SavedIDList toSave = new SavedIDList();
			for (int i = 1; true; i++)
			{
				String strID = idColumn.getCellData(i);
				if (strID == null)
					break;
				String strName = nameColumn.getCellData(i);
				String strNote = noteColumn.getCellData(i);
				toSave.add(new SavedID(IClickerID.idFromString(strID), strName, strNote));
			}
			UserPreferences.setSavedIDList(toSave);
		}

		private void initializeSendButton()
		{
			sendingButton.setOnAction(e ->
			{
				startSending();
				isCapturing = primaryViewController.getStartToggleNode().isSelected();
				if (isCapturing)// If capturing
				{
					primaryViewController.getStartToggleNode().fire();
					primaryViewController.getStartToggleNode().setDisable(true);
				}

				if (!sendListToggle.isSelected()) // Not to send list
				{
					(new Thread(() ->
					{
						if (!emulator.isAvailable())
						{
							emulator.stopAndGoStandby();
						}
						emulator.submitAnswer(
								Answer.valueOf(((JFXRadioButton) sendingToggleGroup.getSelectedToggle()).getText()));
						Platform.runLater(() ->
						{
							endSending(isCapturing);
						});

					})).start();

				} else// Send List
				{
					initializeProgress();
					double submitSum = 0;
					for (int i = 0; idColumn.getCellData(i) != null; i++)
					{
						submitSum += sendingSlider.getValue();
					}
					double fSubmitSum = submitSum;
					(new Thread(() ->
					{
						if (!emulator.isAvailable())
						{
							emulator.stopAndGoStandby();
						}
						while (!emulator.startSubmitMode(new PrintHandler()))
							emulator.stopAndGoStandby();// If user keep click 'stop' button, then this will help
						double submitCount = 0;
						for (int indexID = 0; idColumn.getCellData(indexID) != null; indexID++)
						{
							for (int i = 0; i < sendingSlider.getValue(); i++)
							{
								emulator.submitInSubmitMode(IClickerID.idFromString(idColumn.getCellData(indexID)),
										Answer.valueOf(
												((JFXRadioButton) sendingToggleGroup.getSelectedToggle()).getText()));
								submitCount++;
								final double dfi = i, fSubmitCount = submitCount;
								Platform.runLater(() ->
								{
									sendingProgressBar.setSecondaryProgress(dfi / sendingSlider.getValue());
									sendingProgressBar.setProgress(fSubmitCount / fSubmitSum);
									double progressPercentage = fSubmitCount / fSubmitSum * 100.0;
									progressPercentage = progressPercentage > 100.0 ? 100.0 : progressPercentage;
									sendingProgressLabel.setText(String.format("%02.02f%%", progressPercentage));
								});
								try
								{
									Thread.sleep(SUBMIT_WAIT_TIME);
								} catch (InterruptedException e1)
								{
									e1.printStackTrace();
								}
							}
						}
						emulator.stopAndGoStandby();
						Platform.runLater(() ->
						{
							endSending(isCapturing);
						});
					})).start();
				}
			});
		}

		private void initializeStopButton()
		{
			stopSendingButton.setOnAction(e ->
			{
				if (sendingButton.isDisable()) // Whether is sending
				{
					(new Thread(() ->
					{
						if (!emulator.isAvailable())
							emulator.stopAndGoStandby();// And then the IllegalStateException will end the sending
														// thread. Not the best way to stop a thread but it works.
														// Call Thread.interrupted() can't stop it before the exception.
						Platform.runLater(() ->
						{
							endSending(isCapturing);
						});
					})).start();
				}
			});
		}

		/**
		 * Set all the components to get ready to sending mode.
		 */
		private void startSending()
		{
			sendingButton.setDisable(true);
			statusPaneController.setSending();
			primaryViewController.showProgressBar();
			treeTableView.setEditable(false);
			sendListToggle.setDisable(true);
			sendingSlider.setDisable(true);
		}

		/**
		 * Reset all the components changed by {@link #startSending()}
		 */
		private void endSending(boolean resumeCapturing)
		{
			primaryViewController.hideProgressBar();
			treeTableView.setEditable(true);
			sendListToggle.setDisable(false);
			statusPaneController.setReady();
			primaryViewController.getStartToggleNode().setDisable(false);
			if (sendListToggle.isSelected())
				sendingSlider.setDisable(false);
			if (resumeCapturing)
			{
				toolbarEventsHandler.OnToggleStart(null, primaryViewController.getStartToggleNode());
			} else
			{
				sendingButton.setDisable(false);
			}

		}

		private void initializeProgress()
		{
			sendingProgressBar.setProgress(0.0);
			sendingProgressBar.setSecondaryProgress(0.0);
			sendingProgressLabel.setText("00.00%");
		}

		public void update(AnswerStats stats)
		{
			class Doublet
			{
				Answer answer;
				int count;

				public Doublet(Answer answer, int count)
				{
					super();
					this.answer = answer;
					this.count = count;
				}
			}
			ArrayList<Doublet> arrayList = new ArrayList<>(5);
			arrayList.add(new Doublet(Answer.A, stats.getNumsA()));
			arrayList.add(new Doublet(Answer.B, stats.getNumsB()));
			arrayList.add(new Doublet(Answer.C, stats.getNumsC()));
			arrayList.add(new Doublet(Answer.D, stats.getNumsD()));
			arrayList.add(new Doublet(Answer.E, stats.getNumsE()));
			arrayList.sort((arg0, arg1) -> arg1.count - arg0.count);// descending order
			sendingToggleGroup.getToggles().forEach(t ->
			{
				JFXRadioButton button = (JFXRadioButton) t;
				t.setSelected(false);
				button.setDisable(true);
				if (button.getText().equals(arrayList.get(0).answer.name()))
				{
					button.setDisable(false);
					button.fire();
				}
			});
		}

		/**
		 * The class required by JFXTreeTableView
		 * 
		 * @author CSR
		 *
		 */
		final class IDTreeObject extends RecursiveTreeObject<IDTreeObject>
		{
			StringProperty name;
			StringProperty id;
			StringProperty note;

			public IDTreeObject(String name, String id, String note)
			{
				this.name = new SimpleStringProperty(name);
				this.id = new SimpleStringProperty(id);
				this.note = new SimpleStringProperty(note);
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
				sendingPaneController.update(stats);
			});
		}
	}

	private class MultipleChoiceToolbarEventHandler
			extends PrimaryViewController.AbstractPrimaryViewToolbarEventsHandler
	{

		@Override
		public void OnToggleStart(ActionEvent e, JFXToggleNode startToggle)
		{
			if (emulator != null)
			{
				if (emulator.getMode() != EmulatorModes.STANDBY)
				{
					emulator.stopAndGoStandby();
				}
				primaryViewController.showProgressBar();
				AnswerStats stats = capturingHandler.getHashMap().getAnswerStats();
				Platform.runLater(() ->
				{
					startToggle.setDisable(true);
					// Update
					dataPaneController.update(stats);
					areaChartController.update(stats);
				});
				(new Thread(() ->
				{
					emulator.startCapture(capturingHandler);
					Platform.runLater(() ->
					{
						primaryViewController.hideProgressBar();
						statusPaneController.setCapturing();
						startToggle.setDisable(false);
						startToggle.setSelected(true);
						sendingButton.setDisable(false);
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

			if (emulator != null)
			{
				primaryViewController.showProgressBar();
				(new Thread(() ->
				{
					emulator.stopAndGoStandby();
					Platform.runLater(() ->
					{
						stopSendingButton.fire();
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
