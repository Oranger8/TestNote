package my.orange.testnote;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class TestNote extends Application implements EventHandler<ActionEvent> {

    private MainScene scene;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("TestNote");

        scene = new MainScene(this);

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> Platform.runLater(SQLHandler::disconnect));

        SQLHandler.connect();
        Platform.runLater(() -> {
            for (Note o : Objects.requireNonNull(SQLHandler.getAllNotes())) scene.addNote(o);
        });
        primaryStage.setMinWidth(560);
        primaryStage.setMinHeight(390);
        primaryStage.setMaxWidth(560);
    }

    @Override
    public void handle(ActionEvent event) {
        Dialog dialog = new Dialog();
        dialog.setTitle("New note");
        dialog.setHeaderText("Type a new note");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);

        VBox vBox = new VBox();
        TextArea area = new TextArea();
        area.setWrapText(true);
        area.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) area.setText(oldValue);
        });
        Label label = new Label(new Date(System.currentTimeMillis()).toString());
        vBox.getChildren().addAll(label, area);
        dialog.getDialogPane().setContent(vBox);
        Platform.runLater(area::requestFocus);
        dialog.setResultConverter(button -> {
            if (button == ButtonType.APPLY) return new Pair<>(label.getText(), area.getText());
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(stringStringPair -> newNote(stringStringPair.getKey(), stringStringPair.getValue()));
    }

    private void newNote(String date, String note) {
        Platform.runLater(() -> {
            if (SQLHandler.addNote(date, note)) scene.addNote(new Note(date, note));
        });
    }
}
