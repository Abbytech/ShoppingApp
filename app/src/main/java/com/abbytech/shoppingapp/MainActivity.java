package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.logging.LogManager;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "test";
    private final Identifier venueID = Identifier.parse("0xfffffffefffffffe");
    private BeaconManager beaconManager;
    private final Region diaryRegion = new Region("Diary", venueID, Identifier.parse("0x00000000"), null);
    private  AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        LogManager.setVerboseLoggingEnabled(true);
        BeaconParser parser = new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-11,i:12-15,i:16-19,p:24-24");
        beaconManager.getBeaconParsers().add(parser);
        beaconManager.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect: connected");

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG, "didEnterRegion");
                MainActivity.this.runOnUiThread(() -> createAndShowDialog());
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG, "didExitRegion");
                if (dialog!=null) dialog.dismiss();
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });
        try {
            beaconManager.startMonitoringBeaconsInRegion(diaryRegion);
        } catch (RemoteException e) {
            Log.d(TAG, "onBeaconServiceConnect: ", e);
        }
    }

    private void createAndShowDialog() {
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("None shall pass")
                .setMessage("You shall not pass!")
                .setIcon(android.R.drawable.ic_dialog_alert).create();
        dialog.show();
    }
}
