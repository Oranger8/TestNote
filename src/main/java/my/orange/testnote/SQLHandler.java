package my.orange.testnote;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLHandler {

    private static final String DATABASE_URL = "jdbc:sqlite:data.db";
    private static final String PREPARED_STATEMENT = "INSERT INTO NOTES (DATE, NOTE) VALUES (?, ?);";

    private Connection connection;

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
    }

    private Statement getStatement() throws SQLException {
        if (!connection.isValid(2)) connection = DriverManager.getConnection(DATABASE_URL);
        return connection.createStatement();
    }

    private PreparedStatement getPreparedStatement() throws SQLException {
        if (!connection.isValid(2)) connection = DriverManager.getConnection(DATABASE_URL);
        return connection.prepareStatement(PREPARED_STATEMENT);
    }

    protected List<Note> getAllNotes() throws Exception {
        Statement s = getStatement();
        List<Note> list = new ArrayList<>();
        ResultSet result = s.executeQuery("SELECT * FROM NOTES;");
        while (result.next()) {
            list.add(new Note(LocalDateTime.parse(result.getString(1)), result.getString(2)));
        }
        s.close();
        return list;
    }

    protected void addNote(Note note) throws Exception {
        PreparedStatement ps = getPreparedStatement();
        ps.setString(1, note.getDate().toString());
        ps.setString(2, note.getText());
        ps.executeUpdate();
        ps.close();
    }

    protected void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
