package my.orange.testnote;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLHandler {

    private static String DATABASE_URL = "jdbc:sqlite:data.db";
    private static String PREPARED_STATEMENT = "INSERT INTO NOTES (DATE, NOTE) VALUES (?, ?);";

    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    SQLHandler() throws SQLException, IOException {
        File file = new File("data.db");
        if (!file.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = getClass().getResourceAsStream("/db/sample.db");
                out = new FileOutputStream(file);
                int data;
                while ((data = in.read()) != -1) out.write(data);
            } catch (IOException e) {
                throw new IOException("Failed to create database");
            } finally {
                if (in != null) try { in.close(); } catch (IOException ignored) {}
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException ignored) {}
                }
            }
        }
        connection = DriverManager.getConnection(DATABASE_URL);
        statement = connection.createStatement();
        preparedStatement = connection.prepareStatement(PREPARED_STATEMENT);
    }

    private synchronized void validate() throws SQLException  {
        if (!connection.isValid(2)) connection = DriverManager.getConnection(DATABASE_URL);
        if (statement == null) connection.createStatement();
        if (preparedStatement == null) connection.prepareStatement(PREPARED_STATEMENT);
    }

    protected List<Note> getAllNotes() throws Exception {
        validate();
        List<Note> list = new ArrayList<>();
        ResultSet result = statement.executeQuery("SELECT * FROM NOTES;");
        while (result.next()) {
            list.add(new Note(LocalDateTime.parse(result.getString(1)), result.getString(2)));
        }
        return list;
    }

    protected void addNote(Note note) throws Exception {
        validate();
        preparedStatement.setString(1, note.getDate().toString());
        preparedStatement.setString(2, note.getText());
        preparedStatement.executeUpdate();
    }

    protected void disconnect() {
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
}
