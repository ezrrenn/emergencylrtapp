package com.example.lrtproject;

public class Passenger {
    public String txtFirstName;
    public String txtLastName;
    public String txtPhoneNum;
    public String txtEmail;
    public String txtPass;

    public Passenger(){

    }

    public Passenger(String txtFirstName, String txtLastName, String txtPhoneNum, String txtEmail, String txtPass) {
        this.txtFirstName = txtFirstName;
        this.txtLastName = txtLastName;
        this.txtPhoneNum = txtPhoneNum;
        this.txtEmail = txtEmail;
        this.txtPass = txtPass;
    }

    public String getTxtFirstName() {
        return txtFirstName;
    }

    public void setTxtFirstName(String txtFirstName) {
        this.txtFirstName = txtFirstName;
    }

    public String getTxtLastName() {
        return txtLastName;
    }

    public void setTxtLastName(String txtLastName) {
        this.txtLastName = txtLastName;
    }

    public String getTxtPhoneNum() {
        return txtPhoneNum;
    }

    public void setTxtPhoneNum(String txtPhoneNum) {
        this.txtPhoneNum = txtPhoneNum;
    }

    public String getTxtEmail() {
        return txtEmail;
    }

    public void setTxtEmail(String txtEmail) {

        this.txtEmail = txtEmail;
    }

    public String getTxtPass() {
        return txtPass;
    }

    public void setTxtPass(String txtPass) {
        this.txtPass = txtPass;
    }
}
