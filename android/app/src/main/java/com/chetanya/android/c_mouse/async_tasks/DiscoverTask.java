package com.chetanya.android.c_mouse.async_tasks;


import android.os.AsyncTask;
import android.util.Log;

import com.chetanya.android.c_mouse.utils.SocketUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class DiscoverTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = "C-Mouse_DiscoverTask";

    private static final int MAX_PACKET_SIZE = 2048;
    private static final String DISCOVERY_REQUEST = "Where are you C-Mouse Server?";
    private static final String DISCOVERY_REPLY = "I am here C-Mouse Client.";

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(Void... aVoid) {
        byte[] receiveBuffer = new byte[MAX_PACKET_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,
                receiveBuffer.length);

        DatagramSocket socket = SocketUtil.getSocket();
        try {
            socket.setBroadcast(true);
        } catch (SocketException e) {
            Log.e(TAG, "SocketException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        byte[] sendPacketData = DISCOVERY_REQUEST.getBytes();
        InetAddress broadcastAddress = SocketUtil.getBroadcastAddress();

        DatagramPacket sendPacket = new DatagramPacket(sendPacketData,
                sendPacketData.length, broadcastAddress, SocketUtil.SERVER_PORT);

        while(true) {
            try {
                socket.send(sendPacket);
                Log.i(TAG, String.format("Sent packet to %s:%d",
                        broadcastAddress.getHostAddress(), SocketUtil.SERVER_PORT));


                socket.receive(receivePacket);
                Log.i(TAG, "Received reply from "
                        + receivePacket.getAddress().getHostAddress());
                String reply = new String(receivePacket.getData()).trim();
                Log.i(TAG, "Reply data: " + reply);

                if(reply.contains(DISCOVERY_REPLY)){
                    Log.i(TAG, "Reply matches!");
                    break;
                }else{
                    Log.i(TAG, "Reply doesn't matches!");
                    continue;
                }
            }
            catch(SocketTimeoutException e) {
                Log.e(TAG,"SocketTimeoutException: " + e.getMessage());
                //e.printStackTrace();
                continue;
            }
            catch (IOException e) {
                Log.e(TAG,"IOException: " + e.getMessage());
                //e.printStackTrace();
                continue;
            }
        }
        if (socket != null) {
            try {
                socket.setBroadcast(false);
            } catch (SocketException e) {
                Log.e(TAG, "SocketException: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
        return receivePacket.getAddress().getHostAddress();
    }

    @Override
    protected void onPostExecute(String serverIP) {
        try{
            SocketUtil.setServerAddress(InetAddress.getByName(serverIP));
        } catch (UnknownHostException e) {
            Log.e(TAG, "UnknownHostException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

}
