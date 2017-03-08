package com.abbytech.util.form;

import android.widget.TextView;

import java.util.Collection;

public abstract class TextFormErrorChecker {
    private Collection<TextView> fields;

    public TextFormErrorChecker(Collection<TextView> fields) {
        if (fields.isEmpty())
            throw new IllegalArgumentException("fields cannot be empty or null");
        this.fields = fields;
    }

    public static TextFormErrorChecker getDefaultErrorChecker(Collection<TextView> fields) {
        return new TextFormErrorChecker(fields) {
            @Override
            protected String checkFieldForError(TextView field) {
                if (field.getText().toString().trim().length() == 0)
                    return "Field is empty";

                return null;
            }
        };
    }

    public void setFields(Collection<TextView> fields) {
        this.fields = fields;
    }

    /** Iterates through all fields and checks them for errors;
     * the definition of an ‘error’ is to be set by the inheriting subclass.
     *  this also displays input errors.
     *  Request focus scrolls the window to the field which has an error.
     *
     * @return true if no error was found, false otherwise.
     */
    public boolean checkForErrors() {
        if (fields==null)
            return true;

        for (TextView field : fields) {
            //if field has error (returns false)
            if (hasError(field)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasError(TextView field){
        String error = checkFieldForError(field);
        boolean hasError = error != null;
        if (hasError){
            onFieldError(field,error);
        }
        return hasError;
    }

    /** Implementing classes are expected to provide their
     *  own logic on whether a fields input is valid.
     *
     * @param field The field that we want checked for errors.
     * @return String describing the error, null otherwise.
     */
    abstract protected String checkFieldForError(TextView field);

    protected void onFieldError(TextView field,String error) {
        field.setError(error);
    }
}
