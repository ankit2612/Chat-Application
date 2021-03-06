package com.plusconnect.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.plusconnect.chat.R;

/**
 * Created by ambesh on 09-09-2015.
 */
public class PlusConnectUtils {








    public static boolean isLocationProviderEnabled(Activity activity,DialogResponseInterface dialogInterface,boolean toShowDialog){

        boolean locationEnabled=false;
        LocationManager locationManager= (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if(!locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                if (toShowDialog)
                showLocationSettingDialog(activity,dialogInterface);
                return false;

            }

        }

        return true;
    }




    private static void showLocationSettingDialog(Activity activity,DialogResponseInterface dialogInterface) {


        DialogUtils.showConfirmationDialog(activity,activity.getString(R.string.textEnableLocationService),activity.getString(R.string.textEnableLocationServiceData)
                ,false,-1,activity.getString(R.string.btn_Ok),activity.getString(R.string.btn_Cancel),dialogInterface);


    }



    public static int GOOGLE_PLAY_SERVICE_CONNECTION_REQUEST_CODE=1;

    public static void onGooglePlayServicesCooncetionFailed(Activity activity,ConnectionResult connectionResult,GoogleApiClient googleApiClient,int requestCode){

        try{

            if(servicesConnected(activity)){
                if (connectionResult.hasResolution()) {
                    try {


                        // Start an Activity that tries to resolve the error
                        connectionResult
                                .startResolutionForResult(
                                        activity,
                                        GOOGLE_PLAY_SERVICE_CONNECTION_REQUEST_CODE);

    /*
     * Thrown if Google Play services canceled the original
     * PendingIntent
     */

                    } catch (IntentSender.SendIntentException e) {

                        // Log the error
                        logException(e);
                    }
                } else {


                    DialogUtils.showInfoDialog(activity,activity.getString(R.string.cannotConnectToGooglePlayServices));


                }
            }



        }
        catch (Exception e){
            logException(e);
        }



    }

    private static boolean servicesConnected(Activity activity) {

        boolean isServiceCOnnected=false;

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        // Showing status
        if(status==ConnectionResult.SUCCESS){
            isServiceCOnnected=true;
        }
        else{
            Dialog dialog = GooglePlayServicesUtil
                    .getErrorDialog(status, activity,
                            GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
            dialog.show();



        }

        return isServiceCOnnected;
    }







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




    public static int convertImageUriToFile ( Uri imageUri, Activity activity )  {

        Cursor cursor = null;
        int imageID = 0;

        try {

            /*********** Which columns values want to get *******/
            String [] proj={
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Thumbnails._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };

            cursor = activity.managedQuery(

                    imageUri,         //  Get data for specific image URI
                    proj,             //  Which columns to return
                    null,             //  WHERE clause; which rows to return (all rows)
                    null,             //  WHERE clause selection arguments (none)
                    null              //  Order-by clause (ascending by name)

            );

            //  Get Query Data

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            //int orientation_ColumnIndex = cursor.
            //    getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            int size = cursor.getCount();

            /*******  If size is 0, there are no images on the SD Card. *****/

            if (size == 0) {


                Toast.makeText(activity,activity.getString(R.string.textCamerayNotAvalaible),Toast.LENGTH_LONG).show();
            }
            else
            {

                int thumbID = 0;
                if (cursor.moveToFirst()) {

                    /**************** Captured image details ************/

                    /*****  Used to show image on view in LoadImagesFromSDCard class ******/
                    imageID     = cursor.getInt(columnIndex);

                    thumbID     = cursor.getInt(columnIndexThumb);

                    String Path = cursor.getString(file_ColumnIndex);

                    //String orientation =  cursor.getString(orientation_ColumnIndex);



                    // Show Captured Image detail on activity

                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Return Captured Image ImageID ( By this ImageID Image will load from sdcard )

        return imageID;
    }



}
