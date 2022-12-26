//package com.artest.chatapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.firebase.client.ChildEventListener;
//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import android.os.Bundle;
//
//import org.w3c.dom.Text;
//
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import java.util.Map;
//import java.io.UnsupportedEncodingException;
//import java.util.Base64;
//
//public class Chat extends AppCompatActivity {
//
//    LinearLayout layout;
//    ImageView sendButton;
//    EditText messageArea;
//    ScrollView scrollView;
//    Firebase reference1, reference2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        layout = (LinearLayout) findViewById(R.id.layout1);
//        sendButton = (ImageView) findViewById(R.id.sendButton);
//        messageArea = (EditText) findViewById(R.id.messageArea);
//        scrollView = (ScrollView) findViewById(R.id.scrollView);
//
//        Firebase.setAndroidContext(this);
//        // Change it to your address
//        reference1 = new Firebase("https://networkinglab9-3f213-default-rtdb.asia-southeast1.firebasedatabase.app/" + "messages/" + UserDetails.username + "_" + UserDetails.chatWith);
//        reference2 = new Firebase("https://networkinglab9-3f213-default-rtdb.asia-southeast1.firebasedatabase.app/" + "messages/" + UserDetails.chatWith + "_" + UserDetails.username);
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String messageText = messageArea.getText().toString();
//                messageArea.setText("");
//                if (!messageText.equals("")) {
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("message", messageText);
//                    map.put("user", UserDetails.username);
//                    reference1.push().setValue(map);
//                    reference2.push().setValue(map);
//                    // Fill in reference1 push
//                }
//            }
//        });
//
//        reference1.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Map map = dataSnapshot.getValue(Map.class);
//                String message = map.get("message").toString();
//                String userName = map.get("user").toString();
//
//                if(userName.equals(UserDetails.username)){
//                    addMessageBox("You:-\n" + message, 1);
//                }
//                else{
//                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
//                }
//                // Fill in
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
//    }
//
//    public void addMessageBox(String message, int type) {
//        // Fill in
//        TextView textView = new TextView(Chat.this);
//        textView.setText(message);
//        if(type == 1){
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(850, 10, 20, 20);
//            textView.setLayoutParams(lp);
//        }
//        else{
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(20, 10, 20, 20);
//            textView.setLayoutParams(lp);
//        }
//        if(type == 1){
//            textView.setBackgroundResource(R.drawable.rounded_corner1);
//        }
//        else{
//            textView.setBackgroundResource(R.drawable.rounded_corner2);
//        }
//        layout.addView(textView);
//        scrollView.fullScroll(View.FOCUS_DOWN);
//    }
//}

package com.artest.chatapp;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class Chat extends AppCompatActivity implements TextWatcher {

    private String name;
    private WebSocket webSocket;
    private final String SERVER_PATH = "ws://10.10.10.141:3000";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);


        name = getIntent().getStringExtra("name");
        initiateSocketConnection();

    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
            sendBtn.setVisibility(View.VISIBLE);
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(Chat.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();

                initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    jsonObject.put("isSent", false);

                    messageAdapter.addItem(jsonObject);

                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    private void initializeView() {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", name);
                jsonObject.put("message", messageEdit.getText().toString());

                webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
