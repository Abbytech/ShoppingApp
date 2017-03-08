package com.abbytech.util.ui;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import abbytech.util.R;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public abstract class SingleFragmentActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        setupFragment();
    }

    /**
     * Layout should contain a container for the fragment.with id 'fragment'
     */
    protected void setLayout() {
        setContentView(R.layout.activity_single_fragment);
    }

    private void setupFragment() {
        int containerViewId = R.id.fragment;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.findFragmentById(containerViewId) == null) {
            fragmentManager
                    .beginTransaction()
                    .add(containerViewId, getFragment())
                    .commit();
        }
    }

    protected abstract Fragment getFragment();
}
