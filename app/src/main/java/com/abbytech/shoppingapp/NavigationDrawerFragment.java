package com.abbytech.shoppingapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.abbytech.shoppingapp.framework.ItemActionEmitter;

public class NavigationDrawerFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, MainActivity.OnBackPressedListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar appbar = (Toolbar) view.findViewById(R.id.appbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(appbar);

        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, appbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationFragment navigationFragment = new NavigationFragment();
        navigationFragment.setListener(fragment -> {
            if (fragment instanceof ItemActionEmitter)
                FragmentActionListenerMapper
                        .bindEmitterToListener(fragment, (ItemActionEmitter) fragment);
        });
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.view_stub_navigation_drawer_container, navigationFragment, NavigationFragment.fragmentManagerTag).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        NavigationFragment navigationFragment = (NavigationFragment) getChildFragmentManager()
                .findFragmentByTag(NavigationFragment.fragmentManagerTag);
        android.support.v4.app.FragmentManager fragmentManager =
                navigationFragment.getChildFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return false;
        } else {
            fragmentManager.popBackStack();
            return true;
        }
    }
}
