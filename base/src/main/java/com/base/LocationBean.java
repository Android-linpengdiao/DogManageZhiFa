package com.base;

public class LocationBean {

    private String query = "";
    private double latitude = 0;
    private double longitude = 0;

    public LocationBean(String query, double latitude, double longitude) {
        this.query = query;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
