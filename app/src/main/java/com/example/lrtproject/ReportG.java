package com.example.lrtproject;

public class ReportG {

    public String uID, FirstName, LastName, PhoneNumber, TypeOfCrime, DateCrime, TimeCrime, Station, Description, SecurityGuard, StatusReport, StatusGuard;
    public String latitude, longitude, IDGuard;
    public String documentId;

    public ReportG(){

    }

    public ReportG(String uID, String firstName, String lastName, String phoneNumber, String typeOfCrime, String dateCrime, String timeCrime, String station, String description, String securityGuard, String statusReport, String statusGuard, String latitude, String longitude, String IDGuard) {
        this.uID = uID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.PhoneNumber = phoneNumber;
        this.TypeOfCrime = typeOfCrime;
        this.DateCrime = dateCrime;
        this.TimeCrime = timeCrime;
        this.Station = station;
        this.Description = description;
        this.SecurityGuard = securityGuard;
        this.StatusReport = statusReport;
        this.StatusGuard = statusGuard;
        this.latitude = latitude;
        this.longitude = longitude;
        this.IDGuard = IDGuard;
    }

    public String getStatusReport() {
        return StatusReport;
    }

    public void setStatusReport(String statusReport) {
        StatusReport = statusReport;
    }

    public String getStatusGuard() {
        return StatusGuard;
    }

    public void setStatusGuard(String statusGuard) {
        StatusGuard = statusGuard;
    }

    public String getSecurityGuard() {
        return SecurityGuard;
    }

    public void setSecurityGuard(String securityGuard) {
        SecurityGuard = securityGuard;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
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

    public String getIDGuard() {
        return IDGuard;
    }

    public void setIDGuard(String IDGuard) {
        this.IDGuard = IDGuard;
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
