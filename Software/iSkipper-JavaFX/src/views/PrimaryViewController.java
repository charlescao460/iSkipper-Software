/**
 * 
 */
package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import application.utils.FocusOnMouse;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
	private JFXScrollPane mainPane;

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

	@FXML
	private void initialize()
	{
		progressbar.setVisible(false);
		initializeMainPane();
		initializeHamburger();
		initializeListView();
		initializeDrawer();
		applyFocusOnMouse();
	}

	private void initializeMainPane()
	{
		mainPane.getMainHeader().setVisible(false);
		mainPane.getCondensedHeader().setVisible(false);
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

	private final static String ITEM_STRING_MULTIPLE_CHOICE = "Multiple Choice";
	private final static String ITEM_STRING_CONFIGURATION = "Configuration";

	private void initializeListView()
	{
		listView = new JFXListView<Label>();
		// Add Items here
		ObservableList<Label> labels = FXCollections.observableArrayList(
				new Label(ITEM_STRING_MULTIPLE_CHOICE), new Label(ITEM_STRING_CONFIGURATION));
		for (Label label : labels)
		{
			label.setFont(new Font(15));
		}
		listView.setItems(labels);
		listView.getSelectionModel().selectedItemProperty()
				.addListener((name, oldSelect, newSelect) ->
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

	private void applyFocusOnMouse()
	{
		FocusOnMouse.apply(rawToggle);
		FocusOnMouse.apply(resetButton);
		FocusOnMouse.apply(startToggle);
		FocusOnMouse.apply(stopButton);
	}

}
