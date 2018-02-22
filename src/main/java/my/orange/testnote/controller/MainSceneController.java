package my.orange.testnote.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;
import my.orange.testnote.Note;
import my.orange.testnote.TestNote;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static my.orange.testnote.Note.dateToString;
import static my.orange.testnote.Note.stringToDate;

public class MainSceneController implements Initializable {

    private TestNote testNote;

    @FXML
    TableColumn<Note, String> dateColumn, textColumn;

    @FXML
    private TableView<Note> table;

    private Dialog<Pair<LocalDateTime, String>> dialog;
    private TextArea area;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dialog = new Dialog<>();
        dialog.setTitle("New Note");
    }

    public void set(TestNote testNote, DialogPane dialogPane) {
        this.testNote = testNote;
        dialog.setDialogPane(dialogPane);
        area = (TextArea) dialogPane.getContent();
        area.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 100) ((StringProperty) observable).setValue(oldValue);
        });
        dialog.setResultConverter(param -> {
            if (param == ButtonType.FINISH)
                return new Pair<>(stringToDate(dialogPane.getHeaderText()), area.textProperty().getValue());
            else
                return null;
        });

        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(dateToString(param.getValue().getDate())));
        textColumn.setCellValueFactory(param -> param.getValue().textProperty());

        table.setItems(testNote.getNotes());
    }

    @FXML
    public void pressed() {
        dialog.setHeaderText(dateToString(LocalDateTime.now()));
        area.textProperty().setValue("");
        Optional<Pair<LocalDateTime, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> testNote.addNote(new Note(pair)));
    }
}
