package com.dayeong.seatmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchResultActivity extends AppCompatActivity implements AsyncResponse {
    GetStoreDB task;
    String url = "http://175.126.112.111/storedata.php";

    private ArrayList<StoreInfo> storeInfoList = new ArrayList<>();
    private String searchStoreName;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    double curLatitude, curLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        searchStoreName = bundle.getString("storeName");
        curLatitude = bundle.getDouble("latitude");
        curLongitude = bundle.getDouble("longitude");

        executeDB();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new SearchResultAdapter(getApplicationContext(), storeInfoList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        StoreInfo resultClicked = storeInfoList.get(position);
                        Intent intent = new Intent(SearchResultActivity.this, SeatAvailabilityActivity.class);
                        intent.putExtra("storeName", resultClicked.getStoreName());
                        startActivity(intent);
                    }
                })
        );
    }

    private void executeDB() {
        task = new GetStoreDB(getApplicationContext(), SearchResultActivity.this);
        task.listener = this;
        task.execute(url);
    }

    @Override
    public void processFinish(String str) {
        String store_name;
        double latitude;
        double longitude;
        int table_num;
        double distance;

        try {
            JSONObject root = new JSONObject(str);
            JSONArray jsonArray = root.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                store_name = jsonObject.getString("store_name");

                if (store_name.contains(searchStoreName)) {
                    latitude = jsonObject.getDouble("latitude");
                    longitude = jsonObject.getDouble("longitude");
                    table_num = jsonObject.getInt("table_num");
                    distance = calculateDistance(latitude, longitude);

                    //if (distance <= 10)
                    storeInfoList.add(new StoreInfo(store_name, latitude, longitude, table_num, distance));
                }
            }

            listSort();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void listSort() {
        Collections.sort(storeInfoList, new Comparator<StoreInfo>() {
            @Override
            public int compare(StoreInfo o1, StoreInfo o2) {
                if (o1.getDistance() > o2.getDistance())
                    return 1;
                else if (o1.getDistance() < o2.getDistance())
                    return -1;
                else return 0;
            }

        });
    }

    private double calculateDistance(double storeLatitude, double storeLongitude) {
        double theta = curLongitude - storeLongitude;
        double dist = Math.sin(deg2rad(curLatitude)) * Math.sin(deg2rad(storeLatitude)) + Math.cos(deg2rad(curLatitude)) * Math.cos(deg2rad(storeLatitude)) * Math.cos(deg2rad(theta));

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
