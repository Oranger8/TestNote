package my.orange.testnote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLHandler {

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    protected static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        statement = connection.createStatement();
        preparedStatement = connection.prepareStatement("INSERT INTO NOTES (DATE, NOTE) VALUES (?, ?);");
    }

    protected static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static List<Note> getAllNotes() {
        List<Note> list = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM NOTES;");
            while (result.next()) {
                list.add(new Note(result.getString(1), result.getString(2)));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static boolean addNote(String date, String text) {
        try {
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, text);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
