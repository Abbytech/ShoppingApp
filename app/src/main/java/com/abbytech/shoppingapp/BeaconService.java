package com.abbytech.shoppingapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.logging.LogManager;

public class BeaconService extends Service implements BeaconConsumer{
    private static final String TAG = "test";
    private final Identifier venueID = Identifier.parse("0xfffffffefffffffe");
    private final Region diaryRegion = new Region("Diary", venueID, Identifier.parse("0x00000000"), null);
    private BeaconManager beaconManager;
    private LocalBinder<BeaconService> binder = new LocalBinder<>(this);
    public BeaconService() {
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    @Override
    public void onCreate() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        LogManager.setVerboseLoggingEnabled(true);
        BeaconParser parser = new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-11,i:12-15,i:16-19,p:24-24");
        beaconManager.getBeaconParsers().add(parser);
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect: connected");

        try {
            beaconManager.startMonitoringBeaconsInRegion(diaryRegion);
        } catch (RemoteException e) {
            Log.e(TAG, "onBeaconServiceConnect: ", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static class LocalBinder<T extends Service> extends Binder {
        T service;
        public LocalBinder(T service) {
            this.service = service;
        }

        public T getService() {
            return service;
        }
    }
}
