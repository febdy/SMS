package com.dayeong.seatmanagementsystem;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class SeatAvailabilityActivity extends AppCompatActivity {

    TextView txtView;
    phpDown task;
    ArrayList<StoreInfo> storeInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        task = new phpDown();
        txtView = (TextView) findViewById(R.id.txtView);

        task.execute("http://175.126.112.111/appdata.php");
    }


    private class phpDown extends AsyncTask<String, Integer, String> {
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
            int table_num = 1;
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

                getSupportActionBar().setTitle(storeInfo.getStoreName());

                for (int i = 1; i <= storeInfo.getTableNum(); i++) {
                    if (storeInfo.getTablesStatus()[i] == 1) {
                        String btnID = "btn_table_" + i;
                        int resID = getResources().getIdentifier(btnID, "id", "com.dayeong.seatmanagementsystem");
                        Button btnTable = (Button) findViewById(resID);
                        btnTable.setBackgroundColor(Color.MAGENTA);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}

