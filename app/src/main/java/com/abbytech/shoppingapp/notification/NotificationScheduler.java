package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.beacon.RegionStatus;

import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;

public class NotificationScheduler implements ZoneAlertService.NotificationPolicy {
    private Map<Region, Long> missedItemsEnableTime = new HashMap<>();
    private Map<Region, Long> offerEnableTime = new HashMap<>();
    private Timer timer = new Timer();
    private List<OnRemindListener> listeners = new ArrayList<>();
    private Observable<RegionStatus> observable = Observable.create(subscriber -> {
        OnRemindListener listener = status -> subscriber.onNext(status);
        listeners.add(listener);
    });

    public NotificationScheduler() {
    }

    public Observable<RegionStatus> getRegionStatusObservable() {
        return observable;
    }

    public void muteRegion(RegionStatus status, Long mutePeriodMillis) {
        if (status.isEntered()) {
            offerEnableTime.put(status.getRegion(), mutePeriodMillis);
        } else {
            missedItemsEnableTime.put(status.getRegion(),
                    System.currentTimeMillis() + mutePeriodMillis);
        }
    }

    public void setReminder(RegionStatus status, Long timeFromNow) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (OnRemindListener onRemindListener : listeners) {
                    onRemindListener.onRemind(status);
                }
            }
        }, timeFromNow);
    }

    @Override
    public boolean shouldNotify(RegionStatus regionStatus) {
        Long enableTime;
        if (regionStatus.isEntered()) {
            enableTime = offerEnableTime.get(regionStatus.getRegion());
        } else {
            enableTime = missedItemsEnableTime.get(regionStatus.getRegion());
        }

        return enableTime == null || System.currentTimeMillis() >= enableTime;
    }

    public interface OnRemindListener {
        void onRemind(RegionStatus status);
    }
}
