package com.artest.chatapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.SocketKeepalive;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class SharepingActivity extends AppCompatActivity {
    TextView thetext;
    EditText titleinput, descinput, priceinput;
    MaterialButton sendbutton;
    GoogleMap mMap = Map.mMap;
    Socket mSocket;
    LocationRequest locationRequest;
    static double latitude;
    static double longitude;
    String titlemsg;
    String descmsg;
    String pricemsg;

    public static final String TAG  = "SharepingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareping);

        thetext = findViewById(R.id.thetext);
        titleinput = findViewById(R.id.titleinput);
        descinput = findViewById(R.id.descinput);
        priceinput = findViewById(R.id.priceinput);
        sendbutton = findViewById(R.id.sendButton);


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        String titlemsg = titleinput.getText().toString().trim();
        String descmsg = descinput.getText().toString().trim();
        String pricemsg = priceinput.getText().toString().trim();


        //System.out.println("This is datatatat" + sendmess);

        SocketHandler.setSocket();
        SocketHandler.establishConnection();
        mSocket = SocketHandler.getSocket();
        System.out.println("This is data" + mSocket);
        mSocket.emit(titlemsg + " " + descmsg + " " + pricemsg);

        getCurrentLocation();//get the current location

        mSocket.on("pong", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (args[0] != null) {
                    final String counter = (String) args[0];
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            thetext.set = counter;
                            System.out.println("This is dataaa" + counter);
                        }
                    });
                }
            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view);
            }
        });

    }

    public void sendMessage(View view){
        getCurrentLocation();
        Log.i(TAG, "sendMessage: ");
        titlemsg = titleinput.getText().toString().trim();
        descmsg = descinput.getText().toString().trim();
        pricemsg = priceinput.getText().toString().trim();

        System.out.println("Send mess" + titlemsg + descmsg + pricemsg);
        if(TextUtils.isEmpty(titlemsg)){
            Log.i(TAG, "sendMessage:2 ");
            return;
        }
        titleinput.setText("");
        descinput.setText("");
        priceinput.setText("");

        JSONObject jsonObject = new JSONObject();
        try {//create send message
            jsonObject.put("titlemsg", titlemsg);
            jsonObject.put("descmsg", descmsg);
            jsonObject.put("pricemsg", pricemsg);
            jsonObject.put("location lat", latitude);
            jsonObject.put("location lon", longitude);
            System.out.println("Json Obj" + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "sendMessage: 1"+ mSocket.emit("chat message", jsonObject));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {
                    getCurrentLocation();

                }else {
                    turnOnGPS();
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }
    public void getLocation()
    {
        getCurrentLocation();
    }
    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SharepingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {
                    LocationServices.getFusedLocationProviderClient(SharepingActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(SharepingActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        latitude = locationResult.getLocations().get(index).getLatitude();
                                        longitude = locationResult.getLocations().get(index).getLongitude();

                                        thetext.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                        System.out.println("The location is" + latitude + " " + longitude);

                                        LatLng mylocation = new LatLng(SharepingActivity.latitude, SharepingActivity.longitude);
                                        MarkerOptions markerOptions = new MarkerOptions().position(mylocation).title(titlemsg);
                                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble));
                                        Marker marker = mMap.addMarker(markerOptions);
                                        System.out.println("Location " + SharepingActivity.latitude + " " + SharepingActivity.longitude);
//                                        mMap.addMarker(new MarkerOptions()
//                                                .position(mylocation)
//                                                .title(titlemsg));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(SharepingActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(SharepingActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }
}
