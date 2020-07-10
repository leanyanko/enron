package com.mycompany.app.models;

public class Receiver {
    private int user_id;
    private int email_id;

    public Receiver (int email_id, int user_id) {
        this.email_id = email_id;
        this.user_id = user_id;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
