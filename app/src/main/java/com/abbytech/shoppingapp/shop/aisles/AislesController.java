package com.abbytech.shoppingapp.shop.aisles;


import android.support.v4.app.Fragment;

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
        ShopFragment shopFragment = new ShopFragment();
        getFragment()
                .getFragmentManager()
                .beginTransaction()
                .addToBackStack("aisle").replace(id, shopFragment).commit();
    }
}
