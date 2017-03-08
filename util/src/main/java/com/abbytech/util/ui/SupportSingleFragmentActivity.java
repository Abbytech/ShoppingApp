package com.abbytech.util.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import abbytech.util.R;

public abstract class SupportSingleFragmentActivity extends AppCompatActivity {
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
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager.findFragmentById(containerViewId) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(containerViewId, getFragment())
                    .commit();
        }
    }

    protected abstract Fragment getFragment();
}
