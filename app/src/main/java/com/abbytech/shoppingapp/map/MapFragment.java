package com.abbytech.shoppingapp.map;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.model.Section;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.notification.LocationAPI;
import com.abbytech.shoppingapp.notification.NotificationFactory;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class MapFragment extends Fragment {
    private static final String TAG = MapFragment.class.getSimpleName();
    private LocationProvider provider;
    private Map<Section, Button> sectionMap = new ArrayMap<>();
    private LocationAPI locationAPI;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_map, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new LocationProvider(getActivity());
        locationAPI = ShoppingApp.getInstance().getLocationAPI();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button dairyButton = (Button) view.findViewById(R.id.button_dairy);
        Button snacksButton = (Button) view.findViewById(R.id.button_snacks);
        sectionMap.put(new Section("00000000"), dairyButton);
        sectionMap.put(new Section("11111111"), snacksButton);
        provider.getCurrentRegionsStream()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(regions -> resetButtonsState())
                .flatMap(sections -> Observable.from(sections))
                .map(section -> sectionMap.get(section))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Button>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Button button) {
                        int color = getActivity()
                                .getResources()
                                .getColor(android.R.color.holo_green_dark);
                        button.setBackgroundColor(color);
                    }
                });

        for (Section key : sectionMap.keySet()) {
            sectionMap.get(key).setOnClickListener(v -> {
                locationAPI.onExitLocation(key)
                        .filter(listItems -> !listItems.isEmpty())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<ListItem>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<ListItem> listItems) {
                                StringBuilder string = NotificationFactory.createString(listItems);
                                AlertDialog dialog =
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Items needed in section")
                                                .setMessage(string.toString()).create();
                                dialog.show();
                            }
                        });
            });
        }
    }

    private void resetButtonsState() {
        for (Button button : sectionMap.values()) {
            button.setBackground(null);
        }
    }
}
