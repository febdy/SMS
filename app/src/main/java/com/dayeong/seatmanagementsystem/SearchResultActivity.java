package com.dayeong.seatmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchResultActivity extends AppCompatActivity {

    private ArrayList<SearchResultItem> storeDB = new ArrayList<>();
    private ArrayList<SearchResultItem> searchResultItemList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String searchStoreName = bundle.getString("storeName");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("latitude");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new SearchResultAdapter(getApplicationContext(), searchResultItemList);
        mRecyclerView.setAdapter(mAdapter);

        makeStoreDB();
        setSearchStoreList(searchStoreName);
        listSort();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        SearchResultItem resultClicked = searchResultItemList.get(position);
                        Intent intent = new Intent(SearchResultActivity.this, SeatAvailabilityActivity.class);
                        intent.putExtra("storeName", resultClicked.getStoreName());
                        startActivity(intent);
                    }
                })
        );

    }

    private void setSearchStoreList(String searchStoreName) {
        for (int i = 0; i < storeDB.size(); i++) {
            SearchResultItem searchResultItem = storeDB.get(i);

            if (searchResultItem.getStoreName().contains(searchStoreName))
                searchResultItemList.add(searchResultItem);
        }

        //mAdapter.notifyDataSetChanged();
    }

    public void listSort() {
        for (int i = 0; i < searchResultItemList.size(); i++) {
            SearchResultItem item = searchResultItemList.get(i);
            double distance = calculateDistance(item.getLatitude(), item.getLongitude());
            searchResultItemList.get(i).setDistance(distance);
        }

        Collections.sort(searchResultItemList, new Comparator<SearchResultItem>() {
            @Override
            public int compare(SearchResultItem o1, SearchResultItem o2) {
                if (o1.getDistance() > o2.getDistance())
                    return 1;
                else if (o1.getDistance() < o2.getDistance())
                    return -1;
                else return 0;
            }

        });

        mAdapter.notifyDataSetChanged();
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

    private void makeStoreDB() {
        SearchResultItem searchResultItem = new SearchResultItem("test0", latitude + 0.001, longitude - 0.001);
        storeDB.add(searchResultItem);

        searchResultItem = new SearchResultItem("test1", latitude - 0.001, longitude + 0.001);
        storeDB.add(searchResultItem);

        searchResultItem = new SearchResultItem("test2", latitude + 0.0015, longitude - 0.001);
        storeDB.add(searchResultItem);

        searchResultItem = new SearchResultItem("test3", latitude + 0.001, longitude - 0.0015);
        storeDB.add(searchResultItem);

        searchResultItem = new SearchResultItem("test4", latitude - 0.0016, longitude - 0.001);
        storeDB.add(searchResultItem);

        searchResultItem = new SearchResultItem("test5", latitude + 0.001, longitude + 0.0015);
        storeDB.add(searchResultItem);
    }
}
