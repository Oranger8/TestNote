package my.orange.testnote;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;

import java.time.LocalDateTime;

public class Note {

    private ObjectProperty<LocalDateTime> date;
    private StringProperty text;

    public Note(LocalDateTime date, String text) {
        this.date = new SimpleObjectProperty<>(date);
        this.text = new SimpleStringProperty(text);
    }

    public Note(Pair<LocalDateTime, String> pair) {
        this(pair.getKey(), pair.getValue());
    }

    public String getText() {
        return text.get();
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public static String dateToString(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate() + " " + localDateTime.getHour() + ":" + localDateTime.getMinute();
    }

    public static LocalDateTime stringToDate(String date) {
        return LocalDateTime.parse(date.replace(' ', 'T'));
    }
}
