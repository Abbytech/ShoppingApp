package com.abbytech.shoppingapp.notification;


import android.support.annotation.NonNull;

import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;

import java.util.List;

public class NotificationFactory {
    public static NotificationData createForMissedItems(MissedItemsRegionData data) {
        String title = String.format("You missed some items in %1$s section",
                data.getRegionStatus().getRegion().getUniqueId());
        List<ListItem> items = data.getItems();
        StringBuilder message = createString(items);
        return new NotificationData(title, message.toString(), data.getRegionStatus());
    }

    @NonNull
    public static StringBuilder createString(List<ListItem> items) {
        StringBuilder message = new StringBuilder();
        for (ListItem listItem : items) {
            String listItemString = String.format("%1$s x%2$d", listItem.getItem().getName(), listItem.getQuantity());
            message.append(listItemString).append("\n");
        }
        return message;
    }

    public static NotificationData createForOffer(OfferRegionData data) {
        String title = String.format("Offers nearby in %1$s section",
                data.getRegionStatus().getRegion().getUniqueId());
        StringBuilder message = new StringBuilder();
        for (Item offer : data.getOffers()) {
            String offerString = String.format("%1$s, %2$s", offer.getName(), offer.getPrice());
            message.append(offerString).append("\n");
        }
        return new NotificationData(title, message.toString(), data.getRegionStatus());
    }
}
