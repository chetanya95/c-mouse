package com.chetanya.android.c_mouse.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chetanya.android.c_mouse.R;
import com.chetanya.android.c_mouse.datas.Touch;
import com.chetanya.android.c_mouse.listeners.SensorListener;
import com.chetanya.android.c_mouse.services.SocketService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "C-Mouse_MainActivity";

    private Toast toast;
    private SensorManager sensorManager;
    private Sensor gSensor;
    private SensorListener sensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        TextView sensorReadingsView = findViewById(R.id.sensorReadings);


        SocketService.discoverServer();

        sensorListener = new SensorListener(sensorReadingsView);
        sensorManager.registerListener(sensorListener, gSensor, SensorManager.SENSOR_DELAY_UI);

    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, gSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void leftClick(View v){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), "Left Mouse Button Clicked", Toast.LENGTH_LONG);
        toast.show();

        Touch touch = new Touch();
        touch.setData("Left Click");
        SocketService.sendData(touch);
    }

    public void rightClick(View v){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), "Right Mouse Button Clicked", Toast.LENGTH_LONG);
        toast.show();

        Touch touch = new Touch();
        touch.setData("Right Click");
        SocketService.sendData(touch);
    }

}
