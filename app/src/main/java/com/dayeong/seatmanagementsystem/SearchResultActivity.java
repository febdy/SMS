package com.dayeong.seatmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    GetStoreDB task;
    String url = "http://175.126.112.111/storedata.php";

    private ArrayList<SearchResultItem> searchResultItemList;
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
        longitude = bundle.getDouble("longitude");

        getSearchResultDB(searchStoreName);
        searchResultItemList = task.getSearchResultItemList();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new SearchResultAdapter(getApplicationContext(), searchResultItemList);
        mRecyclerView.setAdapter(mAdapter);

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

    private void getSearchResultDB(String searchStoreName) {
        task = new GetStoreDB(getApplicationContext(), SearchResultActivity.this, searchStoreName, latitude, longitude, "getSearch");
        task.execute(url);
    }

}
