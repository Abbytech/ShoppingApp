package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.util.ui.SingleFragmentActivity;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class MainActivity extends SingleFragmentActivity {
    private static final String TAG = "test";
    final Object dialogLock = new Object();
    private BeaconManager beaconManager;
    private AlertDialog dialog;
    private final ServiceConnection beaconServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            beaconManager = ((BeaconService.LocalBinder<BeaconService>) service)
                    .getService().getBeaconManager();
            beaconManager.addMonitorNotifier(new MonitorNotifier() {
                @Override
                public void didEnterRegion(Region region) {
                    Log.d(TAG, "didEnterRegion");
                    runOnUiThread(MainActivity.this::dismissDialog);
                }

                @Override
                public void didExitRegion(Region region) {
                    Log.d(TAG, "didExitRegion");
                    runOnUiThread(() -> createAndShowDialog(region.getUniqueId()));
                }

                @Override
                public void didDetermineStateForRegion(int i, Region region) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            beaconManager.removeAllMonitorNotifiers();
        }
    };

    @Override
    protected Fragment getFragment() {
        return new NavigationFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(getApplicationContext(), BeaconService.class), beaconServiceConn,BIND_AUTO_CREATE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unbindService(beaconServiceConn);
    }

    private void dismissDialog(){
        if (dialog!=null) {
            synchronized (dialogLock){
                dialog.dismiss();
            }
        }
    }
    private void createAndShowDialog(String title) {
        synchronized (dialogLock) {
            if (dialog!=null) dialog.dismiss();
            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(title)
                    .setMessage("You left the aisle without picking up some of your shopping items")
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.show();
        }
    }
}
