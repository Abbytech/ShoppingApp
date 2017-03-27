package com.abbytech.shoppingapp;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.beacon.RegionFactory;

import junit.framework.AssertionFailedError;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.junit.Before;
import org.junit.Test;

public class BeaconTest {

    private final Object serviceLock = new Object();
    private BeaconManager beaconManager;
    private Region testRegion = RegionFactory.createRegion("Bakery", "0x00000000");

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.bindService(new Intent(context, BeaconService.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BeaconService beaconService = ((BeaconService.LocalBinder<BeaconService>) service).getService();
                beaconManager = beaconService.getBeaconManager();
                try {
                    beaconManager.startRangingBeaconsInRegion(testRegion);
                    beaconManager.startMonitoringBeaconsInRegion(testRegion);
                } catch (RemoteException ignored) {

                }
                synchronized (serviceLock) {
                    serviceLock.notify();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
        synchronized (serviceLock) {
            serviceLock.wait();
        }
    }

    @Test
    public void detectsBeacon() {
        Object beaconLock = new Object();
        beaconManager.addRangeNotifier((collection, region) -> {
            System.out.print("beacon detected" + region.toString());
            synchronized (beaconLock) {
                beaconLock.notify();
            }
        });
        synchronized (beaconLock) {
            try {
                beaconLock.wait(10000);
                throw new AssertionFailedError("beacon not detected within 10 seconds");
            } catch (InterruptedException e) {
                //beacon was detected
                System.out.print("beacon detected");
            }
        }
    }
}
