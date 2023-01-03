package com.artest.chatapp;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;


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
