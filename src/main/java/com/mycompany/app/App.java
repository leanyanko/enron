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

        String credentials = args[0];


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
        // remove meta info
        String from = row[1].replace("frozenset({'","").replace("'})", "");
        int sender_id = savePersonToDB(from);

        Timestamp d = Timestamp.valueOf(row[5]);
        Mail email = saveEmailToDB(sender_id, row, d);

        int email_id =  email.getId();
        // remove meta info
        String to = row[2].replace("frozenset({'", "").replace("'})", "");
        String[] receivers = to.split("', ");
        saveReceiversToDB(receivers, email_id);
    }

    //returns ID of either new or existing user;
    private static int savePersonToDB(String email_address) {
        int person_id;
        if (users.containsKey(email_address)) {
            person_id = users.get(email_address).getId();
        } else {
            Person person = new Person(email_address, email_address.indexOf("@enron") >= 0);
            USRS.save(person).ifPresent(person:: setId);
            person_id = person.getId();
            users.put(email_address, person);
        }
        return  person_id;
    }

    private static void saveReceiversToDB(String[] receivers, int email_id) {
        for (String receiver: receivers) {
            int user_id = savePersonToDB(receiver);
            Receiver r = new Receiver(email_id, user_id);
            RSVRS.save(r);
        }
    }

    private static Mail saveEmailToDB(int sender_id, String[] row, Timestamp d) {

        String content = row[4];
        //---------------------- Forwarded by Phillip K Allen/HOU/ECT on 01/18/2000
        //06:03 PM ---------------------------
        //
        //
        //enorman@living.com on 01/18/2000 02:44:50 PM
        //To: Phillip K Allen/HOU/ECT@ECT
        //cc: ben@living.com, enorman@living.com, stephanie@living.com
        //Subject: RE: Choosing a style

        boolean forwarded = false;
        int init_id = sender_id;
        String subject = row[3];

        if (content.indexOf("---- Forwarded by") >= 0) {
            forwarded = true;
            String marker = " ---------------------------";
            int b = content.indexOf(marker);
            if (b >= 0) {
                content = content.substring(b + marker.length());
                int e = content.indexOf("on");
                if (e >= 0) {
                    String email_address = content.substring(0, e);
                    email_address = email_address.trim();
                    if (email_address.length() <= 100 && email_address.contains("@") && !email_address.contains(" ")) {
                        System.out.println("++++++ " + email_address);
                        init_id = savePersonToDB(email_address);
                    }

                    content = content.substring(e).trim();
                    int sub = content.indexOf("Subject: ");
                    if (sub >= 0) {
                        e = content.indexOf("\n\n");
                        if (e >= 0 && sub < e) {
                            subject = content.substring(sub, e);
                            content = content.substring(e).trim();
                        }
                    }
                }
            }
        }

        Mail email = new Mail(sender_id, subject, content, d, forwarded, init_id);
        emails.add(email);
        MAILS.saveWithForwarded(email).ifPresent(email :: setId);
        Content c = new Content(email.getId(), email.getContent());
        CNTNT.save(c);
        return email;
    }
}