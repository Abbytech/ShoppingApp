package com.abbytech.shoppingapp.framework;


import android.support.v4.app.Fragment;

public abstract class ActionController<T> implements OnItemActionListener<T> {
    private Fragment fragment;

    public ActionController(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
