package com.dayeong.seatmanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dayeong on 2016-07-24.
 */
public class GetStoreDB extends AsyncTask<String, Integer, String> {

    ArrayList<StoreInfo> storeInfoList = new ArrayList<>();
    Context context;
    Activity activity;

    public GetStoreDB(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
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
        String store_name;
        int table_num;
        int[] tablesStatus;

        try {
            JSONObject root = new JSONObject(str);
            JSONArray jsonArray = root.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                store_name = jsonObject.getString("store_name");
                table_num = jsonObject.getInt("table_num");
                tablesStatus = new int[table_num + 1];
                tablesStatus[0] = 0;

                for (int j = 1; j <= table_num; j++) {
                    tablesStatus[j] = jsonObject.getInt("table_" + String.valueOf(j));
                }
                storeInfoList.add(new StoreInfo(store_name, table_num, tablesStatus));
            }

            StoreInfo storeInfo = storeInfoList.get(0);
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
            }

            TextView tableAvailable = (TextView) activity.findViewById(R.id.table_available);
            tableAvailable.setText(available + "/" + tableNum);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
