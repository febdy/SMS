package com.dayeong.seatmanagementsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SeatAvailabilityActivity extends AppCompatActivity {

    GetStoreDB task;
    FloatingActionButton fabSecond, fabThird;
    String url = "http://175.126.112.111/appdata.php";
    boolean fabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_availability);
        Toolbar toolbarSeat = (Toolbar) findViewById(R.id.toolbar_seat);
        setSupportActionBar(toolbarSeat);

        Bundle bundle = getIntent().getExtras();
        final String storeName = bundle.getString("storeName");

        FloatingActionButton fabRefresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStoreDB(storeName);
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
        });

        getStoreDB(storeName);

    }

    public void setTitle(String title) {
        this.getSupportActionBar().setTitle(title);
    }

    private void getStoreDB(String storeName) {
        task = new GetStoreDB(getApplicationContext(), SeatAvailabilityActivity.this, storeName, "getSeat");
        task.execute(url);
    }
}

