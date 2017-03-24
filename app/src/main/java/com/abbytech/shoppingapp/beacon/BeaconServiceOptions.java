package com.abbytech.shoppingapp.beacon;


import android.content.Context;

import com.abbytech.shoppingapp.R;

public class BeaconServiceOptions {
    private long exitPeriod = 0;

    private String beaconLayout;

    private BeaconServiceOptions(long exitPeriod, String beaconLayout) {
        this.exitPeriod = exitPeriod;
        this.beaconLayout = beaconLayout;
    }

    public static BeaconServiceOptions fromResources(Context context) {
        String beaconLayout = context.getString(R.string.beacon_layout);
        long exitPeriod = context.getResources().getInteger(R.integer.exit_period);
        return new BeaconServiceOptions(exitPeriod, beaconLayout);
    }

    public long getExitPeriod() {
        return exitPeriod;
    }

    public String getBeaconLayout() {
        return beaconLayout;
    }
}
