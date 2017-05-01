package com.abbytech.shoppingapp.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class ListItem {
    private Long quantity;
    @Id(autoincrement = true)
    private Long id;
    @SerializedName("item_ID")
    private Long itemId;
    @ToOne(joinProperty = "itemId")
    private Item item;
    private Long shoppingListId;
    @SerializedName("checked_item")
    private boolean checked = false;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1219881867)
    private transient ListItemDao myDao;
    @Generated(hash = 1864644456)
    private transient Long item__resolvedKey;
    public ListItem(Item item,ShoppingList shoppingList) {
        this.item = item;
        this.itemId = item.getId();
        this.shoppingListId = shoppingList.getId();
    }

    @Generated(hash = 1919990123)
    public ListItem() {
    }

    public ListItem(Item item, Long quantity, ShoppingList shoppingList) {
        this.item = item;
        this.itemId = item.getId();
        this.shoppingListId = shoppingList.getId();
        setQuantity(quantity);
    }

    @Generated(hash = 974873313)
    public ListItem(Long quantity, Long id, Long itemId, Long shoppingListId,
                    boolean checked) {
        this.quantity = quantity;
        this.id = id;
        this.itemId = itemId;
        this.shoppingListId = shoppingListId;
        this.checked = checked;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(Long quantity) {
        this.quantity += quantity;
    }
    public boolean isChecked() {
        return checked;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShoppingListId() {
        return this.shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /** To-one relationship, resolved on first access. */
    @Keep
    @Generated(hash = 1430130750)
    public Item getItem() {
        Long __key = this.itemId;
        if (item==null) {
            if (item__resolvedKey == null || !item__resolvedKey.equals(__key)) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                    throw new DaoException("Entity is detached from DAO context");
                }
                ItemDao targetDao = daoSession.getItemDao();
                Item itemNew = targetDao.load(__key);
                synchronized (this) {
                    item = itemNew;
                    item__resolvedKey = __key;
                }
            }
        }
        return item;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 204118061)
    public void setItem(Item item) {
        synchronized (this) {
            this.item = item;
            itemId = item == null ? null : item.getId();
            item__resolvedKey = itemId;
        }
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ListItem && this.getId().equals(((ListItem) obj).getId());
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1124631400)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getListItemDao() : null;
    }
}
