package com.abbytech.shoppingapp.map;


import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.abbytech.shoppingapp.R;
import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.databinding.LayoutMapBinding;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.Section;
import com.abbytech.shoppingapp.notification.LocationAPI;
import com.abbytech.shoppingapp.notification.NotificationFactory;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MapFragment extends Fragment {
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final Map<Integer, Arguments> sectionDimensionsMap = new ArrayMap<>();

    static {
        sectionDimensionsMap.put(R.id.button_dairy,
                new Arguments(new Position(10, 10), R.string.section_title_dairy));
        sectionDimensionsMap.put(R.id.button_snacks,
                new Arguments(new Position(350, 400), R.string.section_title_snacks, (short) 270));
        sectionDimensionsMap.put(R.id.button_produce,
                new Arguments(new Position(400, 900), R.string.section_title_produce));
        sectionDimensionsMap.put(R.id.button_bakery,
                new Arguments(new Position(200, 1000),
                        R.string.section_title_bakery, (short) 270));
    }

    private LocationProvider provider;
    private Map<Section, View> sectionMap = new ArrayMap<>();
    private List<View> sectionViews = new ArrayList<>();
    private LocationAPI locationAPI;
    private PhotoView photoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutMapBinding mapBinding = LayoutMapBinding.inflate(inflater, container, false);
        Collection<Arguments> sectionArguments = sectionDimensionsMap.values();

        mapBinding.setSections(new ArrayList<>(sectionArguments));
        return mapBinding.getRoot();
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
        for (Integer id : sectionDimensionsMap.keySet()) {
            sectionViews.add(view.findViewById(id));
        }
        View dairyButton = view.findViewById(R.id.button_dairy);
        View snacksButton = view.findViewById(R.id.button_snacks);

        photoView = (PhotoView) view.findViewById(R.id.photoview);
        photoView.setImageResource(R.drawable.floorplan);
        photoView.setScaleType(ImageView.ScaleType.CENTER);
        photoView.setMinimumScale(0.75F);
        final PhotoViewAttacher attacher = photoView.getAttacher();
        Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        photoView.setScale(photoView.getMinimumScale());
                        attacher.setPinchDisabled(true);
                        layoutSections();
                        Log.d(TAG, "actual width=" + attacher.getActualWidth());
                        Log.d(TAG, "actual height=" + attacher.getActualHeight());
                    }
                });


        attacher.setOnMatrixChangeListener(rect -> {
            float translationX = attacher.getMatrixTransformation(Matrix.MTRANS_X);
            float translationY = attacher.getMatrixTransformation(Matrix.MTRANS_Y);
            Log.d(TAG, String.format("Translation x:%1$f,y:%2$f", translationX, translationY));
            for (View section : sectionViews) {
                section.setTranslationX(translationX);
                section.setTranslationY(translationY);
            }
        });
        attacher.setOnPhotoTapListener((view1, x, y) -> {
            for (byte i = 0; i < 9; i++) {
                float propertyX;
                propertyX = attacher.getMatrixTransformation(i);
                Log.d(TAG, String.format("Property %1$d:%2$f", i, propertyX));
            }
            Log.d(TAG, String.format("Tap x:%1$f,y:%2$f", x, y));
        });

        sectionMap.put(new Section("00000000"), dairyButton);
        sectionMap.put(new Section("11111111"), snacksButton);
        setupCurrentSectionHighlighting();
        setupButtonListeners();
        setupSectionsOfInterestHighlighting();
    }

    private void layoutSections() {
        PhotoViewAttacher attacher = photoView.getAttacher();
        Resources resources = getResources();
        for (View section : sectionViews) {
            int id = section.getId();
            Arguments arguments = sectionDimensionsMap.get(id);
            Position sectionPosition = arguments.getPosition();
            int sectionLeftDp = sectionPosition.getLeft();
            int sectionPositionLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sectionLeftDp, resources.getDisplayMetrics());
            int sectionTopDp = sectionPosition.getTop();
            int sectionPositionTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sectionTopDp, resources.getDisplayMetrics());
            int left = attacher.getActualLeft() + sectionPositionLeft;
            int top = attacher.getActualTop() + sectionPositionTop;
            ViewGroup.LayoutParams layoutParams = section.getLayoutParams();
            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(layoutParams);
            params.x = left;
            params.y = top;
            section.setLayoutParams(params);
        }
    }

    private void setupButtonListeners() {
        for (Section key : sectionMap.keySet()) {
            sectionMap.get(key).setOnClickListener(v -> {
                locationAPI.onExitLocation(key)
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

    private void setupCurrentSectionHighlighting() {
        provider.getSectionObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(regions -> resetButtonsState())
                .filter(section -> section != null)
                .map(section -> sectionMap.get(section))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<View>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onNext(View button) {
                        button.setBackgroundResource(R.drawable.border_entered);
                    }
                });
    }

    private void setupSectionsOfInterestHighlighting() {
        for (Section section : sectionMap.keySet()) {
            locationAPI.onExitLocation(section)
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
                            TextView textView = (TextView) sectionMap.get(section)
                                    .findViewById(R.id.textView_number_missing_items);
                            textView.setText(String.valueOf(listItems.size()));
                        }
                    });
        }
    }

    private void resetButtonsState() {
        for (View view : sectionMap.values()) {
            view.setBackgroundResource(R.drawable.border);
        }
    }


    public static class Arguments {
        private Position position;
        private short titleRotation;
        private int title;

        public Arguments(Position position, int title, short titleRotation) {
            this.position = position;
            this.titleRotation = titleRotation;
            this.title = title;
        }

        public Arguments(Position position, int title) {
            this.position = position;
            this.title = title;
        }

        public short getTitleRotation() {
            return titleRotation;
        }

        public void setTitleRotation(short titleRotation) {
            this.titleRotation = titleRotation;
        }

        Position getPosition() {
            return position;
        }

        public int getTitle() {
            return title;
        }
    }

    private static class Position {
        int left, top;

        public Position(int top, int left) {
            this.left = left;
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public int getTop() {
            return top;
        }

    }
}
