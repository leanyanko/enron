package com.mycompany.app.models;

import java.sql.Timestamp;

public class Mail {
    private Integer id;
    private int sender;
    private String subject;
    private String content;
    private Timestamp date;
    private boolean forwarded;
    private int init_id;

    public Mail(int sender, String subject, String content, Timestamp date) {
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.date = date;
    }


    public Mail(int sender, String subject, Timestamp date) {
        this.subject = subject;
        this.date = date;
    }

    public Mail(int id,  int sender, String content, Timestamp date) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.date = date;
    }

    public Mail(int sender, String subject, String content, Timestamp date, boolean forwarded, int init_id) {
        this.sender = sender;
        this.subject = subject;
        this.content = content;
        this.date = date;
        this.forwarded = forwarded;
        this.init_id = init_id;
    }

    public Mail(int id,  int sender, String content, Timestamp date, boolean forwarded, int init_id) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.date = date;
        this.forwarded = forwarded;
        this.init_id = init_id;
    }

    @Override
    public String toString() {
        return "Mail{" +
                ", sender " + sender + "\n" +
                ", subject " + subject + "\n" +
                ", content " + content + "\n" +
                ", date " + date +
                '}';
    }

    public String getContent() {
        return content;
    }

    public int getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }


    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public int getInit_id() {
        return init_id;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }

    public void setInit_id(int init_id) {
        this.init_id = init_id;
    }
}
