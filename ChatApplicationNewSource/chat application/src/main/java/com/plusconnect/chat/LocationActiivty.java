package com.plusconnect.chat;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.plusconnect.Utilities.DialogResponseInterface;
import com.plusconnect.Utilities.DialogUtils;
import com.plusconnect.Utilities.PlusConnectUtils;

/**
 * Created by ambesh on 27-09-2015.
 */
public class LocationActiivty extends ActionBarActivity {

    private SupportMapFragment supportMapFragment;
    private FrameLayout mapContainer;
    private GoogleApiClient googleApiClient;
    private Toolbar tool_bar;
    private Button sendLocationBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map_activity);
        mapContainer= (FrameLayout) findViewById(R.id.mapContainer);
        tool_bar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(tool_bar);
        sendLocationBtn= (Button) findViewById(R.id.sendLocationBtn);
        sendLocationBtn.setOnClickListener(onClickListener);



        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setCustomView(R.layout.layout_map_activity);
        initliseSupportMapFragment();
        createGoogleApiCLient();

    }

    private void initliseSupportMapFragment() {
        supportMapFragment=SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.mapContainer,supportMapFragment);
        fragmentTransaction.commit();

    }

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


    public static String LATITUDE="latitude";

    public static String LONGITUDE="longitude";
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.sendLocationBtn:
                    Bundle bundle=new Bundle();
                    GoogleMap googleMap=supportMapFragment.getMap();

                    if (googleMap!=null){
                        Location location=googleMap.getMyLocation();
                        if (location!=null){
                            bundle.putString(LATITUDE,location.getLatitude()+"");
                            bundle.putString(LONGITUDE, location.getLongitude() + "");
                            Intent intent=new Intent();
                            intent.putExtras(bundle);
                            LocationActiivty.this.setResult(RESULT_OK,intent);
                            finish();
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


