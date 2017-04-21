package com.abbytech.shoppingapp;


import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.beacon.RegionStatus;

import org.junit.Before;
import org.junit.Test;

import rx.Observable;

public class BeaconServiceTest {
    private final Object lock = new Object();
    private BeaconService beaconService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            beaconService = ((BeaconService.LocalBinder<BeaconService>) service)
                    .getService();
            beaconService.startForeground(1, new Notification());
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        Intent service = new Intent(context, BeaconService.class);
        context.bindService(service, conn, Context.BIND_AUTO_CREATE);
        synchronized (lock) {
            lock.wait();
        }
        context.unbindService(conn);
        context.bindService(service, conn, Context.BIND_AUTO_CREATE);
        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void enterThenExitRegionTriggered() throws Exception {
        Observable<RegionStatus> monitorStream = beaconService.getMonitorStream();
        monitorStream.filter(RegionStatus::isEntered).toBlocking().first();
        System.out.print("Region entered");
        monitorStream.filter(regionStatus -> !regionStatus.isEntered()).toBlocking().first();
        System.out.print("Region exited");
    }
}
