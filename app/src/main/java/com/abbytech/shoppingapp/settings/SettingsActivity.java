package com.abbytech.shoppingapp.settings;


import android.support.v4.app.Fragment;

import com.abbytech.util.ui.SupportSingleFragmentActivity;

public class SettingsActivity extends SupportSingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return new SettingsFragment();
    }
}
