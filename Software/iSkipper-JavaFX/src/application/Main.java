package application;

import java.io.IOException;
import java.lang.reflect.Field;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application
{
	private Stage stage;
	private JFXDecorator decorator;
	private Pane selectPortsPane;
	private Scene selectPortsScene;

	@Override
	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		try
		{
			loadSelectPortsView();
			initializeDecorator();
			initializeSelectPortsScene();
			initializeStage();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initializeStage()
	{
		stage.setScene(selectPortsScene);
		stage.setTitle("i>Skipper");
		stage.setResizable(false);
		stage.show();
	}

	private void initializeSelectPortsScene()
	{
		selectPortsScene = new Scene(decorator);
		selectPortsScene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
	}

	private void loadSelectPortsView() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/views/SelectPortsView.fxml"));
		selectPortsPane = loader.load();
	}

	private void initializeDecorator()
	{
		decorator = new JFXDecorator(stage, selectPortsPane);
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

	public static void main(String[] args)
	{
		launch(args);
	}
}
