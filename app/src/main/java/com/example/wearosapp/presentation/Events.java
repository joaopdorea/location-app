package com.example.wearosapp.presentation;

public class Events {

    String id;
    String name;
    String dateTime;
    String latitude;
    String longitude;
    String category;
    String risk;

    public Events() {

    }

    public Events(String id, String name, String dateTime, String latitude, String longitude, String category, String risk) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.risk = risk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
