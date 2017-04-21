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
import com.abbytech.shoppingapp.beacon.RegionStatus;
import com.abbytech.shoppingapp.model.Beacon;

import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import rx.Observable;
import rx.functions.Action1;

public class ZoneAlertService extends Service implements ServiceConnection {
    private static final String TAG = ZoneAlertService.class.getSimpleName();
    // TODO: 16/04/2017 Expandable notification/better display of item data
    private final Action1<NotificationData> notificationSubscriber = n -> {
        NotificationManager notificationManager =
                (NotificationManager) ZoneAlertService.this.getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(ZoneAlertService.this.getApplicationContext())
                .setContentTitle(n.getTitle())
                .setContentText(n.getBody())
                .setVibrate(new long[]{1500, 500, 1500, 500})
                .setSmallIcon(R.drawable.ic_list_black_24dp)
                .build();
        notificationManager.notify(0, notification);
    };
    private BeaconService.LocalBinder<ZoneAlertService> binder =
            new BeaconService.LocalBinder<>(this);
    private Observable<MissedItemsRegionData> missedItemsRegionDataObservable;
    private Observable<OfferRegionData> offerRegionDataObservable;
    private boolean bound = false;
    private OnServiceReadyListener<ZoneAlertService> serviceReadyListener;

    public Observable<MissedItemsRegionData> getMissedItemsRegionDataObservable() {
        return missedItemsRegionDataObservable;
    }

    public Observable<OfferRegionData> getOfferRegionDataObservable() {
        return offerRegionDataObservable;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        startForeground(1, new Notification());
        bindBeaconService();
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
        createStreams(beaconManager.getMonitorStream());
        subscribeMissedItemsNotification(missedItemsRegionDataObservable);
        subscribeOfferNotification(offerRegionDataObservable);
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
        if (serviceReadyListener != null) serviceReadyListener.onServiceReady(this);
    }

    private void subscribeOfferNotification(Observable<OfferRegionData> offerRegionDataObservable) {
        offerRegionDataObservable
                .map(NotificationFactory::createForOffer)
                .subscribe(notificationSubscriber);
    }

    private void subscribeMissedItemsNotification
            (Observable<MissedItemsRegionData> missedItemsRegionDataObservable) {
        missedItemsRegionDataObservable
                .map(NotificationFactory::createForMissedItems)
                .subscribe(notificationSubscriber);
    }

    private void createStreams(Observable<RegionStatus> monitorStream) {
        LocationAPI locationAPI = ShoppingApp.getInstance().getLocationAPI();
        missedItemsRegionDataObservable = monitorStream
                .filter(regionStatus -> !regionStatus.isEntered())
                .flatMap(regionStatus -> locationAPI
                        .onExitLocation(new Beacon(regionStatus.getRegionId()))
                        .map(listItems -> new MissedItemsRegionData(regionStatus.getRegion(), listItems)))
                .filter(missedItemsRegionData -> !missedItemsRegionData.getItems().isEmpty());

        offerRegionDataObservable = monitorStream
                .filter(RegionStatus::isEntered)
                .flatMap(regionStatus -> locationAPI.onEnterlocation(new Beacon(regionStatus.getRegionId()))
                        .map(items -> new OfferRegionData(regionStatus.getRegion(), items)))
                .filter(offerRegionData -> !offerRegionData.getOffers().isEmpty());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG, "onServiceDisconnected: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setOnServiceReadyListener(OnServiceReadyListener<ZoneAlertService> serviceReadyListener) {
        this.serviceReadyListener = serviceReadyListener;
    }

    public interface OnServiceReadyListener<T extends Service> {
        void onServiceReady(T service);
    }
}
