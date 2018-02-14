package my.orange.testnote;

import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

class Note extends VBox {

    Note(String date, String text) {
        TextField field = new TextField(date);
        field.setDisable(true);
        field.setAlignment(Pos.CENTER);
        field.setPrefWidth(120);
        getChildren().add(field);

        TextArea area = new TextArea(text);
        area.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) area.setText(oldValue);
        });
        area.setEditable(false);
        area.setPrefSize(120, 110);
        area.setWrapText(true);
        getChildren().add(area);
    }
}
