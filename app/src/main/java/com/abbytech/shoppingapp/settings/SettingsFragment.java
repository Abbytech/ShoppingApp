package com.abbytech.shoppingapp.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.notification.ZoneAlertService;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_settings);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        String sharedPreferencesName = getPreferenceManager().getSharedPreferencesName();
        Log.d(TAG, sharedPreferencesName);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "ZONE_ALERTS":
                switchZoneAlertService(sharedPreferences, getActivity().getApplicationContext());
                break;
            case "OFFER_ALERTS":
                break;
            case "MISSED_ITEM_ALERTS":
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void switchZoneAlertService(SharedPreferences sharedPreferences, Context context) {
        boolean zoneAlerts = sharedPreferences.getBoolean("ZONE_ALERTS", false);
        Intent zoneAlertServiceIntent = new Intent(context, ZoneAlertService.class);
        if (zoneAlerts) context.startService(zoneAlertServiceIntent);
        else context.stopService(zoneAlertServiceIntent);
    }
}
