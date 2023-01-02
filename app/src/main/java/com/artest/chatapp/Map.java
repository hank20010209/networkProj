package com.artest.chatapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.util.Log;
import android.view.ViewGroup;
import java.util.concurrent.FutureTask;
import java.lang.annotation.ElementType;
import java.util.Objects;
import java.time.LocalTime;
import java.lang.Object.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.firebase.client.DataSnapshot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import javax.annotation.Nonnull;


public class Map extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener{
    ImageButton infobutton, friendbutton, sharpingbutton;
    static GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        infobutton = (ImageButton)findViewById(R.id.infobutton);
        friendbutton = (ImageButton)findViewById(R.id.friendbutton);
        sharpingbutton = (ImageButton)findViewById(R.id.sharpingbutton);

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Map.this, UserInfo.class));
            }
        });

        friendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Map.this, AddUserActivity.class));
            }
        });

        sharpingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Map.this, SharepingActivity.class));
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://shareping-cloud-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference spotRef = database.getReference().child("map").child("spot");
        System.out.println("This is Data" + spotRef);
        System.out.println("This is Data" + database);
        spotRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot ds : task.getResult().getChildren()) {
                        double lat = ds.child("lat").getValue(Double.class);
                        double lng = ds.child("lng").getValue(Double.class);
                        String title = ds.child("title").getValue(String.class);
                        Long time = ds.child("time").getValue(Long.class);

                        //Display spot on map
                        LatLng spot = new LatLng(lat, lng);
                        MarkerOptions marker = new MarkerOptions().position(spot).title(title);
                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble));
                        mMap.addMarker(marker);

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(spot));
                    }
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });

//        mDatabase =
        // Add a marker in Sydney and move the camera
//        LatLng mylocation = new LatLng(SharepingActivity.latitude, SharepingActivity.longitude);
//        System.out.println("Location " + SharepingActivity.latitude + " " + SharepingActivity.longitude);
//        mMap.addMarker(new MarkerOptions()
//                .position(mylocation)
//                .title("mylocation"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

    }

    public boolean onMarkerClick(final Marker marker)
    {
        startActivity(new Intent(Map.this, ItemActivity.class));
        System.out.println("The Mark on Click");
        return false;
    }
}
