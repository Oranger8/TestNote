package my.orange.testnote;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import my.orange.testnote.controller.MainSceneController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestNote extends Application {

    private ObservableList<Note> notes;
    private SQLHandler sql;

    private Executor executor;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        try {
            sql = new SQLHandler();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            System.exit(1);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database is unreachable", ButtonType.OK).showAndWait();
            System.exit(1);
        }
        notes = FXCollections.observableArrayList();
        executor = Executors.newFixedThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        primaryStage.setTitle("TestNote");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainscene.fxml"));
        Scene scene = new Scene(loader.load());
        MainSceneController sceneController = loader.getController();
        primaryStage.setScene(scene);
        primaryStage.show();

        FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/fxml/dialog.fxml"));
        DialogPane dialogPane = dialogLoader.load();


        Task<List<Note>> getAllTask = new GetAllTask();
        executor.execute(getAllTask);

        sceneController.set(this, dialogPane);
    }

    public ObservableList<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        executor.execute(new AddTask(note));
    }

    @Override
    public void stop() {
        if (sql != null) sql.disconnect();
    }

    class GetAllTask extends Task<List<Note>> {

        GetAllTask() {
            setOnSucceeded(event -> notes.addAll(GetAllTask.this.getValue()));
            setOnFailed(event -> GetAllTask.this.getException().printStackTrace());
        }

        @Override
        protected List<Note> call() throws Exception {
            return sql.getAllNotes();
        }
    }

    class AddTask extends Task {

        private Note note;

        AddTask(Note note) {
            this.note = note;
            setOnSucceeded(event -> notes.add(note));
            setOnFailed(event -> AddTask.this.getException().printStackTrace());
        }

        @Override
        protected Object call() throws Exception {
            sql.addNote(note);
            return null;
        }
    }
}
