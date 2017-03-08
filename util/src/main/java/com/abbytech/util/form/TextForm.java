package com.abbytech.util.form;

import android.view.View;
import android.widget.TextView;

import java.util.Collection;

public interface TextForm<T> extends Submittable<T> {
    T getFormData(Collection<TextView> fields);
    Collection<TextView> findAndSetFields(View view);
}
