package com.fepa.meupet.model.agent.person;

public class Person {

    private String phoneNumber;     // its unique

    public Person(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
