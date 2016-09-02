package com.dayeong.seatmanagementsystem;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SeatAvailabilityActivity extends AppCompatActivity implements AsyncResponse {

    GetStoreDB task;
    // FloatingActionButton fabSecond, fabThird;
    String url = "http://175.126.112.111/tablestatus.php";
    // boolean fabOpen = false;
    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        Toolbar toolbarSeat = (Toolbar) findViewById(R.id.toolbar_seat);
        setSupportActionBar(toolbarSeat);

        Bundle bundle = getIntent().getExtras();
        storeName = bundle.getString("storeName");

    /*    FloatingActionButton fabRefresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeDB();
            }
        });


      fabSecond = (FloatingActionButton) findViewById(R.id.fab_second);
        fabThird = (FloatingActionButton) findViewById(R.id.fab_third);

        FloatingActionButton fabFloor = (FloatingActionButton) findViewById(R.id.fab_Floor);
        fabFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabOpen == false) {
                    fabSecond.setVisibility(View.VISIBLE);
                    fabThird.setVisibility(View.VISIBLE);

                    fabSecond.setClickable(true);
                    fabThird.setClickable(true);
                } else {
                    fabSecond.setVisibility(View.INVISIBLE);
                    fabThird.setVisibility(View.INVISIBLE);

                    fabSecond.setClickable(false);
                    fabThird.setClickable(false);
                }

                fabOpen = !fabOpen;
            }
        }); */

        executeDB();

    }

    public void setTitle(String title) {
        this.getSupportActionBar().setTitle(title);
    }

    private void executeDB() {
        task = new GetStoreDB(getApplicationContext(), SeatAvailabilityActivity.this);
        task.listener = this;
        task.execute(url);
    }

    @Override
    public void processFinish(String str) {
        String store_name;
        int table_num;
        int[] tablesStatus;
        StoreStatusInfo storeInfo = new StoreStatusInfo();

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

                setTitle(storeInfo.getStoreName());

                for (int i = 1; i <= tableNum; i++) {
                    int tableStatus = storeInfo.getTablesStatus()[i];
                    String btnID = "btn_table_" + i;
                    int resID = getApplicationContext().getResources().getIdentifier(btnID, "id", "com.dayeong.seatmanagementsystem");
                    Button btnTable = (Button) findViewById(resID);

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

                TextView tableAvailable = (TextView) findViewById(R.id.table_available);
                tableAvailable.setText(available + "/" + tableNum);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Store Name is NULL", Toast.LENGTH_SHORT).show();
        }
    }
}

