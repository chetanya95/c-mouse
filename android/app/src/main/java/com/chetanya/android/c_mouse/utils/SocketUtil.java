package com.chetanya.android.c_mouse.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.chetanya.android.c_mouse.async_tasks.DiscoverTask;
import com.chetanya.android.c_mouse.async_tasks.SendDataTask;
import com.chetanya.android.c_mouse.inputs.intefaces.InputData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.List;

public class SocketUtil {

    public static final int SERVER_PORT = 8095;

    private static final String TAG = "C-Mouse_SocketService";
    private static final int TIMEOUT = 4000; // milliseonds

    private static InetAddress serverAddress = null;
    private static DatagramSocket socket = null;
    private static InetAddress broadcastAddress = null;

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

    public static InetAddress getBroadcastAddress(WifiManager wifiManager, String ipAddress) throws IOException {
        DhcpInfo dhcp = wifiManager.getDhcpInfo();
        if(dhcp == null) {
            Log.i(TAG, "DHCP is null");
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (convertIP2Int(ipAddress.getBytes()) & dhcp.netmask) | ~dhcp.netmask;
        Log.i(TAG, "broadcast: " + broadcast);
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    public static String getIpAddress(WifiManager wifiManager, NetworkInfo wifiNetworkInfo){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiNetworkInfo.isConnected()) {
            int ipAddress = wifiInfo.getIpAddress();
            // Convert little-endian to big-endianif needed
            if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
                ipAddress = Integer.reverseBytes(wifiInfo.getIpAddress());
            }
            byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
            String ipAddressString;
            try {
                ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
                return ipAddressString;
            } catch (UnknownHostException e) {
                Log.e(TAG, "UnknownHostException: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }

        Method method = null;
        try {
            method = wifiManager.getClass().getDeclaredMethod("getWifiApState");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "NoSuchMethodException: " + e.getMessage());
            e.printStackTrace();
        }
        if(method != null)
            method.setAccessible(true);
        int actualState = -1;
        try {
            if(method!=null) {
                actualState = (Integer) method.invoke(wifiManager, (Object[]) null);
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "IllegalAccessException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "InvocationTargetException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        if(actualState==13){  //if wifiAP is enabled
            Log.i(TAG, "Found Hotspot Enabled!");
            return "192.168.43.1"; //hardcoded WifiAP ip
        }
        return null;
    }
    public static int convertIP2Int(byte[] ipAddress){
        return (int) (Math.pow(256, 3)*Integer.valueOf(ipAddress[3] & 0xFF)+Math.pow(256, 2)*Integer.valueOf(ipAddress[2] & 0xFF)+256*Integer.valueOf(ipAddress[1] & 0xFF)+Integer.valueOf(ipAddress[0] & 0xFF));
    }


    public static InetAddress getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(InetAddress serverAddress) {
        SocketUtil.serverAddress = serverAddress;
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
            //Log.e(TAG, "Trying to send data but couldn't discover the server yet!");
        }
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static void setSocket(DatagramSocket socket) {
        SocketUtil.socket = socket;
    }

    public static InetAddress getBroadcastAddress() {
        return broadcastAddress;
    }

    public static void setBroadcastAddress(InetAddress broadcastAddress) {
        SocketUtil.broadcastAddress = broadcastAddress;
    }
}
