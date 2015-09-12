package com.plusconnect.Utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.plusconnect.chat.R;

/**
 * Created by ambesh on 09-09-2015.
 */
public class PlusConnectUtils {



    public static void  logException(Exception e){
        e.printStackTrace();
    }


    /**
     * Checks if is network available.
     *
     *
     * @param ctx
     *            the ctx
     *
     * @param toShowDialog if set to true a default Network Not avaliable dialog is shown
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Activity ctx,boolean toShowDialog) {
        boolean isNetwokAvailable = false;
        ConnectivityManager connectionManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectionManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectionManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiInfo != null && wifiInfo.isConnected()) {
            isNetwokAvailable = true;
        } else if (mobileInfo != null && mobileInfo.isConnected()) {
            isNetwokAvailable = true;
        } else {

            isNetwokAvailable = false;
        }

        if(!isNetwokAvailable&&toShowDialog){
            DialogUtils.showInfoDialog(ctx, ctx.getString(R.string.textSorry), ctx.getString(R.string.textNetworNotAvaliable));
        }
        return isNetwokAvailable;
    }

}
