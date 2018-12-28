package application;

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
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("/views/SelectPortsView.fxml"));
			Pane selectPortsPane = loader.load();
			JFXDecorator decorator = new JFXDecorator(primaryStage, selectPortsPane);
			initializeDecorator(decorator);
			Scene scene = new Scene(decorator);
			scene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("i>Skipper");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initializeDecorator(JFXDecorator decorator)
	{
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
