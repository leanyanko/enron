package com.mycompany.app.controllers;

import com.mycompany.app.JDBCConnection;
import com.mycompany.app.models.Content;
import com.mycompany.app.models.Receiver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContentController {
    private static final Logger LOGGER = Logger.getLogger(MailController.class.getName());

    private final Optional<Connection> connection;

    public ContentController(String credentials) throws ClassNotFoundException {
        String[] db_key = credentials.split("--");
        this.connection =  Optional.ofNullable(new JDBCConnection().getConnection(db_key[0], db_key[1], db_key[2]));
    }

    public Optional<Integer> save(Content content) {
        final String sql = "INSERT INTO contents (email_id, content) VALUES(?, ?)";
        String message = "The user to be added should not be null";
        Content nonNullContent = Objects.requireNonNull(content, message);

        return connection.flatMap(conn -> {
            int numberOfInsertedRows = 0;

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setInt(1, nonNullContent.getEmail_id());
                statement.setString(2, nonNullContent.getContent());

                numberOfInsertedRows = statement.executeUpdate();

                // If success
                LOGGER.log(Level.INFO, "Was the user inserted successfully? {0}",
                        numberOfInsertedRows > 0);
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }

            return Optional.of(numberOfInsertedRows);
        });
    }
}
