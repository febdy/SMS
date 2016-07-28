package com.dayeong.seatmanagementsystem;

/**
 * Created by Dayeong on 2016-07-27.
 */
public class SearchResultItem {

    private String storeName;
    private int distance;

    public SearchResultItem() {
    }

    public SearchResultItem(String storeName, int distance) {
        this.storeName = storeName;
        this.distance = distance;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

}
