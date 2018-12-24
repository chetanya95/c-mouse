package com.chetanya.android.c_mouse.services;

import android.util.Log;

import com.chetanya.android.c_mouse.async_tasks.DiscoverTask;
import com.chetanya.android.c_mouse.async_tasks.SendDataTask;
import com.chetanya.android.c_mouse.datas.intefaces.InputData;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SocketService{

    private static final String TAG = "C-Mouse_SocketService";
    private static final int TIMEOUT = 4000; // milliseonds

    private static InetAddress serverAddress = null;
    private static DatagramSocket socket = null;

    static{
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            Log.e(TAG, "SocketException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static InetAddress getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(InetAddress serverAddress) {
        SocketService.serverAddress = serverAddress;
    }

    public static void discoverServer(){
        DiscoverTask discoverTask = new DiscoverTask();
        discoverTask.execute();
    }

    public static void sendData(InputData inputData){
        if(serverAddress != null) {
            SendDataTask sendDataTask = new SendDataTask();
            sendDataTask.execute(inputData);
        }else{
            Log.e(TAG, "Trying to send data but couldn't discover the server yet!");
        }
    }

    public static final int SERVER_PORT = 2802;

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static void setSocket(DatagramSocket socket) {
        SocketService.socket = socket;
    }
}
