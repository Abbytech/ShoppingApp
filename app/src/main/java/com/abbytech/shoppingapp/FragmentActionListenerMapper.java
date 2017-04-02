package com.abbytech.shoppingapp;


import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListController;
import com.abbytech.shoppingapp.shop.ShopFragment;
import com.abbytech.shoppingapp.shop.aisles.AislesController;
import com.abbytech.shoppingapp.shop.aisles.AislesFragment;
import com.abbytech.shoppingapp.shoppinglist.ShopController;

import java.util.Map;

public final class FragmentActionListenerMapper {
    private static final Map<Class<? extends ItemActionEmitter>, Class<? extends ActionController>>
            fragmentActionListenerMap = new ArrayMap<>();
    private static final String TAG = "Mapper";

    static {
        fragmentActionListenerMap.put(AislesFragment.class, AislesController.class);
        fragmentActionListenerMap.put(ShoppingListFragment.class, ShoppingListController.class);
        fragmentActionListenerMap.put(ShopFragment.class, ShopController.class);
    }

    public static Map<Class<? extends ItemActionEmitter>, Class<? extends ActionController>> getFragmentActionListenerMap() {
        return fragmentActionListenerMap;
    }

    public static OnItemActionListener bindEmitterToListener(Fragment fragment, ItemActionEmitter emitter) {
        Class<? extends ActionController> itemActionListener = fragmentActionListenerMap.get(emitter.getClass());
        try {
            if (itemActionListener != null) {
                OnItemActionListener instance = itemActionListener.getConstructor(Fragment.class).newInstance(fragment);
                emitter.setOnItemActionListener(instance);
                return instance;
            }
        } catch (Exception e) {
            Log.e(TAG, "getFragment: ", e);
        }
        return null;
    }
}
