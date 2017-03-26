package com.abbytech.shoppingapp.shoppinglist;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.shop.ShopFragment;

public class ShopActionFragment extends Fragment {
    private ShoppingListManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_single_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShopFragment shopFragment = new ShopFragment();
        ShoppingListRepo shoppingListRepo = ShoppingApp.getInstance().getShoppingListRepo();
        manager = new ShoppingListManager(shopFragment, shoppingListRepo,
                () -> shoppingListRepo.getShoppingList(1).toBlocking().first());
        getChildFragmentManager().beginTransaction().replace(R.id.fragment, shopFragment).commit();
    }
}
