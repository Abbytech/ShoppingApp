package com.abbytech.shoppingapp.model;


public class ListItem {
    private Item item;

    private boolean checked = false;

    public Item getItem() {
        return item;
    }

    public boolean isChecked() {
        return checked;
    }

    public ListItem(Item item) {
        this.item = item;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
