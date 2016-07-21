package com.dayeong.seatmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MainActivity extends AppCompatActivity implements MapView.POIItemEventListener {

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
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            MapPoint current = MapPoint.mapPointWithGeoCoord(latitude, longitude);
            mapView.setMapCenterPointAndZoomLevel(current, 0, true);

            mapView.removeAllPOIItems();
            makeCurrentMarker(mapView, current);
            makeMarker(mapView, MapPoint.mapPointWithGeoCoord(latitude + 0.001, longitude + 0.001), "HIHI");
            makeMarker(mapView, MapPoint.mapPointWithGeoCoord(latitude - 0.001, longitude - 0.0001), "BYEBYE");

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        startActivity(intent);
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
