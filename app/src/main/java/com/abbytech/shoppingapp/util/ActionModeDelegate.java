package com.abbytech.shoppingapp.util;


import android.support.annotation.MenuRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abbytech.util.adapter.RecyclerAdapter;

public class ActionModeDelegate<T extends ActionModeDelegate.Selectable> implements View.OnLongClickListener, RecyclerAdapter.OnItemClickListener<T>, ActionMode.Callback {
    private AppCompatActivity activity;
    private int menuRes;
    private ActionModeListener listener;
    private boolean isInActionMode = false;
    private ActionMode mode;

    public ActionModeDelegate(AppCompatActivity activity, @MenuRes int menuRes, ActionModeListener listener) {
        this.activity = activity;
        this.menuRes = menuRes;
        this.listener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        this.mode = mode;
        mode.getMenuInflater().inflate(menuRes, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        boolean b = listener.onActionItemClicked(mode, item);
        mode.finish();
        return b;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        listener.onExitActionMode(mode);
        isInActionMode = false;
    }

    @Override
    public void onItemClicked(T item) {
        if (isInActionMode) item.setSelected(!item.isSelected());
    }

    @Override
    public boolean onLongClick(View v) {
        if (!isInActionMode) {
            activity.startSupportActionMode(this);
            isInActionMode = true;
        }
        return true;
    }

    public void exitActionMode() {
        if (mode != null) mode.finish();
    }
    public interface Selectable {
        boolean isSelected();

        void setSelected(boolean selected);
    }

    public interface ActionModeListener {
        boolean onActionItemClicked(ActionMode mode, MenuItem item);

        void onExitActionMode(ActionMode mode);
    }
}
