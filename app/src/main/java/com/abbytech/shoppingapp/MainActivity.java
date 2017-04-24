package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.notification.MissedItemsRegionData;
import com.abbytech.shoppingapp.notification.NotificationData;
import com.abbytech.shoppingapp.notification.NotificationFactory;
import com.abbytech.shoppingapp.notification.ZoneAlertService;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends SupportSingleFragmentActivity{
    private static final String TAG = "test";
    private AlertDialog dialog;
    private Subscriber<MissedItemsRegionData> missedItemsRegionDataSubscriber;
    private OnBackPressedListener onBackPressedListener;
    private ZoneAlertService zoneAlertService;
    private final ZoneAlertService.OnServiceReadyListener<ZoneAlertService> serviceReadyListener =
            service -> {
                missedItemsRegionDataSubscriber = createSubscriber();
                service
                        .getMissedItemsRegionDataObservable()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(missedItemsRegionDataSubscriber);
            };
    private final ServiceConnection notificationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            zoneAlertService = ((BeaconService.LocalBinder<ZoneAlertService>) service)
                    .getService();
            zoneAlertService.setOnServiceReadyListener(serviceReadyListener);
            if (zoneAlertService.isReady()) serviceReadyListener.onServiceReady(zoneAlertService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private boolean bound = false;

    @NonNull
    private Subscriber<MissedItemsRegionData> createSubscriber() {
        return new Subscriber<MissedItemsRegionData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MissedItemsRegionData missedItemsRegionData) {
                NotificationData data = NotificationFactory.createForMissedItems(missedItemsRegionData);
                AlertDialog dialog = createAndShowDialog(data.getTitle(), data.getBody());
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                        .setOnClickListener(v -> {
                            // TODO: 23/04/2017 add settings option for reminder time
                            zoneAlertService.muteRegion(missedItemsRegionData.getRegionStatus(),
                                    5000/*TimeUnit.MINUTES.toMillis(5)*/);
                            dialog.dismiss();
                        });
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (!onBackPressedListener.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected Fragment getFragment() {
        NavigationDrawerFragment navigationDrawerFragment = new NavigationDrawerFragment();
        Bundle args = new Bundle();
        args.putString(NavigationDrawerFragment.extraHeaderText,
                ShoppingApp.getInstance().getUsername());
        navigationDrawerFragment.setArguments(args);
        onBackPressedListener = navigationDrawerFragment;
        return navigationDrawerFragment;
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean zoneAlerts = getSharedPreferences(getString(R.string.shared_prefs_settings), MODE_PRIVATE)
                .getBoolean("ZONE_ALERTS", false);
        if (zoneAlerts) {
            Intent service = new Intent(getApplicationContext(), ZoneAlertService.class);
            bindService(service, notificationServiceConnection, BIND_AUTO_CREATE);
            getApplicationContext().startService(service);
            bound = true;
        }
    }

    @Override
    protected void onPause() {
        if (missedItemsRegionDataSubscriber != null) missedItemsRegionDataSubscriber.unsubscribe();
        if (zoneAlertService != null) {
            zoneAlertService.setOnServiceReadyListener(null);
        }
        if (bound) {
            unbindService(notificationServiceConnection);
            bound = false;
        }
        super.onPause();
    }

    private AlertDialog createAndShowDialog(String title, String message) {
            if (dialog != null) dialog.dismiss();
            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.dialog_button_mute_reminder, null)
                    .setIcon(android.R.drawable.ic_popup_reminder).create();
            dialog.show();
        return dialog;
    }

    public interface OnBackPressedListener {
        /**
         * @return true if back press is 'consumed' i.e activity should NOT call super.onBackPressed
         */
        boolean onBackPressed();
    }
}
