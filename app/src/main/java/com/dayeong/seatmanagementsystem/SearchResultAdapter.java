package com.dayeong.seatmanagementsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dayeong on 2016-07-27.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<SearchResultItem> searchResultItemList;
    private Context context;
    private double latitude, longitude;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storeName, distance;

        public ViewHolder(View v) {
            super(v);
            storeName = (TextView) v.findViewById(R.id.txt_store_name);
            distance = (TextView) v.findViewById(R.id.txt_distance);
        }
    }

    public SearchResultAdapter(Context context, ArrayList<SearchResultItem> mySearchResultItemList) {
        this.context = context;
        searchResultItemList = mySearchResultItemList;
    }

    public SearchResultAdapter(Context context, ArrayList<SearchResultItem> mySearchResultItemList, double latitude, double longitude) {
        this.context = context;
        searchResultItemList = mySearchResultItemList;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchResultItem item = searchResultItemList.get(position);
        String listStoreName = item.getStoreName();
        double listDistance = calculateDistance(item.getLatitude(), item.getLongitude());

        holder.storeName.setText(listStoreName);
        holder.distance.setText(String.format("%.2f", listDistance));
    }

    @Override
    public int getItemCount() {
        return searchResultItemList.size();
    }

    private double calculateDistance(double itemLatitude, double itemLongitude) {
        double currentLatitude = this.latitude;
        double currentLongitude = this.longitude;

        double theta = currentLongitude - itemLongitude;
        double dist = Math.sin(deg2rad(currentLatitude)) * Math.sin(deg2rad(itemLatitude)) + Math.cos(deg2rad(currentLatitude)) * Math.cos(deg2rad(itemLatitude)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; // kilometer

        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
