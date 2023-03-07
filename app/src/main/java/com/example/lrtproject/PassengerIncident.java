package com.example.lrtproject;

public class PassengerIncident {

    public String SharingFor, Gender, EstimateDate, EstimateTime, Station, ListOfCrime, Latitude, Longitude, IncidentDescription;
    public boolean expandedd;
    public String docId;

    public PassengerIncident(){

    }

    public PassengerIncident(String sharingFor, String gender, String estimateDate, String estimateTime, String station, String listOfCrime, String latitude, String longitude, String incidentDescription) {
        this.SharingFor = sharingFor;
        this.Gender = gender;
        this.EstimateDate = estimateDate;
        this.EstimateTime = estimateTime;
        this.Station = station;
        this.ListOfCrime = listOfCrime;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.IncidentDescription = incidentDescription;
        this.expandedd = false;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String station) {
        Station = station;
    }

    public String getSharingFor() {
        return SharingFor;
    }

    public void setSharingFor(String sharingFor) {
        this.SharingFor = sharingFor;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        this.Gender = gender;
    }

    public String getEstimateDate() {
        return EstimateDate;
    }

    public void setEstimateDate(String estimateDate) {
        this.EstimateDate = estimateDate;
    }

    public String getEstimateTime() {
        return EstimateTime;
    }

    public void setEstimateTime(String estimateTime) {
        this.EstimateTime = estimateTime;
    }

    public String getListOfCrime() {
        return ListOfCrime;
    }

    public void setListOfCrime(String listOfCrime) {
        this.ListOfCrime = listOfCrime;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getIncidentDescription() {
        return IncidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.IncidentDescription = incidentDescription;
    }

    public boolean isExpandedd() {
        return expandedd;
    }

    public void setExpandedd(boolean expandedd) {
        this.expandedd = expandedd;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
