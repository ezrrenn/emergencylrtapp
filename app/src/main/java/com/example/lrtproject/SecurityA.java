package com.example.lrtproject;

public class SecurityA {

    public String uID, sFirstName, sLastName, sEmail, sPhoneNumber, sDuty;
    public boolean expanded;
    public String documentId;

    public SecurityA(String first_name){

    }

    public SecurityA(String uID, String sFirstName, String sLastName, String sEmail, String sPhoneNumber, String sDuty) {
        this.uID = uID;
        this.sFirstName = sFirstName;
        this.sLastName = sLastName;
        this.sEmail = sEmail;
        this.sPhoneNumber = sPhoneNumber;
        this.sDuty = sDuty;
        this.expanded = false;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getsFirstName() {
        return sFirstName;
    }

    public void setsFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getsLastName() {
        return sLastName;
    }

    public void setsLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsPhoneNumber() {
        return sPhoneNumber;
    }

    public void setsPhoneNumber(String sPhoneNumber) {
        this.sPhoneNumber = sPhoneNumber;
    }

    public String getsDuty() {
        return sDuty;
    }

    public void setsDuty(String sDuty) {
        this.sDuty = sDuty;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
