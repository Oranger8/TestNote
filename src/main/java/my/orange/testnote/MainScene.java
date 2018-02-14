package my.orange.testnote;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainScene extends Scene {

    private GridPane gridPane;
    private int x, y;

    MainScene(TestNote testNote) {
        super(new VBox(), 540, 350);
        VBox vBox = (VBox) getRoot();

        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        vBox.getChildren().add(scrollPane);

        Button button = new Button("Add");
        button.setOnAction(testNote);
        HBox hBox = new HBox(button);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setPadding(new Insets(10, 0, 0, 0));
        vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        x = 0; y = 0;
    }

    protected void addNote(Note note) {
        if (x > 3) {
            x = 0; y++;
        }
        gridPane.add(note, x, y);
        x++;
    }
}
