package com.abbytech.shoppingapp;


import android.app.LauncherActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.abbytech.shoppingapp.databinding.ViewListItemBinding;
import com.abbytech.shoppingapp.framework.ItemActionEmitter;
import com.abbytech.shoppingapp.framework.OnItemActionListener;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ListItemDao;
import com.abbytech.shoppingapp.model.ListItemView;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.shoppinglist.OnShoppingListItemActionListener;
import com.abbytech.shoppingapp.util.ActionModeDelegate;
import com.abbytech.util.Announcer;
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

    private Announcer<OnItemActionListener> listener = new Announcer<>(OnItemActionListener.class);
    private ActionModeDelegate<ListItemView> actionModeDelegate;
    private int shoppingListId = 1;
    private double price =0;
    private double totalprice=0;

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
                            listener.announce().onItemAction(listItemView.getListItem(), ACTION_DELETE);
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
        //todo add a listener for the price calculator to 'listener'
        listener.addListener(new OnItemActionListener() {
            @Override
            public void onItemAction(Object item,@OnShoppingListItemActionListener.Action int action) {
                ListItem listItem = (ListItem) item;
               /* for(Item i :listItem)
                {
                    TextView tv=(TextView)view.findViewById(R.id.textView3);
                    String q=tv.toString();
                    double quantity= Double.parseDouble(q);
                    totalprice+=i.getPrice() * quantity;

                }*/
                //todo do something with the action
                switch (action) {
                    case OnShoppingListItemActionListener.ACTION_CHECK:
                        break;
                    case OnShoppingListItemActionListener.ACTION_DELETE:
                        break;
                    case OnShoppingListItemActionListener.ACTION_MODIFY:
                        break;
                }
            }

            @Override
            public void onItemAction(Object item, int action, @Nullable Bundle extra) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actionModeDelegate.exitActionMode();
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
        //todo tell the price thing that a new list is set
    }

    @Override
    public void setOnItemActionListener(OnItemActionListener<ListItem> listener) {
        this.listener.addListener(listener);
        if (adapter != null) setAdapterListener();
    }

    private void setAdapterListener() {
        adapter.setOnItemActionListener(listener.announce());
    }

    // TODO: 15/04/2017 Display different items in different aisles/sections
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
