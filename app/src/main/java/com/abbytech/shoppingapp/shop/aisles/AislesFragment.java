package com.abbytech.shoppingapp.shop.aisles;


import android.support.v4.app.Fragment;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.databinding.LayoutSectionBinding;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.util.adapter.DataBindingRecyclerAdapter;
import com.android.databinding.library.baseAdapters.BR;

import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;
import rx.android.schedulers.AndroidSchedulers;

import static com.abbytech.shoppingapp.shop.OnAisleActionListener.ACTION_SELECTED;

public class AislesFragment extends Fragment implements ItemActionEmitter<Aisle> {

    private AisleAdapter adapter;
    private OnItemActionListener<Aisle> listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_shop_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recycler);
        RecyclerView.ItemAnimator animator = new ScaleInBottomAnimator();
        recyclerView.setItemAnimator(animator);
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setSpanCount(2);
        adapter = new AisleAdapter(null);
        setAdapterListener();
        recyclerView.setAdapter(adapter);
        AisleRepo repo = new AisleRepo(getActivity());
        repo.getAisles().observeOn(AndroidSchedulers.mainThread()).subscribe(aisles -> adapter.setItemList(aisles));
    }

    @Override
    public void setOnItemActionListener(OnItemActionListener<Aisle> listener) {
        this.listener = listener;
        if (adapter != null) setAdapterListener();
    }

    private void setAdapterListener() {
        adapter.setOnItemClickListener(item -> listener.onItemAction(item, ACTION_SELECTED));
    }

    class AisleAdapter extends DataBindingRecyclerAdapter<Aisle> {
        public AisleAdapter(List<Aisle> objects) {
            super(objects);
        }

        @Override
        protected ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return LayoutSectionBinding.inflate(inflater, parent, false);
        }

        @Override
        public void onBindViewHolder(DataBindingViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            Aisle item = getItem(position);
            ImageView view = ((LayoutSectionBinding) holder.getBinding()).imageView1;
            view.setImageDrawable(item.drawable);
        }

        @Override
        protected int getDataBindingVariableId(int position) {
            return BR.aisle;
        }
    }
}
