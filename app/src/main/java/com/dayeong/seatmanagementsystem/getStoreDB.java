package com.dayeong.seatmanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Dayeong on 2016-07-24.
 */
public class GetStoreDB extends AsyncTask<String, Integer, String> {
    StoreStatusInfo storeInfo;
    Context context;
    Activity activity;
    String storeName = "";
    ArrayList<String> storeNameList;
    ArrayList<SearchResultItem> searchResultItemList;
    Double curLatitude, curLongitude;
    String mode;

    public GetStoreDB(Context context, Activity activity, String mode) {
        this.context = context;
        this.activity = activity;
        storeNameList = new ArrayList<>();
        this.mode = mode;
    }

    public GetStoreDB(Context context, Activity activity, String storeName, String mode) {
        this.context = context;
        this.activity = activity;
        this.storeName = storeName;
        this.mode = mode;
    }

    public GetStoreDB(Context context, Activity activity, String storeName, Double latitude, Double longitude, String mode) {
        this.context = context;
        this.activity = activity;
        this.storeName = storeName;
        this.curLatitude = latitude;
        this.curLongitude = longitude;
        searchResultItemList = new ArrayList<>();
        this.mode = mode;
    }

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder jsonHtml = new StringBuilder();
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                //conn.setConnectTimeout(10000);
                conn.setUseCaches(false);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    for (; ; ) {
                        String line = br.readLine();
                        if (line == null) break;
                        jsonHtml.append(line + "\n");
                    }
                    br.close();
                } else {
                    Log.d("로그 : ", "ResponseCode is not \"OK\"");
                }
                conn.disconnect();
            } else {
                Log.d("로그 : ", "HttpURLConnection is null");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonHtml.toString();

    }

    protected void onPostExecute(String str) {
        switch (mode) {
            case "getIndex":
                getStoreInfo(str);
                break;
            case "getSearch":
                getSearchInfo(str);
                break;
            case "getSeat":
                getSeatInfo(str);
                break;
            default:
        }
    }

    protected void getStoreInfo(String str) {
        String store_name;

        try {
            JSONObject root = new JSONObject(str);
            JSONArray jsonArray = root.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                store_name = jsonObject.getString("store_name");

                storeNameList.add(store_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void getSearchInfo(String str) {
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

                if (store_name.contains(storeName)) {
                    latitude = jsonObject.getDouble("latitude");
                    longitude = jsonObject.getDouble("longitude");
                    table_num = jsonObject.getInt("table_num");
                    distance = calculateDistance(latitude, longitude);

                    //if (distance <= 10)
                        searchResultItemList.add(new SearchResultItem(store_name, latitude, longitude, table_num, distance));
                }
            }

            listSort();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void getSeatInfo(String str) {
        String store_name;
        int table_num;
        int[] tablesStatus;

        if (storeName != null) {
            try {
                JSONObject root = new JSONObject(str);
                JSONArray jsonArray = root.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    store_name = jsonObject.getString("store_name");

                    if (storeName.equals(store_name)) {
                        table_num = jsonObject.getInt("table_num");
                        tablesStatus = new int[table_num + 1];
                        tablesStatus[0] = 0;

                        for (int j = 1; j <= table_num; j++) {
                            tablesStatus[j] = jsonObject.getInt("table_" + String.valueOf(j));
                        }

                        storeInfo = new StoreStatusInfo(store_name, table_num, tablesStatus);

                    }
                }

                int tableNum = storeInfo.getTableNum();
                int available = 0;

                activity.setTitle(storeInfo.getStoreName());

                for (int i = 1; i <= tableNum; i++) {
                    int tableStatus = storeInfo.getTablesStatus()[i];
                    String btnID = "btn_table_" + i;
                    int resID = context.getResources().getIdentifier(btnID, "id", "com.dayeong.seatmanagementsystem");
                    Button btnTable = (Button) activity.findViewById(resID);

                    if (tableStatus == 1) {
                        available += 1;
                        btnTable.setBackgroundColor(Color.RED);
                    } else if (tableStatus == 0) {
                        btnTable.setBackgroundColor(Color.GREEN);
                    } else {
                        btnTable.setBackgroundColor(Color.YELLOW);
                    }

                    btnTable.setVisibility(View.VISIBLE);
                }

                TextView tableAvailable = (TextView) activity.findViewById(R.id.table_available);
                tableAvailable.setText(available + "/" + tableNum);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "Store Name is NULL", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> getStoreNameList() {
        return storeNameList;
    }

    public ArrayList<SearchResultItem> getSearchResultItemList() {
        return searchResultItemList;
    }

    private void listSort() {
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
    }

    private double calculateDistance(double itemLatitude, double itemLongitude) {
        double theta = curLongitude - itemLongitude;
        double dist = Math.sin(deg2rad(curLatitude)) * Math.sin(deg2rad(itemLatitude)) + Math.cos(deg2rad(curLatitude)) * Math.cos(deg2rad(itemLatitude)) * Math.cos(deg2rad(theta));

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
