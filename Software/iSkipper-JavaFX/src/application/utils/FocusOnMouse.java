package application.utils;

import javafx.scene.Node;

public final class FocusOnMouse
{
	public static void apply(Node node)
	{
		node.setOnMouseEntered(e -> node.requestFocus());
		node.setOnMouseExited(e -> node.getScene().getRoot().requestFocus());
	}
}
