package com.abbytech.shoppingapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.abbytech.shoppingapp.shop.ShopFragment;

import java.util.Map;

public class NavigationFragment extends Fragment {
    private static Map<Integer,Class<? extends Fragment>> fragmentMap;
    static{
        fragmentMap = new android.support.v4.util.ArrayMap<>();
        fragmentMap.put(R.id.navigation_home,ShoppingListFragment.class);
        fragmentMap.put(R.id.navigation_dashboard, ShopFragment.class);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class<? extends Fragment> fragment = fragmentMap.get(item.getItemId());
            try {
                getChildFragmentManager().beginTransaction().replace(R.id.content,fragment.newInstance()).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_navigation,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
