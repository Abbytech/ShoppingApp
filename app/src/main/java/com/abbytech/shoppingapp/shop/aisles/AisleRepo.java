package com.abbytech.shoppingapp.shop.aisles;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.abbytech.shoppingapp.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class AisleRepo {
    private Context context;
    private List<Aisle> aisles;

    public AisleRepo(Context context) {
        this.context = context;
    }

    Observable<List<Aisle>> getAisles() {
        if (aisles == null) loadAisles();
        return Observable.just(aisles);
    }

    private void loadAisles() {
        aisles = new ArrayList<>();
        Resources resources = context.getResources();
        String[] names = resources.getStringArray(R.array.name_aisle);
        TypedArray drawables = resources.obtainTypedArray(R.array.drawable_aisle);
        for (int i = 0; i < names.length; i++) {
            Aisle aisle = new Aisle(names[i], drawables.getDrawable(i));
            aisles.add(aisle);
        }
    }
}
