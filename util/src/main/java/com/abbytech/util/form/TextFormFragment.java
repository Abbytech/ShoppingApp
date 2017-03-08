package com.abbytech.util.form;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.abbytech.util.eventform.RequestFragment;

import java.util.Collection;


/**
 * FormFragment is used as a framework for handling form input;
 * all classes extending from it are expected to error-check input
 * and notify the user of such errors,
 * it also makes serializing data easier by maintaining a list of all text fields
 * in the form.
 */
abstract public class TextFormFragment<T> extends RequestFragment<T> implements TextForm<T>, TextView.OnEditorActionListener {
    /**
     * Reference for all fields inside the fragment such as ‘Username’, ‘Password’, etc.
     */
    private Collection<TextView> fields;
    private TextFormErrorChecker checker;

    /** Subclasses should use this to add their fields to the list
     *  maintained by this inorder for error-checking and callbacks to work
     */
    public abstract Collection<TextView> findAndSetFields(View view);

    /**
     *
     * @return An error checker implementation or null.
     */
    protected abstract TextFormErrorChecker setupErrorChecker(Collection<TextView> fields);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Force inheriting subclasses to supply their own views.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    abstract public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                      Bundle savedInstanceState);

    /**Adds all the fields that were bound to the list
     * maintained by this, then sets the listener of keyboard actions (next, done, etc)
     * to this.
     * @param view The fragments view that was inflated in onCreateView
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupForm(view);
    }

    private void setupForm(View view) {
        Collection<TextView> subclassFields = findAndSetFields(view);
        if (subclassFields==null||subclassFields.isEmpty()){
            throw new IllegalStateException("Fields not returned in 'addFields' call");
        }
        fields = subclassFields;
        checker = setupErrorChecker(fields);
        setEditorActionListener();
    }

    private void setEditorActionListener() {
        for(TextView field:fields){
            field.setOnEditorActionListener(this);
        }
    }

    @Override
    /** Callback used by the Android system
     * for keyboard input actions such as enter, next, etc.
     *
     * @return true if the IME action should NOT take effect;
     * IE: IME_ACTION_NEXT does not advance to the next field.
     */
    public boolean onEditorAction(TextView field, int actionId, KeyEvent event) {
        if (checker==null)
            return false;

        //trigger onUserActionDone when there's no error
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onUserActionDone();
            return false;
        } else if (actionId == EditorInfo.IME_ACTION_NEXT) {
            return checker.hasError(field);
        }
        return false;
    }

    /**
     * Called when the user presses enter on the last field of the input fields.
     */
    private void onUserActionDone() {
        if (onPreSubmitErrorCheck())
            onSubmit(getFormData(getFields()));
    }

    private boolean onPreSubmitErrorCheck() {
        return hasError();
    }

    public boolean hasError() {
        return checker == null || checker.checkForErrors();
    }

    /** Optional button for submitting form.
     *  Calls onUserActionDone
     */
    protected void setSubmitButton(View submitButton){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUserActionDone();
            }
        });
    }

    public Collection<TextView> getFields() {
        return fields;
    }
}
