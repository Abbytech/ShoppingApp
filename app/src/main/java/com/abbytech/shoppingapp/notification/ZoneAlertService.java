package com.abbytech.shoppingapp.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.beacon.RegionStatus;
import com.abbytech.shoppingapp.model.Section;

import org.altbeacon.beacon.MonitorNotifier;

import rx.Observable;
import rx.Observer;

public class ZoneAlertService extends Service implements ServiceConnection {
    private static final String TAG = ZoneAlertService.class.getSimpleName();
    private final Observer<NotificationData> notificationSubscriber = new Observer<NotificationData>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
        }

        @Override
        public void onNext(NotificationData n) {
            NotificationManager notificationManager =
                    (NotificationManager) ZoneAlertService.this.getSystemService(NOTIFICATION_SERVICE);

            String[] items = n.getBody().split("\n");
            String contentText = String.format("%1$d items", items.length);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(ZoneAlertService.this.getApplicationContext());
            builder.setContentTitle(n.getTitle())
                    .setContentText(contentText)
                    .setVibrate(new long[]{0, 500, 250, 500})
                    .setSmallIcon(R.drawable.ic_logo);
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            inboxStyle.setSummaryText(contentText);
            for (String item : items) inboxStyle.addLine(item);
            builder.setStyle(inboxStyle);

            Notification notification = builder.build();
            notificationManager.notify(n.hashCode(), notification);
        }
    };
    private BeaconService.LocalBinder<ZoneAlertService> binder =
            new BeaconService.LocalBinder<>(this);
    private Observable<MissedItemsRegionData> missedItemsRegionDataObservable;
    private Observable<OfferRegionData> offerRegionDataObservable;
    private boolean bound = false;
    private boolean ready = false;
    private OnServiceReadyListener<ZoneAlertService> serviceReadyListener;
    private NotificationPolicy missedItemsPolicy;
    private NotificationPolicy offerPolicy;
    private NotificationScheduler notificationScheduler;
    private boolean missedItemsEnabled = true;
    private boolean offerEnabled = true;
    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    switch (key) {
                        case "OFFER_ALERTS":
                            boolean offerAlerts = sharedPreferences
                                    .getBoolean("OFFER_ALERTS", true);
                            setOfferStreamEnabled(offerAlerts);
                            break;
                        case "MISSED_ITEM_ALERTS":
                            boolean missedItemAlerts = sharedPreferences
                                    .getBoolean("MISSED_ITEM_ALERTS", true);
                            setMissedItemsStreamEnabled(missedItemAlerts);
                            break;
                    }
                }
            };

    public boolean isReady() {
        return ready;
    }

    public void muteRegion(RegionStatus status, long timeMillis) {
        notificationScheduler.muteRegion(status, timeMillis);
        notificationScheduler.setReminder(status, timeMillis);
    }

    private void setMissedItemsPolicy(NotificationPolicy missedItemsPolicy) {
        this.missedItemsPolicy = missedItemsPolicy;
    }

    private void setOfferPolicy(NotificationPolicy offerPolicy) {
        this.offerPolicy = offerPolicy;
    }

    public void setOfferStreamEnabled(boolean enabled) {
        offerEnabled = enabled;
        Log.d(TAG, "setOfferStreamEnabled: " + enabled);
    }

    public void setMissedItemsStreamEnabled(boolean enabled) {
        missedItemsEnabled = enabled;
        Log.d(TAG, "setMissedItemsStreamEnabled: " + enabled);
    }
    public Observable<MissedItemsRegionData> getMissedItemsRegionDataObservable() {
        return missedItemsRegionDataObservable;
    }

    public Observable<OfferRegionData> getOfferRegionDataObservable() {
        return offerRegionDataObservable;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        bindBeaconService();
        notificationScheduler = new NotificationScheduler();
        setMissedItemsPolicy(notificationScheduler);
        setOfferPolicy(notificationScheduler);
        setupSettings();
    }

    private void setupSettings() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.shared_prefs_settings), MODE_PRIVATE);
        Log.d(TAG, "preferences name " + prefs.toString());
        boolean offerAlerts = prefs.getBoolean("OFFER_ALERTS", false);
        boolean missedItemAlerts = prefs.getBoolean("MISSED_ITEM_ALERTS", false);
        setOfferStreamEnabled(offerAlerts);
        setMissedItemsStreamEnabled(missedItemAlerts);
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
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
            bound = true;
            return;
        }
        BeaconService beaconManager = ((BeaconService.LocalBinder<BeaconService>) service)
                .getService();
        createStreams(beaconManager.getMonitorStream());
        subscribeMissedItemsNotification(missedItemsRegionDataObservable);
        subscribeOfferNotification(offerRegionDataObservable);
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(org.altbeacon.beacon.Region region) {
                Log.d(TAG, "didEnterRegion");
            }

            @Override
            public void didExitRegion(org.altbeacon.beacon.Region region) {
                Log.d(TAG, "didExitRegion:" + region.toString());
            }

            @Override
            public void didDetermineStateForRegion(int i, org.altbeacon.beacon.Region region) {

            }
        });
        if (serviceReadyListener != null) serviceReadyListener.onServiceReady(this);
        ready = true;
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
                .filter(regionStatus -> missedItemsEnabled)
                .mergeWith(notificationScheduler.getRegionStatusObservable())
                .filter(regionStatus -> !regionStatus.isEntered())
                .filter(regionStatus -> {
                    boolean b;
                    b = missedItemsPolicy == null || missedItemsPolicy.shouldNotify(regionStatus);
                    return b;
                })
                .flatMap(regionStatus -> locationAPI
                        .onExitLocation(new Section(regionStatus.getRegionId()))
                        .map(listItems -> new MissedItemsRegionData(regionStatus, listItems)))
                .filter(missedItemsRegionData -> !missedItemsRegionData.getItems().isEmpty());

        offerRegionDataObservable = monitorStream
                .filter(regionStatus -> offerEnabled)
                .mergeWith(notificationScheduler.getRegionStatusObservable())
                .filter(RegionStatus::isEntered)
                .filter(regionStatus -> {
                    boolean b;
                    b = offerPolicy == null || offerPolicy.shouldNotify(regionStatus);
                    return b;
                })
                .flatMap(regionStatus -> locationAPI.onEnterlocation(new Section(regionStatus.getRegionId()))
                        .map(items -> new OfferRegionData(regionStatus, items)))
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

    public interface NotificationPolicy {
        boolean shouldNotify(RegionStatus regionStatus);
    }

    public interface OnServiceReadyListener<T extends Service> {
        void onServiceReady(T service);
    }
}
