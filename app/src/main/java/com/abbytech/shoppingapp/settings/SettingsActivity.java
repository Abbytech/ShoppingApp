package com.abbytech.shoppingapp.settings;

import android.app.Fragment;

import com.abbytech.util.ui.SingleFragmentActivity;

public class SettingsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new SettingsFragment();
    }
}
