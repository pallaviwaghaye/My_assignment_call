package com.assignment.call.model;


public class CallEntry {

    int callId;
    String name;
    String phoneNumber;
    String emailId;
    String query;
    String timeStamp;


    public CallEntry() {
    }

    public CallEntry(int callId, String name, String phoneNumber, String emailId, String query, String timeStamp) {
        this.callId = callId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.query = query;
        this.timeStamp = timeStamp;
    }

    public int getCallId() {
        return callId;
    }

    public void setCallId(int callId) {
        this.callId = callId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
