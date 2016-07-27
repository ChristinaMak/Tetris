package tetris;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by Christina on 7/25/2016.
 */
public class BoxButton extends Button {
    Rectangle box;
    Text text;

    public BoxButton(String words) {
        box = new Rectangle(100, 50, Color.SILVER);
        text = new Text(words);

        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 20));

        StackPane pane = new StackPane(box, text);

        super.setGraphic(pane);
        this.setStyle("-fx-focus-color: transparent;");
//        this.setStyle("-fx-border-color: transparent;");
//        this.setStyle("-fx-inner-border: transparent;");
//        this.setStyle("-fx-body-color: transparent;");
        this.setPadding(Insets.EMPTY);
    }
}
