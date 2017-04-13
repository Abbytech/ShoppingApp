package com.abbytech.shoppingapp.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.model.Beacon;
import com.abbytech.util.Announcer;

import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ZoneAlertService extends Service implements ServiceConnection {
    private static final String TAG = ZoneAlertService.class.getSimpleName();
    private BeaconService.LocalBinder<ZoneAlertService> binder =
            new BeaconService.LocalBinder<>(this);
    private Announcer<OnNotificationListener> announcer = Announcer.to(OnNotificationListener.class);
    private Observable<MissedItemsRegionData> missedItemsRegionDataObservable = Observable.create(subscriber -> {
        announcer.addListener(new OnNotificationListener() {
            @Override
            public void onNotifyMissingItems(MissedItemsRegionData notification) {
                subscriber.onNext(notification);
            }

            @Override
            public void onNotifyOffer(OfferRegionData notification) {

            }
        });
    });
    private boolean bound = false;
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        startForeground(1,new Notification());
        bindBeaconService();
        missedItemsRegionDataObservable
                .map(NotificationFactory::createForMissedItems)
                .subscribe(n -> {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    Notification notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle(n.getTitle())
                            .setContentText(n.getBody())
                            .setVibrate(new long[]{1500, 500, 1500, 500})
                            .setSmallIcon(R.drawable.ic_list_black_24dp)
                            .build();
                    notificationManager.notify(0, notification);
                });
    }

    private void bindBeaconService() {
        Intent service = new Intent(getApplicationContext(), BeaconService.class);
        bindService(service, this, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        unbindService(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected: ");
        //currently this is needed due to a bug in the AltBeacon library.
        if (!bound) {
            unbindService(this);
            bindBeaconService();
        }
        bound = true;
        BeaconService beaconManager = ((BeaconService.LocalBinder<BeaconService>) service)
                .getService();
        beaconManager.getMonitorStream()
                .filter(regionStatus -> !regionStatus.isEntered())
                .flatMap(regionStatus -> ShoppingApp.getInstance()
                        .getLocationAPI()
                        .onExitLocation(new Beacon(regionStatus.getRegion().getId2().toHexString().substring(2)))
                        .map(listItems -> new MissedItemsRegionData(regionStatus.getRegion(), listItems)))
                .filter(missedItemsRegionData -> !missedItemsRegionData.getItems().isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MissedItemsRegionData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MissedItemsRegionData missedItemsRegionData) {
                        Log.d(TAG, "Received list of unchecked items; displaying dialog");
                        notifyMissingItems(missedItemsRegionData);
                    }
                });
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG, "didEnterRegion");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG, "didExitRegion:" + region.toString());
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });
    }

    private void notifyMissingItems(MissedItemsRegionData missedItemsRegionData) {
        Log.d(TAG, "notifyMissingItems: ");
        announcer.announce().onNotifyMissingItems(missedItemsRegionData);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public Announcer<OnNotificationListener> getAnnouncer() {
        return announcer;
    }

    public interface OnNotificationListener {
        void onNotifyMissingItems(MissedItemsRegionData notification);

        void onNotifyOffer(OfferRegionData notification);
    }
}
