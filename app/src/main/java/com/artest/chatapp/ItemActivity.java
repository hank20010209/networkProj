package com.artest.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
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


public class ItemActivity extends AppCompatActivity {
    MaterialButton chatbutton;
    TextView title, desc, price;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        //while(Map.objDone == 0);
        StringBuilder fromMap = Map.returnObj;
        String key = getIntent().getStringExtra("key");

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://shareping-cloud-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference spotRef = database.getReference().child("deal").child("deal-detail").child(key);
        spotRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    System.out.println("From DB" + task.getResult());
                    title.setText(dataSnapshot.child("title").getValue(String.class));
                    desc.setText(dataSnapshot.child("describe").getValue(String.class));
                    price.setText(dataSnapshot.child("price").getValue(Long.class).toString());
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
        chatbutton = (MaterialButton)findViewById(R.id.chatbutton);
        title = (TextView)findViewById(R.id.title);
        desc = (TextView)findViewById(R.id.desc);
        price = (TextView)findViewById(R.id.price);

        title.setText("");
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(ItemActivity.this, AddUserActivity.class));
            }
        });
    }
}
