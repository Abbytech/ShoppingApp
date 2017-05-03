package com.abbytech.shoppingapp.settings;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SeekBarPreference;
import android.util.Log;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.notification.ZoneAlertService;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(getString(R.string.shared_prefs_settings));
        preferenceManager.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        String sharedPreferencesName = preferenceManager.getSharedPreferencesName();
        Log.d(TAG, sharedPreferencesName);
        addPreferencesFromResource(R.xml.preferences_settings);
        SeekBarPreference preference = (SeekBarPreference) preferenceManager.findPreference(getString(R.string.PREFS_KEY_REMINDER_DELAY));
        preference.setMin(1);
        preference.setMax(5);
        preference.setSeekBarIncrement(1);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

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
