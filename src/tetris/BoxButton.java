package tetris;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Class for a rectangular button with text for use in a Tetris application
 *     sidebar.
 * Created by Christina Mak
 * July 27, 2016
 */
public class BoxButton extends Button {
    private Rectangle box;
    private Text text;

    /**
     * Constructor for a rectangular button with text
     * @param words the text on the button
     */
    public BoxButton(String words) {
        box = new Rectangle(120, 45, Color.SILVER);
        text = new Text(words);
        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));

        StackPane pane = new StackPane(box, text);
        super.setGraphic(pane);

        // remove default button bordering and focusing
        this.setStyle("-fx-focus-color: transparent;");
        this.setPadding(Insets.EMPTY);
        this.setFocusTraversable(false);
    }
}
