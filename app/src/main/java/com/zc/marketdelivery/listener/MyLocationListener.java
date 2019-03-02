package com.zc.marketdelivery.listener;

import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

public class MyLocationListener extends BDAbstractLocationListener {
    private TextView tv;

    public MyLocationListener(TextView tv) {
        this.tv = tv;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        StringBuilder currentPosition = new StringBuilder();
        currentPosition.append(location.getDistrict());
        currentPosition.append(location.getStreet());
        currentPosition.append(location.getStreetNumber());
        tv.setText(currentPosition);
    }

}