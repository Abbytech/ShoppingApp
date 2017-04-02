package com.abbytech.shoppingapp.shop.aisles;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.abbytech.shoppingapp.FragmentActionListenerMapper;
import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.shop.OnAisleActionListener;
import com.abbytech.shoppingapp.shop.ShopFragment;

public class AislesController extends ActionController<Aisle> implements OnAisleActionListener {
    public AislesController(Fragment fragment) {
        super(fragment);
    }

    @Override
    public void onItemAction(Aisle item, @Action int action) {
        int id = getFragment().getId();
        Bundle bundle = new Bundle();
        bundle.putString(ShopFragment.EXTRA_AISLE, item.name);
        ShopFragment shopFragment = ShopFragment.createInstance(bundle);
        FragmentActionListenerMapper.bindEmitterToListener(shopFragment, shopFragment);
        getFragment()
                .getFragmentManager()
                .beginTransaction()
                .addToBackStack("aisle")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(id, shopFragment).commit();
    }
}
