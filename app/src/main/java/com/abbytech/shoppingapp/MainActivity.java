package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.notification.MissedItemsRegionData;
import com.abbytech.shoppingapp.notification.NotificationData;
import com.abbytech.shoppingapp.notification.NotificationFactory;
import com.abbytech.shoppingapp.notification.OfferRegionData;
import com.abbytech.shoppingapp.notification.ZoneAlertService;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

public class MainActivity extends SupportSingleFragmentActivity {
    private static final String TAG = "test";
    final Object dialogLock = new Object();
    private AlertDialog dialog;
    private final ZoneAlertService.OnNotificationListener listener = new ZoneAlertService.OnNotificationListener() {
        @Override
        public void onNotifyMissingItems(MissedItemsRegionData missedItemsRegionData) {
            NotificationData data = NotificationFactory.createForMissedItems(missedItemsRegionData);
            createAndShowDialog(data.getTitle(), data.getBody());
        }

        @Override
        public void onNotifyOffer(OfferRegionData notification) {

        }
    };
    private NavigationFragment navigationFragment;
    private ZoneAlertService zoneAlertService;
    private final ServiceConnection notificationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            zoneAlertService = ((BeaconService.LocalBinder<ZoneAlertService>) service)
                    .getService();
            zoneAlertService.getAnnouncer().addListener(MainActivity.this.listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fragmentManager =
                navigationFragment.getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) super.onBackPressed();
        else fragmentManager.popBackStack();
    }

    @Override
    protected Fragment getFragment() {
        navigationFragment = new NavigationFragment();
        navigationFragment.setListener(fragment -> {
            if (fragment instanceof ItemActionEmitter)
                FragmentActionListenerMapper
                        .bindEmitterToListener(fragment, (ItemActionEmitter) fragment);
        });
        return navigationFragment;
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(getApplicationContext(), ZoneAlertService.class), notificationServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        zoneAlertService.getAnnouncer().removeListener(listener);
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
}
