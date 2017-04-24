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
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.logging.LogManager;
import org.altbeacon.beacon.logging.Loggers;

import java.io.IOException;

import rx.Observable;

public class BeaconService extends Service implements BeaconConsumer {
    private static final String TAG = "BeaconService";
    private BeaconManager beaconManager;
    private LocalBinder<BeaconService> binder = new LocalBinder<>(this);
    private Region[] regions;
    private Observable<RegionStatus> monitorStream;
    public BeaconService() {
    }

    public static void setupBeaconManager(BeaconManager beaconManager, BeaconServiceOptions options) {
        BeaconManager.setRegionExitPeriod(options.getExitPeriod());
        BeaconParser parser = new BeaconParser().setBeaconLayout(options.getBeaconLayout());
        beaconManager.getBeaconParsers().add(parser);
        beaconManager.setBackgroundMode(true);
        beaconManager.setBackgroundBetweenScanPeriod(1000L);
        beaconManager.setBackgroundScanPeriod(options.getBackgroundScanPeriod());
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    public Observable<RegionStatus> getMonitorStream() {
        return monitorStream;
    }

    public void addMonitorNotifier(MonitorNotifier notifier) {
        beaconManager.addMonitorNotifier(notifier);
    }

    @Override
    public void onCreate() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        LogManager.setLogger(Loggers.verboseLogger());
        LogManager.setVerboseLoggingEnabled(true);
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG, "didEnterRegion: ");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG, "didExitRegion: ");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });
        beaconManager.bind(this);
        setupMonitorObservable();
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
        try {// TODO: 08/04/2017 load beacon information from server
            regions = RegionFactory.getRegionsFromResources(this);
            for (Region region : regions) {
                beaconManager.startMonitoringBeaconsInRegion(region);
            }
        } catch (RemoteException | IOException e) {
            Log.e(TAG, "onBeaconServiceConnect: ", e);
        }
    }

    private void setupMonitorObservable() {
        monitorStream = Observable.create(subscriber -> {
            beaconManager.addMonitorNotifier(new MonitorNotifier() {
                @Override
                public void didEnterRegion(Region region) {
                    subscriber.onNext(new RegionStatus(region, true));
                }

                @Override
                public void didExitRegion(Region region) {
                    subscriber.onNext(new RegionStatus(region, false));
                }

                @Override
                public void didDetermineStateForRegion(int i, Region region) {

                }
            });
        });
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
