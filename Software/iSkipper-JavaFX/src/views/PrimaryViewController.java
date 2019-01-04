/**
 * 
 */
package views;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXToolbar;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import application.utils.DialogPrintStream;
import application.utils.FocusOnMouse;
import application.utils.TextDialog;
import emulator.Emulator;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author CSR
 *
 */
public final class PrimaryViewController
{

	@FXML
	private BorderPane rootPane;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private AnchorPane mainPane;

	@FXML
	private JFXToolbar toolbar;

	@FXML
	private Label toolbarTitle;

	@FXML
	private JFXHamburger hamburger;

	@FXML
	private JFXToggleNode startToggle;

	@FXML
	private JFXButton stopButton;

	@FXML
	private JFXButton resetButton;

	@FXML
	private JFXToggleNode rawToggle;

	@FXML
	private JFXDrawer drawer;

	@FXML
	private JFXProgressBar progressbar;

	private JFXListView<Label> listView;

	private AbstractPrimaryViewToolbarEventsHandler toolbarEventsHandler;

	private Emulator emulator;

	private TextDialog rawOutputDialog;

	private static final double SVG_ICON_RATIO = 0.6;

	private final static String ITEM_STRING_MULTIPLE_CHOICE = "Multiple Choice";

	private final static String ITEM_STRING_CONFIGURATION = "Configuration";

	@FXML
	private void initialize()
	{
		// initialize components of PrimaryView
		progressbar.setVisible(false);
		initializeHamburger();
		initializeListView();
		initializeDrawer();
		initilaizeRawToggle();
		setSvgIcons();
		applyFocusOnMouse();
		// Load content into PrimaryView
		loadMutipleChoicePane();
		setToolbarComponentEventHandlers();
	}

	private void initializeHamburger()
	{
		hamburger.setAnimation(new HamburgerBackArrowBasicTransition(hamburger));
		hamburger.setOnMouseClicked(e ->
		{
			if (drawer.isClosed() || drawer.isClosing())
			{
				drawer.open();
			} else
			{
				drawer.close();
			}
		});
	}

	private void initializeListView()
	{
		listView = new JFXListView<Label>();
		// Add Items here
		ObservableList<Label> labels = FXCollections.observableArrayList(new Label(ITEM_STRING_MULTIPLE_CHOICE),
				new Label(ITEM_STRING_CONFIGURATION));
		for (Label label : labels)
		{
			label.setFont(new Font(15));
		}
		listView.setItems(labels);
		listView.getSelectionModel().selectedItemProperty().addListener((name, oldSelect, newSelect) ->
		{
			// Change content of mainPane here.
			switch (newSelect.getText())
			{
			case ITEM_STRING_MULTIPLE_CHOICE:
				// TODO
				break;
			case ITEM_STRING_CONFIGURATION:
				// TODO
				break;
			}
		});
	}

	private void initializeDrawer()
	{
		drawer.setOverLayVisible(false);
		drawer.setSidePane(listView);
		drawer.setOnDrawerOpening(e ->
		{
			final Transition animation = hamburger.getAnimation();
			animation.setRate(1);
			animation.play();
		});
		drawer.setOnDrawerClosing(e ->
		{
			final Transition animation = hamburger.getAnimation();
			animation.setRate(-1);
			animation.play();
		});
	}

	private void initilaizeRawToggle()
	{
		rawOutputDialog = new TextDialog("i>Skipper - Raw Output",
				this.getClass().getResource("/css/application.css").toExternalForm());
		rawOutputDialog.getStage().getIcons().add(new Image(this.getClass().getResourceAsStream("/resource/icon.png")));
		System.setOut(new DialogPrintStream(System.out, rawOutputDialog));
		rawToggle.setOnAction(e ->
		{
			if (rawToggle.isSelected())
				rawOutputDialog.show();
			else
				rawOutputDialog.hide();
		});
		rawOutputDialog.getDecorator().setOnCloseButtonAction(() ->
		{
			rawToggle.setSelected(false);
			rawOutputDialog.hide();
		});
	}

	private void setSvgIcons()
	{

		// Start Toggle Node
		SVGGlyph startSvg = new SVGGlyph(
				"M 512 64 C 264.6 64 64 264.6 64 512 s 200.6 448 448 448 s 448 -200.6 448 -448 S 759.4 64 "
						+ "512 64 Z m 0 820 c -205.4 0 -372 -166.6 -372 -372 s 166.6 -372 372 -372 s 372 166.6 372 "
						+ "372 s -166.6 372 -372 372 Z M 719.4 499.1 l -296.1 -215 A 15.9 15.9 0 0 0 398 297 v 430 c "
						+ "0 13.1 14.8 20.5 25.3 12.9 l 296.1 -215 a 15.9 15.9 0 0 0 0 -25.8 Z m -257.6 134 V 390.9 L "
						+ "628.5 512 L 461.8 633.1 Z",
				Color.WHITE);
		startToggle.setText(null);
		setSVGForNode(startToggle, startSvg);

		// Stop Button
		SVGGlyph stopSvg = new SVGGlyph(
				"M 512 64 C 264.6 64 64 264.6 64 512 s 200.6 448 448 448 s 448 -200.6 448 -448 S 759.4 64 512 64 Z m 0 "
						+ "820 c -205.4 0 -372 -166.6 -372 -372 c 0 -89 31.3 -170.8 83.5 -234.8 l 523.3 523.3 C 682.8 852.7 601 "
						+ "884 512 884 Z m 288.5 -137.2 L 277.2 223.5 C 341.2 171.3 423 140 512 140 c 205.4 0 372 166.6 372 372 "
						+ "c 0 89 -31.3 170.8 -83.5 234.8 Z",
				Color.WHITE);
		stopButton.setText(null);
		setSVGForNode(stopButton, stopSvg);

		// Reset Button
		SVGGlyph resetSvg = new SVGGlyph(
				"M 909.1 209.3 l -56.4 44.1 C 775.8 155.1 656.2 92 521.9 92 C 290 92 102.3 279.5 102 511.5 C 101.7 743.7 289.8 932 "
						+ "521.9 932 c 181.3 0 335.8 -115 394.6 -276.1 c 1.5 -4.2 -0.7 -8.9 -4.9 -10.3 l -56.7 -19.5 a 8 8 0 0 0 -10.1 4.8 "
						+ "c -1.8 5 -3.8 10 -5.9 14.9 c -17.3 41 -42.1 77.8 -73.7 109.4 A 344.77 344.77 0 0 1 655.9 829 c -42.3 17.9 -87.4 "
						+ "27 -133.8 27 c -46.5 0 -91.5 -9.1 -133.8 -27 A 341.5 341.5 0 0 1 279 755.2 a 342.16 342.16 0 0 1 -73.7 -109.4 c "
						+ "-17.9 -42.4 -27 -87.4 -27 -133.9 s 9.1 -91.5 27 -133.9 c 17.3 -41 42.1 -77.8 73.7 -109.4 c 31.6 -31.6 68.4 -56.4 "
						+ "109.3 -73.8 c 42.3 -17.9 87.4 -27 133.8 -27 c 46.5 0 91.5 9.1 133.8 27 a 341.5 341.5 0 0 1 109.3 73.8 c 9.9 9.9 19.2 "
						+ "20.4 27.8 31.4 l -60.2 47 a 8 8 0 0 0 3 14.1 l 175.6 43 c 5 1.2 9.9 -2.6 9.9 -7.7 l 0.8 -180.9 c -0.1 -6.6 -7.8 -10.3 "
						+ "-13 -6.2 Z",
				Color.WHITE);
		resetButton.setText(null);
		setSVGForNode(resetButton, resetSvg);

		// Raw Toggle
		SVGGlyph rawSvg = new SVGGlyph(
				"M 688 312 v -48 c 0 -4.4 -3.6 -8 -8 -8 H 296 c -4.4 0 -8 3.6 -8 8 v 48 c 0 4.4 3.6 8 8 8 h 384 c 4.4 0 8 -3.6 "
						+ "8 -8 Z m -392 88 c -4.4 0 -8 3.6 -8 8 v 48 c 0 4.4 3.6 8 8 8 h 184 c 4.4 0 8 -3.6 8 -8 v -48 c 0 -4.4 -3.6 "
						+ "-8 -8 -8 H 296 Z m 376 116 c -119.3 0 -216 96.7 -216 216 s 96.7 216 216 216 s 216 -96.7 216 -216 s -96.7 -216 "
						+ "-216 -216 Z m 107.5 323.5 C 750.8 868.2 712.6 884 672 884 s -78.8 -15.8 -107.5 -44.5 C 535.8 810.8 520 772.6 "
						+ "520 732 s 15.8 -78.8 44.5 -107.5 C 593.2 595.8 631.4 580 672 580 s 78.8 15.8 107.5 44.5 C 808.2 653.2 824 "
						+ "691.4 824 732 s -15.8 78.8 -44.5 107.5 Z M 640 812 a 32 32 0 1 0 64 0 a 32 32 0 1 0 -64 0 Z m 12 -64 h 40 "
						+ "c 4.4 0 8 -3.6 8 -8 V 628 c 0 -4.4 -3.6 -8 -8 -8 h -40 c -4.4 0 -8 3.6 -8 8 v 112 c 0 4.4 3.6 8 8 8 Z M "
						+ "440 852 H 208 V 148 h 560 v 344 c 0 4.4 3.6 8 8 8 h 56 c 4.4 0 8 -3.6 8 -8 V 108 c 0 -17.7 -14.3 -32 -32 "
						+ "-32 H 168 c -17.7 0 -32 14.3 -32 32 v 784 c 0 17.7 14.3 32 32 32 h 272 c 4.4 0 8 -3.6 8 -8 v -56 c 0 "
						+ "-4.4 -3.6 -8 -8 -8 Z",
				Color.WHITE);
		rawToggle.setText(null);
		setSVGForNode(rawToggle, rawSvg);
	}

	private void setSVGForNode(Labeled node, SVGGlyph svg)
	{
		svg.setSizeForHeight(node.getPrefHeight() * SVG_ICON_RATIO);
		node.setGraphic(svg);
	}

	private void setToolbarComponentEventHandlers()
	{
		if (toolbarEventsHandler != null)
		{
			startToggle.setOnAction(e ->
			{
				if (startToggle.isSelected())
					toolbarEventsHandler.OnToggleStart(e, startToggle);
				else
					toolbarEventsHandler.OnUntoggleStart(e, startToggle);
			});
			stopButton.setOnAction(e -> toolbarEventsHandler.OnActionButtonStop(e, stopButton));
			resetButton.setOnAction(e -> toolbarEventsHandler.OnActionButtonReset(e, resetButton));

		}

	}

	private void loadMutipleChoicePane()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/views/MultipleChoicePane.fxml"));
		MultipleChoicePaneController controller = new MultipleChoicePaneController();
		controller.setEmulator(emulator);
		controller.setPrimaryViewController(this);
		loader.setController(controller);
		Pane mutipleChoicePane;
		try
		{
			mutipleChoicePane = loader.load();
			changeMainPaneContent(mutipleChoicePane);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.setToolbarEventsHandler(controller.getToolbarEventsHandler());

	}

	private void changeMainPaneContent(Node node)
	{
		mainPane.getChildren().clear();
		mainPane.getChildren().add(node);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setTopAnchor(node, 0.0);

		JFXScrollPane.smoothScrolling(scrollPane);
	}

	private void applyFocusOnMouse()
	{
		FocusOnMouse.apply(rawToggle);
		FocusOnMouse.apply(resetButton);
		FocusOnMouse.apply(startToggle);
		FocusOnMouse.apply(stopButton);

	}

	/**
	 * The abstract class to handle events of the toolbar's components of
	 * PrimaryView.
	 * 
	 * @author CSR
	 *
	 */
	public static abstract class AbstractPrimaryViewToolbarEventsHandler
	{
		/**
		 * When the 'start' ToggleNode was toggled.
		 */
		public abstract void OnToggleStart(ActionEvent e, JFXToggleNode startToggle);

		/**
		 * When the 'start' ToggleNode was untoggled.
		 */
		public abstract void OnUntoggleStart(ActionEvent e, JFXToggleNode startToggle);

		/**
		 * When the 'stop' button was activated.
		 */
		public abstract void OnActionButtonStop(ActionEvent e, JFXButton stopButton);

		/**
		 * When the 'reset' button was activated.
		 */
		public abstract void OnActionButtonReset(ActionEvent e, JFXButton resetButton);
	}

	/**
	 * @return the toolbarEventsHandler to handle events of the toolbar's components
	 *         of PrimaryView.
	 */
	public AbstractPrimaryViewToolbarEventsHandler getToolbarEventsHandler()
	{
		return toolbarEventsHandler;
	}

	/**
	 * @param toolbarEventsHandler
	 *            the toolbarEventsHandler to set to handle events of the toolbar's
	 *            components of PrimaryView.
	 */
	public void setToolbarEventsHandler(AbstractPrimaryViewToolbarEventsHandler toolbarEventsHandler)
	{
		this.toolbarEventsHandler = toolbarEventsHandler;
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
	 * Show the progress Bar.
	 */
	public void showProgressBar()
	{
		progressbar.setVisible(true);
	}

	/**
	 * Hide the progress Bar.
	 */
	public void hideProgressBar()
	{
		progressbar.setVisible(false);
	}

	/**
	 * @return the 'start' JFXToggleNode
	 */
	public JFXToggleNode getStartToggleNode()
	{
		return startToggle;
	}

}
