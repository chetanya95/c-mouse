package com.chetanya.android.c_mouse.async_tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.chetanya.android.c_mouse.datas.intefaces.InputData;
import com.chetanya.android.c_mouse.services.SocketService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SendDataTask extends AsyncTask<InputData, Void, Boolean> {
    private static final String TAG = "C-Mouse_SendTask";


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(InputData... inputData) {
        DatagramSocket socket = SocketService.getSocket();

        try {
            byte[] buffer = inputData[0].toJSON().getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, SocketService.getServerAddress() , SocketService.SERVER_PORT);
            socket.send(packet);
        }catch(UnsupportedEncodingException e){
            Log.e(TAG, "UnsupportedEncodingException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch(IOException e){
            Log.e(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean returnBoolean) {


    }
}
