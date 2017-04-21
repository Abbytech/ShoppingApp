package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;

public class NotificationFactory {
    public static NotificationData createForMissedItems(MissedItemsRegionData data) {
        String title = String.format("You missed some items in %1$s section",
                data.getRegion().getUniqueId());
        StringBuilder message = new StringBuilder();
        for (ListItem listItem : data.getItems()) {
            message.append(listItem.getItem().getName()).append("\n");
        }
        return new NotificationData(title, message.toString());
    }

    public static NotificationData createForOffer(OfferRegionData data) {
        String title = String.format("Offers nearby in %1$s section",
                data.getRegion().getUniqueId());
        StringBuilder message = new StringBuilder();
        for (Item offer : data.getOffers()) {
            message.append(offer.getName()).append("\n");
        }
        return new NotificationData(title, message.toString());
    }
}
