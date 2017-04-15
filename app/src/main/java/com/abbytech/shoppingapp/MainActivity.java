package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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
    final Object dialogLock = new Object();
    private AlertDialog dialog;
    private final Subscriber<MissedItemsRegionData> missedItemsRegionDataSubscriber =
            new Subscriber<MissedItemsRegionData>() {
        @Override
        public void onCompleted() {

        }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(MissedItemsRegionData missedItemsRegionData) {
            NotificationData data = NotificationFactory.createForMissedItems(missedItemsRegionData);
            createAndShowDialog(data.getTitle(), data.getBody());
        }
    };
    private final ZoneAlertService.OnServiceReadyListener<ZoneAlertService> serviceReadyListener =
            service -> service
                    .getMissedItemsRegionDataObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(missedItemsRegionDataSubscriber);
    private OnBackPressedListener onBackPressedListener;
    private ZoneAlertService zoneAlertService;
    private final ServiceConnection notificationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            zoneAlertService = ((BeaconService.LocalBinder<ZoneAlertService>) service)
                    .getService();
            zoneAlertService.setOnServiceReadyListener(serviceReadyListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onBackPressed() {
        if (!onBackPressedListener.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected Fragment getFragment() {
        NavigationDrawerFragment navigationDrawerFragment = new NavigationDrawerFragment();
        onBackPressedListener = navigationDrawerFragment;
        return navigationDrawerFragment;
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent service = new Intent(getApplicationContext(), ZoneAlertService.class);
        bindService(service,notificationServiceConnection,BIND_AUTO_CREATE);
        getApplicationContext().startService(service);
    }

    @Override
    protected void onPause() {
        missedItemsRegionDataSubscriber.unsubscribe();
        if (zoneAlertService != null) zoneAlertService.setOnServiceReadyListener(null);
        unbindService(notificationServiceConnection);
        super.onPause();
    }

    private void createAndShowDialog(String title, String message) {
        synchronized (dialogLock) {
            if (dialog != null) dialog.dismiss();
            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton("Mute reminder", null)
                    .setIcon(android.R.drawable.ic_popup_reminder).create();
            dialog.show();
        }
    }

    public interface OnBackPressedListener {
        /**
         * @return true if back press is 'consumed' i.e activity should NOT call super.onBackPressed
         */
        boolean onBackPressed();
    }
}
