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

        mAdapter = new SearchResultAdapter(getApplicationContext(), searchResultItemList, latitude, longitude);
        mRecyclerView.setAdapter(mAdapter);

        makeStoreDB();
        setSearchStoreList(searchStoreName);

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

        mAdapter.notifyDataSetChanged();
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
