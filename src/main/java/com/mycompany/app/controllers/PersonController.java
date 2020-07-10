package com.mycompany.app.controllers;

import com.mycompany.app.JDBCConnection;
import com.mycompany.app.models.Person;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersonController {

    private static final Logger LOGGER = Logger.getLogger(MailController.class.getName());

    private final Optional<Connection> connection;

    public PersonController(String credentials) throws ClassNotFoundException {
        String[] db_key = credentials.split("--");
        this.connection =  Optional.ofNullable(new JDBCConnection().getConnection(db_key[0], db_key[1], db_key[2]));
    }

    public Optional<Integer> save(Person person) {
        final String sql = "INSERT INTO people (email_address, enron) VALUES(?, ?)";
        String message = "The user to be added should not be null";
        Person nonNullPerson = Objects.requireNonNull(person, message);

        return connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (PreparedStatement statement =
                         conn.prepareStatement(
                                 sql,
                                 Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullPerson.getEmail());
                statement.setBoolean(2, nonNullPerson.isEnron());

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
}
