package com.abbytech.shoppingapp.map;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.ArraySet;
import android.util.Log;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.beacon.RegionStatus;
import com.abbytech.shoppingapp.model.Section;
import com.abbytech.util.Announcer;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Observer;

public class LocationProvider implements ServiceConnection {
    private static final String TAG = LocationProvider.class.getSimpleName();
    private final Intent serviceIntent;
    private Context context;
    private Set<Section> currentRegions = new ArraySet<>();
    private Map<Section, Collection<Beacon>> sectionBeaconsMap = new ArrayMap<>();
    private Observable<Set<Section>> currentRegionsStream;
    private Announcer<UpdateListener> updateListeners = new Announcer<>(UpdateListener.class);
    private BeaconService beaconService;
    private boolean bound = false;
    private Observable<Map<Section, Collection<Beacon>>> sectionBeaconsStream;
    private Observable<Section> sectionObservable;

    public LocationProvider(Context context) {
        this.context = context;
        serviceIntent = new Intent(context, BeaconService.class);
        createCurrentRegionsStream();
        createSectionBeaconsMapStream();
        createStrongestSectionStream();
        context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }

    public Observable<Section> getSectionObservable() {
        return sectionObservable;
    }

    private void createStrongestSectionStream() {
        sectionObservable = sectionBeaconsStream
                .filter(sectionCollectionMap ->
                        !sectionCollectionMap.isEmpty())
                .map(sectionCollectionMap -> {
                    Section strongest = sectionCollectionMap.keySet().iterator().next();
                    Beacon strongestBeacon = sectionCollectionMap.entrySet().iterator().next().getValue().iterator().next();
                    for (Section section : sectionCollectionMap.keySet()) {
                        Collection<Beacon> beacons = sectionCollectionMap.get(section);
                        for (Beacon beacon : beacons) {
                            if (beacon.getDistance() < strongestBeacon.getDistance()) {
                                strongestBeacon = beacon;
                                strongest = section;
                            }
                        }
                    }
                    Log.d(TAG, "Strongest section=" + strongest.getSectionID());
                    return strongest;
                }).mergeWith(sectionBeaconsStream
                        .filter(sectionCollectionMap -> sectionCollectionMap.isEmpty())
                        .map(sectionCollectionMap -> null));
    }

    public Observable<Map<Section, Collection<Beacon>>> getSectionBeaconsStream() {
        return sectionBeaconsStream;
    }

    private void createSectionBeaconsMapStream() {
        sectionBeaconsStream = Observable.create(subscriber ->
                updateListeners.addListener(() -> subscriber.onNext(sectionBeaconsMap)));

    }

    public Set<Section> getCurrentRegions() {
        return currentRegions;
    }

    public Observable<Set<Section>> getCurrentRegionsStream() {
        return currentRegionsStream;
    }

    private void createCurrentRegionsStream() {
        currentRegionsStream = Observable.create(subscriber -> {
            UpdateListener updateListener = () -> subscriber.onNext(currentRegions);
            updateListeners.addListener(updateListener);
        });
    }

    public void disconnectService() {
        if (beaconService != null) context.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (!bound) {
            bound = true;
            context.unbindService(this);
            context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
            return;
        }
        beaconService = ((BeaconService.LocalBinder<BeaconService>) service)
                .getService();
        beaconService.getMonitorStream().subscribe(new Observer<RegionStatus>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(RegionStatus regionStatus) {
                Log.d(TAG, "onNext: " + regionStatus.getRegion().toString());
                boolean exited = !regionStatus.isEntered();
                Section section = Section.fromRegion(regionStatus.getRegion());
                if (exited) {
                    currentRegions.remove(section);
                    sectionBeaconsMap.remove(section);
                } else currentRegions.add(section);
                updateListeners.announce().onUpdate();
            }
        });
        BeaconManager beaconManager = beaconService.getBeaconManager();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                Log.d(TAG, "didRangeBeaconsInRegion: " + region.toString());
                Section section = Section.fromRegion(region);
                if (!collection.isEmpty()) {
                    if (!currentRegions.contains(section)) {
                        currentRegions.add(section);
                    }
                    sectionBeaconsMap.put(section, collection);
                }
                updateListeners.announce().onUpdate();
            }
        });
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private interface UpdateListener {
        void onUpdate();
    }
}
