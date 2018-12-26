package com.chetanya.android.c_mouse.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import com.chetanya.android.c_mouse.inputs.Movement;
import com.chetanya.android.c_mouse.utils.SocketUtil;

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

            float roundX = Math.round(preciseX * 10000) / 10000.0f;
            float roundY = Math.round(preciseY * 10000) / 10000.0f;
            float roundZ = Math.round(preciseZ * 10000) / 10000.0f;

            String sensorReadings = "Sensor Readings\n" + "x: "+ Float.toString(roundX) + ", y: " + Float.toString(roundY) + " z: " + Float.toString(roundZ);
            sensorReadingsView.setText(sensorReadings);

            //as delta(t) is constant, directly multiplying acceleration by a constant to get movement
            Movement movement = new Movement();

            movement.setX(-Math.round(roundX * MOVEMENT_CONSTANT));
            movement.setY( Math.round(roundY * MOVEMENT_CONSTANT));

            SocketUtil.sendData(movement);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
