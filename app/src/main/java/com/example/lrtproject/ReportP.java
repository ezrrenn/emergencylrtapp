package com.example.lrtproject;

public class ReportP {
    public String uId, FirstName, LastName, PhoneNumber, DateCrime, TypeOfCrime, Emergency, TimeCrime, Station, Description, StatusReport;
    public boolean expanded;
    public String documentId;

    public ReportP(){

    }

    public ReportP(String uId, String firstName, String lastName, String phoneNumber, String dateCrime, String typeOfCrime, String emergency, String timeCrime, String station, String description, String statusReport) {
        this.uId = uId;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.PhoneNumber = phoneNumber;
        this.DateCrime = dateCrime;
        this.TypeOfCrime = typeOfCrime;
        this.Emergency = emergency;
        this.TimeCrime = timeCrime;
        this.Station = station;
        this.Description = description;
        this.StatusReport = statusReport;
        this.expanded = false;
    }


    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getStatusReport() {
        return StatusReport;
    }

    public void setStatusReport(String statusReport) {
        this.StatusReport = statusReport;
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

    public String getDateCrime() {
        return DateCrime;
    }

    public void setDateCrime(String dateCrime) {
        this.DateCrime = dateCrime;
    }

    public String getTypeOfCrime() {
        return TypeOfCrime;
    }

    public void setTypeOfCrime(String typeOfCrime) {
        this.TypeOfCrime = typeOfCrime;
    }

    public String getEmergency() {
        return Emergency;
    }

    public void setEmergency(String emergency) {
        this.Emergency = emergency;
    }

    public String getTimeCrime() {
        return TimeCrime;
    }

    public void setTimeCrime(String timeCrime) {
        this.TimeCrime = timeCrime;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        this.Station = station;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

}
