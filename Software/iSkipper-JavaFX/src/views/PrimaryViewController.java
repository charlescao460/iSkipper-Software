/**
 * 
 */
package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * @author CSR
 *
 */
public final class PrimaryViewController
{

	@FXML
	private BorderPane rootPane;

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
	private JFXDrawer drawer;

	@FXML
	private JFXProgressBar progressbar;

	@FXML
	private void initialize()
	{
		progressbar.setVisible(false);
		initializeHamburger();
		initializeDrawer();
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

	private void initializeDrawer()
	{
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

}
