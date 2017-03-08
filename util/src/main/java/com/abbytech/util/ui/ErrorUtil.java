package com.abbytech.util.ui;

import android.app.Activity;
import android.widget.Toast;

public class ErrorUtil {
    public static void finishActivityWithErrorToast(Activity activity, int stringID) {
        showErrorToast(activity, stringID);
        activity.finish();
    }

    public static void showErrorToast(Activity activity, int stringID) {
        Toast.makeText(activity, stringID, Toast.LENGTH_LONG).show();
    }
}
