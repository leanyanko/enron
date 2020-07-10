package com.mycompany.app.controllers;

import com.mycompany.app.JDBCConnection;
import com.mycompany.app.models.Mail;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailController {

    private static final Logger LOGGER = Logger.getLogger(MailController.class.getName());

    private final Optional<Connection> connection;

    public MailController(String credentials) throws ClassNotFoundException {
        String[] db_key = credentials.split("--");
        this.connection =  Optional.ofNullable(new JDBCConnection().getConnection(db_key[0], db_key[1], db_key[2]));
    }

    public Optional<Mail> get(int id) {
        return connection.flatMap(conn -> {
            Optional<Mail> email = Optional.empty();
            String sql = "SELECT * FROM emails WHERE id = " + id;

            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int sender = resultSet.getInt("sender_id");
                    String subject = resultSet.getString("subject");
                    Timestamp date = resultSet.getTimestamp("date");

                    email = Optional.of(
                            new Mail(id, sender, subject, date));

                    LOGGER.log(Level.INFO, "Found {0} in database", email.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return email;
        });
    }

    public Collection<Mail> getAll() {
        System.out.println("GETTING ALL DATA FROM TABLE");
        final String sql = "SELECT * FROM emails";
        final Collection<Mail> emails = new ArrayList<>();
        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    final int id = resultSet.getInt("id");
                    int sender = resultSet.getInt("sender_id");
                    String subject = resultSet.getString("subject");
                    Timestamp date = resultSet.getTimestamp("date");

                    final Mail email = new Mail(id, sender, subject, date);

                    emails.add(email);

                    LOGGER.log(Level.INFO, "Found {0} in database", email);
                }

            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return emails;
    }

    public Optional<Integer> save(Mail email) {
        final String sql = "INSERT INTO emails (sender_id, subject, date) VALUES(?, ?, ?)";
        String message = "The email to be added should not be null";
        Mail nonNullEmail = Objects.requireNonNull(email, message);

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setInt(1, nonNullEmail.getSender());
                statement.setString(2, nonNullEmail.getSubject());
                statement.setTimestamp(3, nonNullEmail.getDate());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }

            return generatedId;
        });
    }

    public Optional<Integer> saveWithForwarded(Mail email) {
        final String sql = "INSERT INTO emails_cleaned (sender_id, subject, date, forwarded, init_id) VALUES(?, ?, ?, ?, ?)";
        String message = "The email to be added should not be null";
        Mail nonNullEmail = Objects.requireNonNull(email, message);

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setInt(1, nonNullEmail.getSender());
                statement.setString(2, nonNullEmail.getSubject());
                statement.setTimestamp(3, nonNullEmail.getDate());
                statement.setBoolean(4, nonNullEmail.isForwarded());
                statement.setInt(5, nonNullEmail.getInit_id());

                int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                System.out.println(ex);
            }

            return generatedId;
        });
    }


    public void update(Mail email) {
        String message = "The customer to be updated should not be null";
        Mail nonNullEmail = Objects.requireNonNull(email, message);
        String sql = "UPDATE emails "
                + "SET "
                + "sender_id = ?, "
                + "subject = ?, "
                + "date = ? "
                + "WHERE "
                + "id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullEmail.getSender());
                statement.setString(2, nonNullEmail.getSubject());
                statement.setTimestamp(3, nonNullEmail.getDate());

                int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the email updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    public void delete(Mail email) {
        String message = "The email to be deleted should not be null";
        Mail nonNullEmail = Objects.requireNonNull(email, message);
        String sql = "DELETE FROM emails WHERE id = ?";

        connection.ifPresent(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullEmail.getId());

                int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the email deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }
}
