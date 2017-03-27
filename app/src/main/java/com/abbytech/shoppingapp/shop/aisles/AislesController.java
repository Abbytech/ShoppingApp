package com.abbytech.shoppingapp.shop.aisles;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.shop.OnAisleActionListener;
import com.abbytech.shoppingapp.shoppinglist.ShopActionFragment;

public class AislesController extends ActionController<Aisle> implements OnAisleActionListener {
    public AislesController(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onItemAction(Aisle item, @Action int action) {
        int id = getFragment().getId();
        ShopActionFragment shopFragment = new ShopActionFragment();
        getFragment()
                .getFragmentManager()
                .beginTransaction()
                .addToBackStack("aisle")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(id, shopFragment).commit();
    }
}
