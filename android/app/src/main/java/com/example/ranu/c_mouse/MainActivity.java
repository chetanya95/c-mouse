package com.example.ranu.c_mouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toast toast;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private MyAccelerometerListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        TextView positionsView = (TextView) findViewById(R.id.positionsView);

        myListener = new MyAccelerometerListener(positionsView);

        senSensorManager.registerListener(myListener, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);


    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(myListener);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(myListener, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void leftClick(View v){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), "Left Mouse Button Clicked", Toast.LENGTH_LONG);
        toast.show();
        myListener.appSocket.sendReading("Left Click");
    }

    public void rightClick(View v){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), "Right Mouse Button Clicked", Toast.LENGTH_LONG);
        toast.show();
        myListener.appSocket.sendReading("Right Click");
    }

}
