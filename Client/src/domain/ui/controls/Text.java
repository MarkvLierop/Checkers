package domain.ui.controls;

import javafx.scene.text.Font;

public class Text {
    private static javafx.scene.text.Text text;
    public static javafx.scene.text.Text newTextObject(String string, int x, int y, boolean isVisible, int fontSize)
    {
        text = new javafx.scene.text.Text(string);
        text.setLayoutX(x);
        text.setLayoutY(y);
        text.setFont(new Font(fontSize));
        text.setVisible(isVisible);
        return text;
    }

    public static void setText(String message)
    {
        text.setText(message);
    }

    public static void setVisible(boolean visible)
    {
        text.setVisible(visible);
    }
}
