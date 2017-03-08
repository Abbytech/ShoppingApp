package com.abbytech.util.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
    private Context context;
    private ConnectivityManager connectivityManager;

    public Util(Context context) {
        this.context = context;
    }

    public boolean isConnectedToInternet() {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }
}
