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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SocketHandler {
    static Socket mSocket;
    static void setSocket()
    {
        try {
            mSocket = IO.socket("https://shareping-cloud-backend.azurewebsites.net/");
        } catch (URISyntaxException e) {
            System.out.println("Can't Not Connecting to Server");
        }
    }
    static Socket getSocket(){
        return mSocket;
    }

    static void establishConnection() {
        mSocket.connect();
    }

    static void closeConnection() {
        mSocket.disconnect();
    }
}
