package com.artest.chatapp;
import android.content.Intent;
import java.lang.Object.*;
import java.net.URLConnection.*;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import javax.annotation.Nonnull;

public class Map extends AppCompatActivity implements OnMapReadyCallback, OnMarkerClickListener{
    ImageButton infobutton, friendbutton, sharpingbutton;
    static GoogleMap mMap;
    static StringBuilder returnObj;
    static int objDone;
    static public class MarkerPkg {
        String id;
        String key;
        String title;
        Double lat;
        Double lng;
        Integer num_of_participants;
        Long time;

        public MarkerPkg(String id, String key, String title, Double lat, Double lng, Integer num_of_participants, Long time){
            this.id = id;
            this.key = key;
            this.title = title;
            this.lat = lat;
            this.lng = lng;
            this.num_of_participants = num_of_participants;
            this.time = time;
        }
    }
    static HashMap<String, MarkerPkg> markers = new HashMap<>();


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
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://shareping-cloud-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference spotRef = database.getReference().child("map").child("spot");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String key = dataSnapshot.getKey();
                String title = dataSnapshot.child("title").getValue(String.class);
                Double lat = dataSnapshot.child("lat").getValue(Double.class);
                Double lng = dataSnapshot.child("lng").getValue(Double.class);
                Integer num_of_participants = dataSnapshot.child("num_of_participants").getValue(Integer.class);
                Long time = dataSnapshot.child("time").getValue(Long.class);

                LatLng spot = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions().position(spot).title(title);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bubble));
                Marker marker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(spot));

                MarkerPkg markerPkg = new MarkerPkg(marker.getId(), key, title, lat, lng, num_of_participants, time);
                markers.put(marker.getId(), markerPkg);

                Log.d("firebase", "onChildAdded:" + dataSnapshot.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("firebase", "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("firebase", "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("firebase", "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("firebase", "postComments:onCancelled", databaseError.toException());
            }
        };
        spotRef.addChildEventListener(childEventListener);

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
        MarkerPkg markerPkg = markers.get(marker.getId());
        returnObj = new StringBuilder();
        Intent intent = new Intent(Map.this, ItemActivity.class);
        intent.putExtra("key", markerPkg.key);
        startActivity(intent);
        System.out.println("The Mark on Click");
        return false;
    }
}
