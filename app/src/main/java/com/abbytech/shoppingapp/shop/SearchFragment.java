package com.abbytech.shoppingapp.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.Item;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by USER on 4/11/2017.
 */

public class SearchFragment extends Fragment implements ItemActionEmitter<Item> {
    private ShopItemAdapter adapter;
    private ShopRepo shopRepo;
    private OnItemActionListener<Item> listener;

    public void setQuery(String query) {
        if (adapter != null) {
            Observable<List<Item>> listObservable = shopRepo.getItemSearch(query)
                    .observeOn(AndroidSchedulers.mainThread());
            listObservable.subscribe(new Subscriber<List<Item>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(List<Item> items) {
                    if (items.isEmpty()) {
                        Toast.makeText(ShoppingApp.getInstance(), "Item not found !!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        adapter.setItemList(items);
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_shop, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopRepo = ShoppingApp.getInstance().getShopRepo();
    }

    public void setOnItemActionListener(OnItemActionListener<Item> listener) {
        this.listener = listener;
        if (adapter != null) adapter.setOnItemActionListener(this.listener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recycler);
        adapter = new ShopItemAdapter(null);
        adapter.setOnItemActionListener(listener);
        recyclerView.setAdapter(adapter);
    }
}
