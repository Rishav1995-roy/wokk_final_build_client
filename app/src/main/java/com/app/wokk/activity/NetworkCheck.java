package com.app.wokk.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {
    private Context context;
    private static NetworkCheck internetConnectionDetector;
    private static Context prevContext;

    private NetworkCheck(Context context) {
        this.context = context;
    }

    public static NetworkCheck getInstant(Context context) {
        if (internetConnectionDetector == null) {
            prevContext = context;
            internetConnectionDetector = new NetworkCheck(context);
        }

        if (prevContext != context){
            internetConnectionDetector = null;
            internetConnectionDetector = new NetworkCheck(context);
        }
        prevContext = context;
        return internetConnectionDetector;
    }

    public boolean isConnectingToInternet() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null)
                    return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}
