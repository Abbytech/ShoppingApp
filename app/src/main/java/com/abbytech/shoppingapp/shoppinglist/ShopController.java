package com.abbytech.shoppingapp.shoppinglist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.shop.OnShopItemActionListener;

public class ShopController extends ActionController<Item> implements OnShopItemActionListener {
    private final ShoppingListManager shoppingListManager;

    public ShopController(Fragment fragment) {
        super(fragment);
        ShoppingListRepo shoppingListRepo = ShoppingApp.getInstance().getShoppingListRepo();
        shoppingListManager = new ShoppingListManager(shoppingListRepo,
                () -> shoppingListRepo.getShoppingList(1).toBlocking().first());
    }


    @Override
    public void onItemAction(Item item, int action, @Nullable Bundle extra) {

    }

    @Override
    public void onItemAction(Item item, @Action int action) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getFragment().getActivity());

        View view = getFragment().getActivity().getLayoutInflater().inflate(R.layout.layout_quantity_dialog, null);
        builder1.setView(view);

        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> {
                    Spinner spinner = (Spinner) view.findViewById(R.id.dialog_spinner);
                    TextView textView = (TextView) spinner.getSelectedView();
                    Long quantity = Long.parseLong(textView.getText().toString().substring(0, 1));
                    shoppingListManager.onItemAction(item, action, quantity);
                })

                .setNegativeButton(
                        "Cancell",
                        (dialog, id) -> dialog.dismiss());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
