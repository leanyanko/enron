package com.mycompany.app;

import com.mycompany.app.controllers.ContentController;
import com.mycompany.app.controllers.MailController;
import com.mycompany.app.controllers.ReceiverController;
import com.mycompany.app.controllers.PersonController;
import com.mycompany.app.models.Content;
import com.mycompany.app.models.Mail;
import com.mycompany.app.models.Person;
import com.mycompany.app.models.Receiver;
import com.opencsv.CSVReader;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private static List<Mail> emails = new ArrayList<>();
    private static Map<String, Person> users = new HashMap<>();

    private static MailController MAILS;
    private static PersonController USRS;
    private static ReceiverController RSVRS;
    private static ContentController CNTNT;

    public static void readCSV(String file)
    {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] row;

            // we are going to read data line by line
            while ((row = csvReader.readNext()) != null) {
                //if it's a title
                if (row[5].indexOf("new") >= 0) continue;
                processRow(row);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws ClassNotFoundException {

        String credentials =

        //        String[] key = credentials.split("--");
//        Optional<Connection> connection=  Optional.ofNullable(new JDBCConnection().getConnection(key[0], key[1], key[2]));

        //database manipulations
        MAILS = new MailController(credentials);
        USRS = new PersonController(credentials);
        RSVRS = new ReceiverController(credentials);
        CNTNT = new ContentController(credentials);

        //path to initial file.
        String csvFile = "enron_test.csv";

        try {
            readCSV(csvFile);
        } catch (Exception e) {
            System.out.printf("EXCPTN: " + e);
        }

        System.out.println( "Hello World!" );
    }

    private static void processRow (String[] row) {

        Timestamp d = Timestamp.valueOf(row[5]);

        // remove meta info
        String from = row[1].replace("frozenset({'","").replace("'})", "");
        String to = row[2].replace("frozenset({'", "").replace("'})", "");
        int sender_id;
        String[] receivers = to.split("', ");
        String content = row[4];
        //---------------------- Forwarded by Phillip K Allen/HOU/ECT on 01/18/2000
        //06:03 PM ---------------------------

        if (users.containsKey(from)) {
            sender_id = users.get(from).getId();
        } else {
            Person person = new Person(from, from.indexOf("@enron") >= 0);
            USRS.save(person).ifPresent(person:: setId);
            sender_id = person.getId();
            users.put(from, person);
        }

        //crete single email
        Mail email = new Mail(sender_id, row[3], content, d);
        emails.add(email);
        MAILS.save(email).ifPresent(email :: setId);
        Content c = new Content(email.getId(), email.getContent());
        CNTNT.save(c);
        for (String receiver: receivers) {
            int email_id = email.getId();
            int user_id;
            if (users.containsKey(receiver)) {
                user_id = users.get(receiver).getId();
            } else {
                Person person = new Person(receiver, receiver.indexOf("@enron") >= 0);
                USRS.save(person).ifPresent(person:: setId);
                user_id = person.getId();
                users.put(receiver, person);
            }
            Receiver r = new Receiver(email_id, user_id);
            RSVRS.save(r);
        }
    }
}