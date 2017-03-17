package com.abbytech.shoppingapp.shop;


import android.app.Fragment;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abbytech.shoppingapp.BR;
import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.databinding.ViewShopItemBinding;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.repo.ItemRepo;
import com.abbytech.util.adapter.DataBindingRecyclerAdapter;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class ShopFragment extends Fragment {

    private ShopItemAdapter adapter;
    private ItemRepo shopRepo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_shop,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shopRepo = ShoppingApp.getInstance().getShopRepo();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.list_recycler);
        adapter = new ShopItemAdapter(null);
        recyclerView.setAdapter(adapter);
        Observable<List<Item>> listObservable = shopRepo.getAllItems();

        listObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(items -> adapter.setItemList(items));
        listObservable.doOnError(throwable -> Toast.makeText(getActivity(),"error in getting item list",
                Toast.LENGTH_SHORT).show());
    }

    class ShopItemAdapter extends DataBindingRecyclerAdapter<Item>{
        public ShopItemAdapter(List<Item> objects) {
            super(objects);
        }

        @Override
        protected ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return ViewShopItemBinding.inflate(inflater,parent,false);
        }

        @Override
        protected int getDataBindingVariableId(int position) {
            return BR.shopItem;
        }
    }
}
