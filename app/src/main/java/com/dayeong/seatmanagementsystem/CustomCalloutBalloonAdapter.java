package com.dayeong.seatmanagementsystem;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;

/**
 * Created by Dayeong on 2016-07-15.
 */
class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
    private final View mCalloutBalloon;

    public CustomCalloutBalloonAdapter(Context context) {
        mCalloutBalloon = View.inflate(context, R.layout.custom_callout_balloon, null);
    }

    @Override
    public View getCalloutBalloon(MapPOIItem poiItem) {
        ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
        ((TextView) mCalloutBalloon.findViewById(R.id.desc)).setText("자세히 보기");

        return mCalloutBalloon;
    }

    @Override
    public View getPressedCalloutBalloon(MapPOIItem poiItem) {
        return null;
    }
}