package com.mycompany.app.controllers;

import com.mycompany.app.JDBCConnection;
import com.mycompany.app.models.Receiver;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverController {
    private static final Logger LOGGER = Logger.getLogger(MailController.class.getName());

    private final Optional<Connection> connection;

    public ReceiverController(String credentials) throws ClassNotFoundException {
        String[] db_key = credentials.split("--");
        this.connection =  Optional.ofNullable(new JDBCConnection().getConnection(db_key[0], db_key[1], db_key[2]));
    }

    public Optional<Integer> save(Receiver user) {
        final String sql = "INSERT INTO receivers (email_id, user_id) VALUES(?, ?)";
        String message = "The user to be added should not be null";
        Receiver nonNullUser = Objects.requireNonNull(user, message);

        return connection.flatMap(conn -> {
            int numberOfInsertedRows = 0;

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setInt(1, nonNullUser.getEmail_id());
                statement.setInt(2, nonNullUser.getUser_id());

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

    public void delete(Receiver email) {
        String message = "The user to be deleted should not be null";
        Receiver nonNullEmail = Objects.requireNonNull(email, message);
        String sql = "DELETE FROM receivers WHERE email_id = ? AND user_id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullEmail.getEmail_id());
                statement.setInt(1, nonNullEmail.getUser_id());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the email deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
}
