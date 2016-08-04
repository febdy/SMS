package com.dayeong.seatmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MapView.POIItemEventListener {
    GetStoreDB task;
    String url = "http://175.126.112.111/storedata.php";

    ArrayList<String> storeNameList;
    String storeName;
    double latitude = 0;
    double longitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MapView mapView = new MapView(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_gps);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGps(mapView);
            }
        });

        getIndex();
        storeNameList = task.getStoreNameList();

        mapView.setDaumMapApiKey("40835261670c49406a6124ee35c9cba8");
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapView.setPOIItemEventListener(this);
        mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter(getApplicationContext()));
        getGps(mapView);
        mapViewContainer.addView(mapView);

    }

    public void getGps(MapView mapView) {
        GPSInfo gps = new GPSInfo(MainActivity.this);

        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            MapPoint current = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            mapView.setMapCenterPointAndZoomLevel(current, 0, true);

            mapView.removeAllPOIItems();
            makeCurrentMarker(mapView, current);
            makeMarker(mapView, MapPoint.mapPointWithGeoCoord(latitude + 0.001, longitude + 0.001), "test1");
            makeMarker(mapView, MapPoint.mapPointWithGeoCoord(latitude - 0.001, longitude - 0.0001), "test2");

        } else {
            gps.showSettingsAlert();
        }
    }

    public void makeCurrentMarker(MapView mapView, MapPoint mapPoint) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("현재위치");
        marker.setTag(0);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        marker.setShowCalloutBalloonOnTouch(false);

        mapView.addPOIItem(marker);
    }

    public void makeMarker(MapView mapView, MapPoint mapPoint, String name) {
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(name);
        marker.setTag(1);
        marker.setMapPoint(mapPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(marker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final ArrayAdapterSearchView searchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(searchItem);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, storeNameList);
        searchView.setAdapter(adapter);

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchString = (String) parent.getItemAtPosition(position);
                searchView.setText(searchString.toString()); // TODO: 커서위치 뒤로
                storeName = searchString;
                startSearchResultActivity(storeName);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                startSearchResultActivity(searchWord);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        Intent intent = new Intent(MainActivity.this, SeatAvailabilityActivity.class);
        intent.putExtra("storeName", mapPOIItem.getItemName());
        startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    private void startSearchResultActivity(String storeName) {
        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
        intent.putExtra("storeName", storeName);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }

    private void getIndex() {
        task = new GetStoreDB(getApplicationContext(), MainActivity.this, "getIndex");
        task.execute(url);
    }
}
