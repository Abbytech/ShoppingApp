package com.abbytech.shoppingapp.util;


import android.content.Context;
import android.content.Intent;

public class ServiceUtils {
    public static void startStopService(Context context, Intent intent, boolean start) {
        if (start) context.startService(intent);
        else context.stopService(intent);
    }
}
