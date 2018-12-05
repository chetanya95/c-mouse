package com.example.ranu.c_mouse;

/**
 * Created by Ranu on 17-09-2016.
 */
import java.io.*;
import java.net.*;

public class AppSocket implements Runnable {

    /*
    private Socket mouseSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Position positionObj;
    */

    private String hostName = "192.168.43.22";
    private int portNumber = 8002;
    private boolean connectStatus = false;
    private InetAddress receiverAddress;
    private DatagramSocket datagramSocket;


    public void run() {

        try{
            datagramSocket = new DatagramSocket();
            receiverAddress = InetAddress.getByName(hostName);
            connectStatus = true;

        } catch (SocketException e) {
            System.err.println("SocketException: " + e.getMessage());
            System.exit(1);
        } catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            System.exit(1);
        }

        /*
        while(true) {
            String position = positionObj.getPosition();
            if(position != null) {
                out.println(position);
            }
            synchronized (positionObj) {
                try {
                    positionObj.wait();
                }
                catch(InterruptedException ex){
                    System.err.println("AppSocket Thread Interrupt Exception: " + ex.getMessage());
                }
            }
        }
        */
    }




    public void sendReading(String position){
        if(connectStatus = true) {
            try {
                byte[] buffer = position.getBytes("UTF-8");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, portNumber);
                datagramSocket.send(packet);
            }catch(UnsupportedEncodingException ex){
                System.err.println("UnsupportedEncodingException: " + ex.getMessage());
            }
            catch(IOException ex){
                System.err.println("IOException in transmitting: " + ex.getMessage());
            }
        }
    }

}