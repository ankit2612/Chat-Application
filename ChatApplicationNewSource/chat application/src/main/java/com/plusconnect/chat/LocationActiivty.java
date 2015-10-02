package com.plusconnect.chat;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.plusconnect.Asynctask.IBackgroundTaskInterface;
import com.plusconnect.Asynctask.StaticMapsAsyncTask;
import com.plusconnect.Beans.StaticMapRequestBean;
import com.plusconnect.Utilities.DialogResponseInterface;
import com.plusconnect.Utilities.DialogUtils;
import com.plusconnect.Utilities.PlusConnectUtils;

/**
 * Created by ambesh on 27-09-2015.
 */
public class LocationActiivty extends ActionBarActivity {

    public static String LATITUDE="latitude";

    public static String LONGITUDE="longitude";
    public static String SEND_BY ="sentBy";
    public static String IMAGE_PATH="image_path";

    private SupportMapFragment supportMapFragment;
    private FrameLayout mapContainer;
    private ImageView testImageView;
    private GoogleApiClient googleApiClient;
    private Toolbar tool_bar;
    private Button sendLocationBtn,enableLocation;
    private String sendBy;
    private   Location location;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map_activity);
        mapContainer= (FrameLayout) findViewById(R.id.mapContainer);
        tool_bar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(tool_bar);
        sendLocationBtn= (Button) findViewById(R.id.sendLocationBtn);
        testImageView= (ImageView) findViewById(R.id.testImageView);
        sendLocationBtn.setOnClickListener(onClickListener);

        enableLocation= (Button) findViewById(R.id.enableLocation);
        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(viewIntent);
            }
        });

        if (!PlusConnectUtils.isLocationProviderEnabled(LocationActiivty.this,null,false)){
            enableLocation.setVisibility(View.VISIBLE);
        }

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        View view=getLayoutInflater().inflate(R.layout.layout_location_header,null);
        view.findViewById(R.id.hc_back_ImageButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,null);
                finish();
            }
        });
    getSupportActionBar().setCustomView(view);

        if (getIntent()!=null){
            Bundle bundle=getIntent().getExtras();
            if (bundle!=null){
                sendBy=bundle.getString(SEND_BY,"");
            }
        }
        initliseSupportMapFragment();
        createGoogleApiCLient();

    }

    private void initliseSupportMapFragment() {
        supportMapFragment=SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.mapContainer,supportMapFragment);
        fragmentTransaction.commit();

    }



    private IBackgroundTaskInterface staticMapInterface=new IBackgroundTaskInterface() {
        @Override
        public void doOnSucess(String... params) {

            if (params!=null&&(params.length!=0)){
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString(LATITUDE,location.getLatitude()+"");
                bundle.putString(LONGITUDE,location.getLongitude()+"");
                bundle.putString(IMAGE_PATH, params[0]);
                intent.putExtras(bundle);

                setResult(RESULT_OK, intent);
//                testImageView.setImageURI(Uri.parse(params[0]));
                finish();
            }



        }

        @Override
        public void doOnError(String errorCode) {

        }
    };

    private void createGoogleApiCLient() {
       googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PlusConnectUtils.isLocationProviderEnabled(LocationActiivty.this,null,false)){
            enableLocation.setVisibility(View.GONE);
        }
        else {
            enableLocation.setVisibility(View.VISIBLE);
        }

        if (!googleApiClient.isConnected()){

            googleApiClient.reconnect();

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        googleApiClient.disconnect();
    }

    private GoogleApiClient.ConnectionCallbacks connectionCallbacks=new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(Bundle bundle) {
//            fetchLocation();

            GoogleMap googleMap=supportMapFragment.getMap();

            if (googleMap!=null){
                googleMap.setMyLocationEnabled(true);

            }
        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };




    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.sendLocationBtn:
                    Bundle bundle=new Bundle();
                    GoogleMap googleMap=supportMapFragment.getMap();

                    if (googleMap!=null){
                        location=googleMap.getMyLocation();
                        if (location!=null){


                            StaticMapsAsyncTask staticMapsAsyncTask=new StaticMapsAsyncTask(LocationActiivty.this,sendBy,staticMapInterface);
                            StaticMapRequestBean staticMapRequestBean=new StaticMapRequestBean();
                            staticMapRequestBean.setLatitude(location.getLatitude()+"");
                            staticMapRequestBean.setLongitude(location.getLongitude()+"");
                            staticMapsAsyncTask.execute(staticMapRequestBean);

//                            bundle.putString(LATITUDE,location.getLatitude()+"");
//                            bundle.putString(LONGITUDE, location.getLongitude() + "");
//                            Intent intent=new Intent();
//                            intent.putExtras(bundle);
//                            LocationActiivty.this.setResult(RESULT_OK,intent);
//                            finish();
                        }

                    }


                    break;
            }
        }
    };
//    private void fetchLocation(){
//
//        Location location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        if (location!=null){
//            GoogleMap googleMap=supportMapFragment.getMap();
//
//            if (googleMap!=null){
//
//                googleMap.
//
//            }
//
//
//
//        }
//        else{
//            DialogUtils.showConfirmationDialog(LocationActiivty.this, "", LocationActiivty.this.getString(R.string.textUnableToFetchLocation)
//                    , false, 0, getString(R.string.btn_Ok), getString(R.string.btn_Cancel), new DialogResponseInterface() {
//                @Override
//                public void doOnPositiveBtnClick(Activity activity) {
//                    fetchLocation();
//                }
//
//                @Override
//                public void doOnNegativeBtnClick(Activity activity) {
//
//                }
//            });
//        }
//    }
    private GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener=new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            PlusConnectUtils.onGooglePlayServicesCooncetionFailed(LocationActiivty.this,connectionResult,googleApiClient,PlusConnectUtils.GOOGLE_PLAY_SERVICE_CONNECTION_REQUEST_CODE
            );
        }
    };







}


