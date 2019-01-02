package application;

import java.io.IOException;
import java.lang.reflect.Field;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;

import emulator.Emulator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import views.PrimaryViewController;
import views.SelectPortsViewController;

public class Main extends Application
{
	private Stage stage;
	private JFXDecorator decorator;
	private Pane selectPortsPane;
	private Scene selectPortsscene;
	private Pane primaryViewPane;
	private Scene primaryViewScene;

	@Override
	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		try
		{
			loadSelectPortsView();
			initializeDecorator(selectPortsPane);
			initializeSelectPortsScene();
			initializeStage(selectPortsscene, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initializeStage(Scene scene, boolean isResizable)
	{
		stage.setScene(scene);
		stage.setTitle("i>Skipper");
		stage.setResizable(isResizable);
		stage.show();
	}

	private void initializeSelectPortsScene()
	{
		selectPortsscene = new Scene(decorator);
		selectPortsscene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
	}

	private void loadSelectPortsView() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/views/SelectPortsView.fxml"));
		selectPortsPane = loader.load();
		SelectPortsViewController controller = loader.getController();
		controller.setMainClass(this);
	}

	private void initializeDecorator(Pane pane)
	{
		decorator = new JFXDecorator(stage, pane);
		try
		{ // Hide the full screen button through reflection.
			Field fullScreenButtonField = JFXDecorator.class.getDeclaredField("btnFull");
			fullScreenButtonField.setAccessible(true);
			JFXButton fullScreenButton = (JFXButton) fullScreenButtonField.get(decorator);
			fullScreenButton.setDisable(true);
			fullScreenButton.setVisible(false);
			// Disable the maximize button through reflection.
			Field maxButtonField = JFXDecorator.class.getDeclaredField("btnMax");
			maxButtonField.setAccessible(true);
			JFXButton maxButton = (JFXButton) maxButtonField.get(decorator);
			maxButton.setDisable(true);;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// Set the close button to exit the whole program
		decorator.setOnCloseButtonAction(() ->
		{
			Platform.exit();
			System.exit(0);
		});
	}

	public void showPrimaryView(Emulator emulator)
	{
		if (emulator == null)
			throw new NullPointerException("Emulator cannot be null!");
		stage.close();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/views/PrimaryView.fxml"));
		PrimaryViewController controller = new PrimaryViewController();
		controller.setEmulator(emulator);
		loader.setController(controller);
		try
		{
			primaryViewPane = loader.load();
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
		stage = new Stage();
		initializeDecorator(primaryViewPane);
		primaryViewScene = new Scene(decorator);
		primaryViewScene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
		initializeStage(primaryViewScene, true);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
