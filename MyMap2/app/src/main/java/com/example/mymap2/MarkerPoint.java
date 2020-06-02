package com.example.mymap2;

public class MarkerPoint {
    private String countryName;
    private String locality;
    private String latitude;
    private String longitude;
    private String colorMarker;
    private String info;
    private String sensor;
    private String sensor_value;

    public MarkerPoint(){}

    public MarkerPoint(String countryName, String locality, String latitude, String longitude, String colorMarker, String info, String sensor, String sensor_value) {
        this.countryName = countryName;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.colorMarker = colorMarker;
        this.info = info;
        this.sensor = sensor;
        this.sensor_value = sensor_value;
    }

    public String getSensor_value() { return sensor_value; }

    public void setSensor_value(String sensor_value) { this.sensor_value = sensor_value; }

    public String getSensor() { return sensor; }

    public void setSensor(String sensor) { this.sensor = sensor; }

    public String getInfo() { return info; }

    public void setInfo(String info) { this.info = info; }

    public String getColorMarker() {
        return colorMarker;
    }

    public void setColorMarker(String colorMarker) {
        this.colorMarker = colorMarker;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
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

}
