package com.abbytech.shoppingapp.beacon;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.logging.LogManager;

public class BeaconService extends Service implements BeaconConsumer{
    private static final String TAG = "test";
    private final Region diaryRegion = RegionFactory.createRegion("Dairy","0x00000000");
    private final Region bakeryRegion = RegionFactory.createRegion("Bakery","0x11111111");
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
        beaconManager.bind(this);
    }

    public static void setupBeaconManager(BeaconManager beaconManager){
        BeaconManager.setRegionExitPeriod(1000);
        BeaconParser parser = new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-11,i:12-15,i:16-19,p:24-24");
        beaconManager.getBeaconParsers().add(parser);
    }
    @Override
    public void onDestroy() {
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect: connected");
        setupBeaconManager(beaconManager);
        try {
            beaconManager.startMonitoringBeaconsInRegion(diaryRegion);
            beaconManager.startMonitoringBeaconsInRegion(bakeryRegion);
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
