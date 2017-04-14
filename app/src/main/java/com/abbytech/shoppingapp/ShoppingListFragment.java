package com.abbytech.shoppingapp;


import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.abbytech.shoppingapp.databinding.ViewListItemBinding;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ListItemView;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.util.ActionModeDelegate;
import com.abbytech.util.adapter.DataBindingRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.abbytech.shoppingapp.shoppinglist.OnShoppingListItemActionListener.ACTION_CHECK;
import static com.abbytech.shoppingapp.shoppinglist.OnShoppingListItemActionListener.ACTION_DELETE;

public class ShoppingListFragment extends Fragment implements ItemActionEmitter<ListItem> {
    private ShoppingListAdapter adapter;
    private ShoppingList shoppingList;
    private OnItemActionListener<ListItem> listener;
    private ActionModeDelegate<ListItemView> actionModeDelegate;
    private int shoppingListId = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_recycler);
        adapter = new ShoppingListAdapter(null);
        actionModeDelegate = new ActionModeDelegate<>((AppCompatActivity) getActivity(),
                R.menu.menu_shopping_list_action_mode, new ActionModeDelegate.ActionModeListener() {
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                Observable.from(new ArrayList<>(adapter.getItems()))
                        .filter(ListItemView::isSelected)
                        .subscribe(listItemView -> {
                            adapter.remove(listItemView);
                            listener.onItemAction(listItemView.getListItem(), ACTION_DELETE);
                        });
                return true;
            }

            @Override
            public void onExitActionMode(ActionMode mode) {
                for (ListItemView listItemView : adapter.getItems())
                    listItemView.setSelected(false);
            }
        });
        adapter.setOnItemClickListener(actionModeDelegate);
        setAdapterListener();
        loadShoppingList(shoppingListId);
        recyclerView.setAdapter(adapter);
    }
    public void loadShoppingList(int id) {
        Observable<ShoppingList> shoppingListObservable = ShoppingApp.getInstance().getShoppingListRepo()
                .getShoppingList(id);
        shoppingListObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShoppingList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ShoppingApp.getInstance(), "Error while getting list", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ShoppingList shoppingList) {
                        setShoppingList(shoppingList);
                    }
                });
    }

    public void setShoppingList(ShoppingList list) {
        this.shoppingList = list;
        List<ListItem> objects = list.getItems();
        List<ListItemView> listItemViews = new ArrayList<>(objects.size());
        for (ListItem object : objects) {
            listItemViews.add(new ListItemView(object));
        }
        if (adapter != null) adapter.setItemList(listItemViews);
    }

    @Override
    public void setOnItemActionListener(OnItemActionListener<ListItem> listener) {
        this.listener = listener;
        if (adapter != null) setAdapterListener();
    }

    private void setAdapterListener() {
        adapter.setOnItemActionListener(listener);
    }

    class ShoppingListAdapter extends DataBindingRecyclerAdapter<ListItemView> implements ItemActionEmitter<ListItem> {

        private OnItemActionListener<ListItem> listener;

        public ShoppingListAdapter(List<ListItemView> objects) {
            super(objects);
        }

        @Override
        protected ViewDataBinding onCreateBinding(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return ViewListItemBinding.inflate(inflater, parent, false);
        }

        @Override
        protected void onViewHolderCreated(DataBindingViewHolder holder) {
            super.onViewHolderCreated(holder);
            View itemView = holder.itemView;
            CheckBox checkbox = (CheckBox) itemView.findViewById(R.id.checkbox_item);
            checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ListItemView listItemView = getItem(position);
                    ListItem listItemData = listItemView.getListItem();
                    if (listItemData.isChecked() != isChecked) {
                        listItemData.setChecked(isChecked);
                        set(position, listItemView);
                    }
                    if (listener != null) listener.onItemAction(listItemData, ACTION_CHECK);
                }
            });
            itemView.setOnLongClickListener(v -> {
                actionModeDelegate.onLongClick(v);
                itemView.callOnClick();
                return true;
            });
        }

        @Override
        protected int getDataBindingVariableId(int position) {
            return BR.listItemView;
        }

        @Override
        public void setOnItemActionListener(OnItemActionListener<ListItem> listener) {
            this.listener = listener;
        }
    }
}
