/**
 * 
 */
package views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import device.SerialAdapter;
import emulator.Emulator;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * @author CSR
 *
 */
public final class SelectPortsViewController
{
	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXSpinner spinner;

	@FXML
	private JFXComboBox<String> comboBox;

	@FXML
	private JFXButton refreshButton;

	@FXML
	private JFXButton cancelButton;

	@FXML
	private Label idLable;

	private SerialAdapter serial;
	private Emulator emulator;

	@FXML
	private void initialize()
	{
		spinner.setVisible(false);// Hide spinner
		listPorts();
	}

	@FXML
	private void onComboBoxItemSelected()
	{
		// Disable components
		refreshButton.setDisable(true);
		spinner.setVisible(true);
		comboBox.setDisable(true);
		(new Thread(() ->
		{
			// Open the selected port
			serial.setSerialPort(comboBox.getSelectionModel().getSelectedIndex());
			if (serial.isAvailable())
			{
				emulator = new Emulator(serial);
				if (emulator.initialize())
				{
					Platform.runLater(() ->
					{
						idLable.setText(emulator.getEmulatorID().toString());
						refreshButton.setText("Start");// The 'Refresh' button now becomes 'Start' button
						idLable.setTextFill(Color.web("#09af00"));
						refreshButton.setStyle("-fx-background-color: #41c300;");
					});
				} else
				{
					Platform.runLater(() ->
					{
						resetComponents();
						idLable.setText("Initialization failed.");
					});
				}

			} else
			{
				Platform.runLater(() -> idLable.setText("Cannot open this port!"));
			}
			// Enable components
			Platform.runLater(() ->
			{
				comboBox.setDisable(false);
				spinner.setVisible(false);
				refreshButton.setDisable(false);
			});
		})).start();
	}

	@FXML
	private void onClickRefreshButton()
	{
		if (emulator != null && emulator.isAvailable()) // Now 'Start' button
		{
			// TODO
		} else
		{
			resetComponents();
			listPorts();
		}
	}

	@FXML
	private void onClickCancelButton()
	{
		Platform.exit();
		System.exit(0);
	}

	private void listPorts()
	{
		serial = new SerialAdapter();
		ObservableList<String> ports = FXCollections.observableArrayList(serial.getAllPortsByNames());
		if (ports.size() == 0)
		{
			idLable.setText("No avaliable port on this computer!");
			return;
		}
		comboBox.getSelectionModel().clearSelection();// Clear comboBox
		comboBox.getItems().clear();
		comboBox.setItems(ports);// Show newly listed ports
	}

	/**
	 * Reset all the components' initial states.
	 */
	private void resetComponents()
	{
		if (serial != null)
			serial.close();
		emulator = null;
		idLable.setText("Unconnected");
		refreshButton.setText("Refresh");
		idLable.setTextFill(Color.web("#ee6002"));
		refreshButton.setStyle("-fx-background-color: #5162e8;");
	}

}
