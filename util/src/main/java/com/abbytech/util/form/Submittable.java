package com.abbytech.util.form;

public interface Submittable<T> {

    /**
     * Called when the form should be submitted.
     */
    void onSubmit(T formData);
}
