package com.abbytech.shoppingapp;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.abbytech.shoppingapp.beacon.BeaconService;
import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.framework.ShoppingListController;
import com.abbytech.shoppingapp.model.Beacon;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.shop.aisles.AislesController;
import com.abbytech.shoppingapp.shop.aisles.AislesFragment;
import com.abbytech.util.ui.SupportSingleFragmentActivity;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends SupportSingleFragmentActivity {
    private static final String TAG = "test";
    private static final Map<Class<? extends Fragment>, Class<? extends ActionController>>
            fragmentActionListenerMap = new ArrayMap<>();

    static {
        fragmentActionListenerMap.put(AislesFragment.class, AislesController.class);
        fragmentActionListenerMap.put(ShoppingListFragment.class, ShoppingListController.class);
    }

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
                    ShoppingApp.getInstance()
                            .getLocationAPI()
                            .onExitLocation(new Beacon(region.getId2().toHexString().substring(2)))
                            .observeOn(AndroidSchedulers.mainThread())
                            .filter(listItems -> !listItems.isEmpty())
                            .subscribe(listItems -> {
                                String title = "Exited " + region.getUniqueId();
                                String message = null;
                                for (ListItem listItem : listItems) {
                                    message += listItem.getItem().getName() + "\n";
                                }
                                createAndShowDialog(title, message);
                            });
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
    private NavigationFragment navigationFragment;

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fragmentManager =
                navigationFragment.getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0)
            super.onBackPressed();
        else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    protected Fragment getFragment() {
        navigationFragment = new NavigationFragment();
        navigationFragment.setListener(fragment -> {
            Class<? extends OnItemActionListener> itemActionListener = fragmentActionListenerMap.get(fragment.getClass());
            try {
                if (itemActionListener != null) {
                    OnItemActionListener instance = itemActionListener.getConstructor(Fragment.class).newInstance(fragment);
                    ((ItemActionEmitter) fragment).setOnItemActionListener(instance);
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
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

    private void createAndShowDialog(String title, String message) {
        synchronized (dialogLock) {
            if (dialog!=null) dialog.dismiss();
            dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.show();
        }
    }
}
