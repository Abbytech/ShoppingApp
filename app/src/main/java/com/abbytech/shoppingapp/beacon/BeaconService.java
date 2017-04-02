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

import java.io.IOException;

public class BeaconService extends Service implements BeaconConsumer {
    private static final String TAG = "test";
    private BeaconManager beaconManager;
    private LocalBinder<BeaconService> binder = new LocalBinder<>(this);
    private Region[] regions;

    public BeaconService() {
    }

    public static void setupBeaconManager(BeaconManager beaconManager, BeaconServiceOptions options) {
        BeaconManager.setRegionExitPeriod(options.getExitPeriod());
        BeaconParser parser = new BeaconParser().setBeaconLayout(options.getBeaconLayout());
        beaconManager.getBeaconParsers().add(parser);
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

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: " + BeaconService.class.getSimpleName());
        if (regions != null) {
            removeAllRegions();
        }
        beaconManager.removeAllMonitorNotifiers();
        beaconManager.unbind(this);
    }

    private void removeAllRegions() {
        try {
            for (Region region : regions) {
                beaconManager.stopMonitoringBeaconsInRegion(region);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "onDestroy: error in removing regions", e);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "onBeaconServiceConnect: connected");
        BeaconServiceOptions beaconServiceOptions = BeaconServiceOptions.fromResources(this);
        setupBeaconManager(beaconManager, beaconServiceOptions);
        try {
            regions = RegionFactory.getRegionsFromResources(this);
            for (Region region : regions) {
                beaconManager.startMonitoringBeaconsInRegion(region);
            }
        } catch (RemoteException | IOException e) {
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
