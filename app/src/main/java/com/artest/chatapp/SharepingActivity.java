package com.artest.chatapp;

import android.annotation.SuppressLint;
import android.net.SocketKeepalive;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class SharepingActivity extends AppCompatActivity {
    TextView thetext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareping);
        thetext = findViewById(R.id.thetext);
        SocketHandler.setSocket();
        SocketHandler.establishConnection();
        Socket mSocket = SocketHandler.getSocket();
        System.out.println("This is data" + mSocket);
        mSocket.emit("ping");

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

    }
}
