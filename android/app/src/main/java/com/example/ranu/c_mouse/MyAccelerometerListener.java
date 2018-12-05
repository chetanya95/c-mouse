package com.example.ranu.c_mouse;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

/**
 * Created by Ranu on 26-08-2016.
 */
public class MyAccelerometerListener implements SensorEventListener {
    private TextView positionsView;
    public AppSocket appSocket;


    public MyAccelerometerListener(TextView v){

        positionsView = v;
        appSocket = new AppSocket();
        Thread th = new Thread(appSocket);
        th.start();
        prevTimestamp = System.currentTimeMillis()*100;
    }

    int velX = 0;
    int velY = 0;
    long prevTimestamp;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_GRAVITY) {
            float floatX = sensorEvent.values[0];
            float floatY = sensorEvent.values[1];
            float floatZ = sensorEvent.values[2];

            int roundX = Math.round(floatX * 10000);
            int roundY = Math.round(floatY * 10000);
            int roundZ = Math.round(floatZ * 10000);

            double x = roundX/10000.0;
            double y = roundY/10000.0;
            double z = roundZ/10000.0;

            String position = "x: "+ Double.toString(x) + ", y: " + Double.toString(y) + " (z: " + Double.toString(z) + ")";
            positionsView.setText(position);

            velX = (int) (x*-10);
            velY = (int) (y*-10);

            appSocket.sendReading("x:" + Integer.toString(velX) + " " + "y:" + Integer.toString(velY) + "end");

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
