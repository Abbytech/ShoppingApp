package com.abbytech.shoppingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.abbytech.shoppingapp.map.MapFragment;
import com.abbytech.shoppingapp.shop.SearchFragment;
import com.abbytech.shoppingapp.shop.aisles.AislesFragment;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class NavigationFragment extends Fragment {
    static final String fragmentManagerTag = "NavigationFragment";
    private static Map<Integer,Class<? extends Fragment>> fragmentMap;
    static{
        fragmentMap = new android.support.v4.util.ArrayMap<>();
        fragmentMap.put(R.id.navigation_shopping_list,ShoppingListFragment.class);
        fragmentMap.put(R.id.navigation_shop, AislesFragment.class);
        fragmentMap.put(R.id.navigation_map, MapFragment.class);
    }

    private OnFragmentNavigateListener listener;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getChildFragmentManager();
            while (fragmentManager.getBackStackEntryCount() != 0)
                fragmentManager.popBackStackImmediate();
            Class<? extends Fragment> fragment = fragmentMap.get(item.getItemId());
            try {
                Fragment instance = fragment.newInstance();
                getChildFragmentManager().beginTransaction().replace(R.id.content, instance).commit();
                if (listener != null) listener.onFragmentNavigated(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };

    public void setListener(OnFragmentNavigateListener listener) {
        this.listener = listener;
    }

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
        MenuItem item = navigation.getMenu().findItem(R.id.navigation_shopping_list);
        mOnNavigationItemSelectedListener.onNavigationItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        Observable<String> queryObservable = Observable.create(subscriber -> searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subscriber.onNext(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                subscriber.onNext(newText);
                return true;
            }
        }));
        queryObservable.debounce(1, TimeUnit.SECONDS).subscribe(query -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            Fragment fragment = fragmentManager.findFragmentById(R.id.content);
            if (!(fragment instanceof SearchFragment)) {
                fragment = new SearchFragment();
                fragmentManager
                        .beginTransaction()
                        .addToBackStack("search")
                        .replace(R.id.content, fragment).commit();
                listener.onFragmentNavigated(fragment);
            }
            SearchFragment searchFragment = (SearchFragment) fragment;
            searchFragment.setQuery(query);
        });
    }

    public interface OnFragmentNavigateListener {
        void onFragmentNavigated(Fragment fragment);
    }
}
