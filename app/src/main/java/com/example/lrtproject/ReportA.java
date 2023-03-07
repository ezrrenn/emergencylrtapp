package com.example.lrtproject;

public class ReportA {
    public String uID, FirstName, LastName, PhoneNumber, TypeOfCrime, DateCrime, TimeCrime, Station, Description, StatusReport;
    public String latitude, longitude, SecurityGuard, IDGuard;
    public String documentId;

    public ReportA(){

    }

    public ReportA(String uID, String firstName, String lastName, String phoneNumber, String typeOfCrime, String dateCrime, String timeCrime, String station, String latitude, String longitude, String description, String statusReport, String securityGuard, String IDGuard) {
        this.uID = uID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.PhoneNumber = phoneNumber;
        this.TypeOfCrime = typeOfCrime;
        this.DateCrime = dateCrime;
        this.TimeCrime = timeCrime;
        this.Station = station;
        this.latitude = latitude;
        this.longitude = longitude;
        this.Description = description;
        this.StatusReport = statusReport;
        this.SecurityGuard = securityGuard;
        this.IDGuard = IDGuard;
    }

    //public ReportA(String uID) {
   //     this.uID = uID;
    //}


    public String getIDGuard() {
        return IDGuard;
    }

    public void setIDGuard(String IDGuard) {
        this.IDGuard = IDGuard;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getSecurityGuard() {
        return SecurityGuard;
    }

    public void setSecurityGuard(String securityGuard) {
        this.SecurityGuard = securityGuard;
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

    public String getTypeOfCrime() {
        return TypeOfCrime;
    }

    public void setTypeOfCrime(String typeOfCrime) {
        this.TypeOfCrime = typeOfCrime;
    }

    public String getDateCrime() {
        return DateCrime;
    }

    public void setDateCrime(String dateCrime) {
        this.DateCrime = dateCrime;
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

    public String getStatusReport() {
        return StatusReport;
    }

    public void setStatusReport(String statusReport) {
        StatusReport = statusReport;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
