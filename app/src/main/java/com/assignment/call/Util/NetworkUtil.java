package com.assignment.call.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {

    private static ConnectivityManager connectivity;

    public static boolean isConnectionAvailable(Context context) {
        if(context != null) {
            connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED || info[i].getState() == NetworkInfo.State.CONNECTING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isServerConnected(String serverUrl) {
        try {
            HttpURLConnection urlcon = (HttpURLConnection) new URL(serverUrl)
                    .openConnection();
            if (urlcon != null) {
                try {
                    urlcon.connect();
                }
                catch (Exception e) {
                }
                if (urlcon.getResponseCode() == 200) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        catch (MalformedURLException e1) {
            return false;
        }
        catch (IOException e) {
            return false;
        }
        catch (Exception e) {
            return false;
        }
    }
}
