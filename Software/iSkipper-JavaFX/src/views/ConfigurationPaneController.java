package views;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.effects.JFXDepthManager;

import emulator.Emulator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import support.IClickerChannel;

public final class ConfigurationPaneController
{
	@FXML
	private AnchorPane rootPane;

	@FXML
	private AnchorPane leftPane;

	@FXML
	private AnchorPane rightPane;

	@FXML
	private AnchorPane licensePane;

	@FXML
	private JFXComboBox<IClickerChannel> channelComboBox;

	@FXML
	private Hyperlink sourceHyperLink;

	private StyleClassedTextArea textArea;

	private VirtualizedScrollPane<StyleClassedTextArea> scrollPane;

	private static final double ANCHOR_PANE_PADDING = 5.00;

	private Emulator emulator;

	private PrimaryViewController primaryViewController;

	private Application application;

	@FXML
	private void initialize()
	{
		drawDepthShadow();
		initializeComboBox();
		initializeLicensArea();
		initializeHyperLink();
	}

	private void drawDepthShadow()
	{
		JFXDepthManager.pop(leftPane);
		JFXDepthManager.pop(rightPane);
	}

	private void initializeComboBox()
	{
		channelComboBox.setItems(FXCollections.observableArrayList(IClickerChannel.values()));
		if (emulator != null)
			channelComboBox.setValue(emulator.getEmulatorChannel());
		channelComboBox.setOnAction(e ->
		{
			// TODO
		});
	}

	private void initializeLicensArea()
	{
		textArea = new StyleClassedTextArea();
		textArea.setEditable(false);
		scrollPane = new VirtualizedScrollPane<StyleClassedTextArea>(textArea);
		licensePane.getChildren().add(scrollPane);
		AnchorPane.setBottomAnchor(scrollPane, ANCHOR_PANE_PADDING);
		AnchorPane.setTopAnchor(scrollPane, ANCHOR_PANE_PADDING);
		AnchorPane.setLeftAnchor(scrollPane, ANCHOR_PANE_PADDING);
		AnchorPane.setRightAnchor(scrollPane, ANCHOR_PANE_PADDING);
		String strLicense = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/resource/Licenses.txt"))).lines()
						.collect(Collectors.joining("\n"));
		textArea.appendText(strLicense);
	}

	private void initializeHyperLink()
	{
		sourceHyperLink.setOnAction(e ->
		{
			if (application != null)
				application.getHostServices().showDocument("https://github.com/charlescao460/iSkipper-Software");
		});
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
	 * @param primaryViewController
	 *            the primaryViewController to set
	 */
	public void setPrimaryViewController(PrimaryViewController primaryViewController)
	{
		this.primaryViewController = primaryViewController;
	}

	/**
	 * @return the application
	 */
	public Application getApplication()
	{
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(Application application)
	{
		this.application = application;
	}

}
