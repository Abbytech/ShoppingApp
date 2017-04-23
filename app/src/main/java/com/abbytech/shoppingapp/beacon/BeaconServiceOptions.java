package com.abbytech.shoppingapp.beacon;


import android.content.Context;
import android.content.res.Resources;

import com.abbytech.shoppingapp.R;

class BeaconServiceOptions {
    private long exitPeriod = 0;
    private long backgroundScanPeriod = 0;
    private String beaconLayout;
    private BeaconServiceOptions(long exitPeriod, String beaconLayout) {
        this.exitPeriod = exitPeriod;
        this.beaconLayout = beaconLayout;
    }

    static BeaconServiceOptions fromResources(Context context) {
        String beaconLayout = context.getString(R.string.beacon_layout);
        Resources resources = context.getResources();
        long exitPeriod = resources.getInteger(R.integer.exit_period);

        BeaconServiceOptions beaconServiceOptions = new BeaconServiceOptions(exitPeriod, beaconLayout);
        beaconServiceOptions.backgroundScanPeriod = resources.getInteger(R.integer.background_scan_period);
        return beaconServiceOptions;
    }

    public long getBackgroundScanPeriod() {
        return backgroundScanPeriod;
    }

    long getExitPeriod() {
        return exitPeriod;
    }

    String getBeaconLayout() {
        return beaconLayout;
    }
}
