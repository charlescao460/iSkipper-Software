package testDialogPrintStream;

import application.utils.DialogPrintStream;
import application.utils.TextDialog;
import javafx.application.Application;
import javafx.stage.Stage;

public class testDialogPrintStreamMain extends Application
{

	@Override
	public void start(Stage primaryStage)
	{
		TextDialog dialog = new TextDialog("TEST", "");
		System.setOut(new DialogPrintStream(System.out, dialog));
		dialog.show();
		System.out.print("1231434134134134134134134\n");
		System.out.print("1231434134134134134134134\n");
		System.out.print("1231434134134134134134134\n");

	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
