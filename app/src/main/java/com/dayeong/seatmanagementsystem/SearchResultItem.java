package com.dayeong.seatmanagementsystem;

/**
 * Created by Dayeong on 2016-07-27.
 */
public class SearchResultItem {

    private String storeName;
    private double latitude, longitude;

    public SearchResultItem() {
    }

    public SearchResultItem(String storeName, double latitude, double longitude) {
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
