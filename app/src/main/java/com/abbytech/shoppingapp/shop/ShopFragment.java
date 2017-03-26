package com.abbytech.shoppingapp.shop;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.repo.ItemRepo;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ShopFragment extends Fragment implements ItemActionEmitter {
    private ShopItemAdapter adapter;
    private ItemRepo shopRepo;
    private OnItemActionListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_shop, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopRepo = ShoppingApp.getInstance().getShopRepo();
    }

    public void setOnItemActionListener(OnItemActionListener listener) {
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
        Observable<List<Item>> listObservable = shopRepo.getAllItems().observeOn(AndroidSchedulers.mainThread());
        listObservable.subscribe(new Subscriber<List<Item>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(), "error in getting item list",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(List<Item> items) {
                adapter.setItemList(items);
            }
        });
    }
}
