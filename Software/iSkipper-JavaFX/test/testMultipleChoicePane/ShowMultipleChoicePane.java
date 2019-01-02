package testMultipleChoicePane;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ShowMultipleChoicePane extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		try
		{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(this.getClass().getResource("/views/MultipleChoicePane.fxml"));
			Pane pane = loader.load();
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(this.getClass().getResource("/css/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}

}
