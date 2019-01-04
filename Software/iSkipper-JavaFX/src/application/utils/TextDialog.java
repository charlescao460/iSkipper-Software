/**
 * 
 */
package application.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import com.jfoenix.controls.JFXDecorator;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The class of a dialog with a text flow.
 * 
 * @author CSR
 *
 */
public class TextDialog
{
	private JFXDecorator decorator;
	private StyleClassedTextArea textArea;
	private StackPane rootPane;
	private VirtualizedScrollPane<StyleClassedTextArea> scrollPane;
	private Scene scene;
	private Stage stage;
	private static final int MAX_LENGTH = 10_000;
	private static final double PREF_HEIGHT = 450.0;
	private static final double PREF_WIDTH = 400.0;

	/**
	 * @param title
	 *            The title to show on this dialog
	 * @param styleSheet
	 *            The style sheet of this dialog
	 */
	public TextDialog(String title, String styleSheet)
	{
		textArea = new StyleClassedTextArea();
		textArea.setEditable(false);
		scrollPane = new VirtualizedScrollPane<StyleClassedTextArea>(textArea);
		rootPane = new StackPane(scrollPane);
		rootPane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
		stage = new Stage();
		stage.setTitle(title);
		decorator = new JFXDecorator(stage, rootPane, false, true, true);
		decorator.setTitle(title);
		scene = new Scene(decorator);
		scene.getStylesheets().add(styleSheet);
		stage.setScene(scene);
	}

	/**
	 * Show this dialog
	 */
	public void show()
	{
		stage.show();
	}

	/**
	 * Hide this dialog
	 */
	public void hide()
	{
		stage.hide();
	}

	/**
	 * Clean current content
	 */
	public void clear()
	{
		textArea.clear();
	}

	/**
	 * Add the string with in this dialog.
	 * 
	 * @param str
	 *            The string to add
	 * 
	 * @param printTime
	 *            Whether to print with a time mark.
	 */
	public void add(String str, boolean printTime)
	{
		if (str == null)
			return;
		if (stage.isShowing())
		{
			(new Thread(() ->
			{
				StringBuilder stringBuilder = new StringBuilder();
				if (printTime)
				{
					SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss-SSS:   ");
					stringBuilder.append(dateFormat.format(new Date()));
				}
				stringBuilder.append(str);
				Platform.runLater(() ->
				{
					textArea.appendText(stringBuilder.toString());
					if (textArea.getLength() >= MAX_LENGTH)
					{
						textArea.deleteText(0, textArea.getLength() - MAX_LENGTH);
					}
					scrollPane.scrollYBy(Double.MAX_VALUE);// Scroll to bottom
				});

			})).start();
		}
	}

	/**
	 * Add the string with in this dialog.
	 * 
	 * @param str
	 */
	public void add(String str)
	{
		add(str, false);
	}

	/**
	 * @return the decorator
	 */
	public JFXDecorator getDecorator()
	{
		return decorator;
	}

}
