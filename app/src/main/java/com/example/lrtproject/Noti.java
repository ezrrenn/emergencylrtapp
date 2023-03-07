package com.example.lrtproject;

import com.google.firebase.Timestamp;

public class Noti {
    public String title, body;
    public Timestamp timestamp;
    public String uID, documentId;

    public Noti(){

    }

    public Noti( String title, String body, Timestamp timestamp) {

        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

