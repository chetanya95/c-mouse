package com.chetanya.android.c_mouse.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import com.chetanya.android.c_mouse.datas.Movement;
import com.chetanya.android.c_mouse.services.SocketService;

public class SensorListener implements SensorEventListener {

    private static final String TAG = "C-Mouse_SensorListener";

    private static final int MOVEMENT_CONSTANT = 20;

    private TextView sensorReadingsView;

    private long prevTimestamp;

    public SensorListener(TextView sensorReadingsView){
        this.sensorReadingsView = sensorReadingsView;

        prevTimestamp = System.currentTimeMillis()*100;
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_GRAVITY) {
            float preciseX = sensorEvent.values[0];
            float preciseY = sensorEvent.values[1];
            float preciseZ = sensorEvent.values[2];

            double roundX = Math.round(preciseX * 10000) / 10000.0;
            double roundY = Math.round(preciseY * 10000) / 10000.0;
            double roundZ = Math.round(preciseZ * 10000) / 10000.0;

            String sensorReadings = "Sensor Readings\n" + "x: "+ Double.toString(roundX) + ", y: " + Double.toString(roundY) + " z: " + Double.toString(roundZ);
            sensorReadingsView.setText(sensorReadings);

            //as delta(t) is constant, directly multiplying acceleration by a constant to get movement
            Movement movement = new Movement();

            movement.setX(-((int) Math.round(roundX * MOVEMENT_CONSTANT)));
            movement.setY(((int) Math.round(roundY * MOVEMENT_CONSTANT)));

            SocketService.sendData(movement);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
