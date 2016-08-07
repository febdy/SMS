package com.dayeong.seatmanagementsystem;

/**
 * Created by Dayeong on 2016-07-27.
 */
public class StoreInfo {

    private String storeName;
    private double latitude, longitude, distance;
    private int table_num;

    public StoreInfo() {
    }

    public StoreInfo(String storeName, double latitude, double longitude) {
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public StoreInfo(String storeName, double latitude, double longitude, int table_num, double distance) {
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.table_num = table_num;
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

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setTableNum(int table_num) {
        this.table_num = table_num;
    }

    public int getTableNum() {
        return table_num;
    }
}
