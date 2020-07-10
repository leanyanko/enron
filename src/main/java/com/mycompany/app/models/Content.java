package com.mycompany.app.models;

import java.sql.Connection;

public class Content {
    private int email_id;
    private String content;

    public Content(int email_id, String content) {
        this.email_id = email_id;
        this.content = content;
    }

    public void setEmail_id(int email_id) {
        this.email_id = email_id;
    }

    public int getEmail_id() {
        return email_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
