package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.beacon.RegionNotification;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.model.Beacon;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends SupportSingleFragmentActivity {
    private static final String TAG = "test";
    final Object dialogLock = new Object();
    private AlertDialog dialog;
    private final ServiceConnection beaconServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BeaconService beaconManager = ((BeaconService.LocalBinder<BeaconService>) service)
                    .getService();
            beaconManager.getMonitorStream()
                    .filter(regionStatus -> !regionStatus.isEntered())
                    .flatMap(regionStatus -> ShoppingApp.getInstance()
                            .getLocationAPI()
                            .onExitLocation(new Beacon(regionStatus.getRegion().getId2().toHexString().substring(2)))
                            .map(listItems -> new RegionNotification(regionStatus.getRegion(), listItems)))
                    .filter(regionNotification -> !regionNotification.getItems().isEmpty())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RegionNotification>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(RegionNotification regionNotification) {
                            Log.d(TAG, "Received list of unchecked items; displaying dialog");
                            String title = String.format("You missed some items in %1$s section",
                                    regionNotification.getRegion().getUniqueId());
                            String message = "";
                            for (ListItem listItem : regionNotification.getItems()) {
                                message += listItem.getItem().getName() + "\n";
                            }
                            createAndShowDialog(title, message);
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

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    private NavigationFragment navigationFragment;

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
        bindService(new Intent(getApplicationContext(), BeaconService.class), beaconServiceConn, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        unbindService(beaconServiceConn);
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
