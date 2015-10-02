package com.plusconnect.Asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

import com.plusconnect.Beans.ChatUserBean;
import com.plusconnect.Beans.StaticMapRequestBean;
import com.plusconnect.Utilities.PlusConnectUtils;
import com.plusconnect.chat.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Created by ambesh on 01-10-2015.
 */
public class StaticMapsAsyncTask extends AsyncTask<StaticMapRequestBean,Intent,String>{

    private Activity activity;
    private ProgressDialog progressDialog;
    private Display display;
    private String userToSend;
    private IBackgroundTaskInterface iBackgroundTaskInterface;
    public StaticMapsAsyncTask(Activity activity,String userToSend,IBackgroundTaskInterface iBackgroundTaskInterface) {

        this.activity = activity;
         display = activity.getWindowManager().getDefaultDisplay();
        this.userToSend=userToSend;
        this.iBackgroundTaskInterface=iBackgroundTaskInterface;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(progressDialog!=null){
            progressDialog.dismiss();
        }

        if (!TextUtils.isEmpty(s)){

            iBackgroundTaskInterface.doOnSucess(s);
        }
        else{
            iBackgroundTaskInterface.doOnError("");
        }

    }

    @Override
    protected String doInBackground(StaticMapRequestBean... params) {


        String filePath=null;
        byte[] bytes=null;

        int width=(display.getWidth()*80)/100;
        int height=(display.getHeight()*20)/100;
        try{
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();




            HttpGet httpGet=new HttpGet();
            String URL=activity.getString(R.string.staticBaseUrl)+activity.getString(R.string.textCenter)
            +"="+""+params[0].getLatitude()+","+params[0].getLongitude()+""+"&"+activity.getString(R.string.textZoom)+"=10"+"&"+
                    activity.getString(R.string.textSize)+"="+width+"x"+height+"&"
                    +activity.getString(R.string.textMapType)+"=roadmap"+"&"+activity.getString(R.string.textMarkers)+"="+"color:red"+"%7C"
                    +params[0].getLatitude()+","+params[0].getLongitude()+"&"+activity.getString(R.string.textKey)
                    +"="+activity.getString(R.string.google_browser_key);


            httpGet.setURI(new URI(URL));

            HttpResponse httpResponse = defaultHttpClient.execute(httpGet);

            bytes= EntityUtils.toByteArray(httpResponse.getEntity());

            File baseDirectory=new File(activity.getExternalFilesDir(null)+userToSend+activity.getString(R.string.textSend));


            baseDirectory.mkdirs();
            File photo=new File(baseDirectory
                    ,System.currentTimeMillis()+".png");


            if (!photo.exists()){
                photo.createNewFile();
            }
            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());

                filePath=photo.getPath();
                fos.write(bytes);
                fos.close();
            }
            catch (java.io.IOException e) {
                PlusConnectUtils.logException(e);
            }




        }
        catch (Exception e){
            PlusConnectUtils.logException(e);
        }


return filePath;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog=new ProgressDialog(new ContextThemeWrapper(activity, android.R.style.Theme_Holo_Light));
        progressDialog.setTitle(activity.getString(R.string.textTitleDownloadignImage));

        progressDialog.setMessage(activity.getString(R.string.textDownloadingProgress));


        progressDialog.setCancelable(false);

        progressDialog.setIcon(0);

        progressDialog.show();



    }
}
