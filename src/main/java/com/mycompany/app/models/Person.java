package com.mycompany.app.models;

public class Person {
    private int id;
    private String email;
    private  boolean enron;

    public Person(String email, boolean enron) {
        this.email = email;
        this.enron = enron;
    }

    public Person(int id, String email, boolean enron) {
        this.id = id;
        this.email = email;
        this.enron = enron;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnron() {
        return enron;
    }

    public void setEnron(boolean enron) {
        this.enron = enron;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}

