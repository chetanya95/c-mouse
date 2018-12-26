package com.chetanya.android.c_mouse.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chetanya.android.c_mouse.R;
import com.chetanya.android.c_mouse.inputs.Touch;
import com.chetanya.android.c_mouse.listeners.SensorListener;
import com.chetanya.android.c_mouse.utils.SettingsUtil;
import com.chetanya.android.c_mouse.utils.SocketUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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

        SettingsUtil.setSharedPreferences(getApplicationContext().getSharedPreferences("com.chetanya.android.c_mouse", Context.MODE_PRIVATE));
        SettingsUtil.initialize();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        String ipAddress = SocketUtil.getIpAddress(wifiManager, wifiNetworkInfo);
        Log.i(TAG, "ipAddress: " + ipAddress);
        InetAddress broadcastAddress = null;
        if(ipAddress.equals("192.168.43.1")){
            try {
                broadcastAddress = InetAddress.getByName("192.168.43.255");
            } catch (UnknownHostException e) {
                Log.e(TAG, "UnknownHostException: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }else{
            try {
                broadcastAddress = SocketUtil.getBroadcastAddress(wifiManager, ipAddress);
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
        SocketUtil.setBroadcastAddress(broadcastAddress);
        SocketUtil.discoverServer();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        TextView sensorReadingsView = findViewById(R.id.sensorReadings);
        sensorListener = new SensorListener(sensorReadingsView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(sensorListener, gSensor, SensorManager.SENSOR_DELAY_GAME);


        final GestureDetectorCompat leftButtonDetector = new GestureDetectorCompat(this, new LeftButtonGestureListener());
        Button leftButton = findViewById(R.id.leftButton);
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return leftButtonDetector.onTouchEvent(event);
            }
        });

        final GestureDetectorCompat rightButtonDetector = new GestureDetectorCompat(this, new RightButtonGestureListener());
        Button rightButton = findViewById(R.id.rightButton);
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return rightButtonDetector.onTouchEvent(event);
            }
        });

    }

    public void leftClick(){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), "Left Click", Toast.LENGTH_SHORT);
        toast.show();

        Touch touch = new Touch();
        touch.setData("Left Click");
        SocketUtil.sendData(touch);
    }

    public void leftFling(){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), SettingsUtil.getLeftFling(), Toast.LENGTH_SHORT);
        toast.show();

        Touch touch = new Touch();
        touch.setData(SettingsUtil.getLeftFling());
        SocketUtil.sendData(touch);
    }

    public void rightClick(){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), "Right Click", Toast.LENGTH_SHORT);
        toast.show();

        Touch touch = new Touch();
        touch.setData("Right Click");
        SocketUtil.sendData(touch);
    }

    public void rightFling(){
        if(toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(), SettingsUtil.getRightFling(), Toast.LENGTH_SHORT);
        toast.show();

        Touch touch = new Touch();
        touch.setData(SettingsUtil.getRightFling());
        SocketUtil.sendData(touch);
    }

    public void settingsClick(View v){
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
    }

    class LeftButtonGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "LeftButtonGestureListen";

        @Override
        public boolean onDown(MotionEvent event) {
            //Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            leftFling();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event){
            leftClick();
            return true;
        }
    }

    class RightButtonGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "RightButtonGestureListen";

        @Override
        public boolean onDown(MotionEvent event) {
            //Log.d(DEBUG_TAG,"onDown: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
            rightFling();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event){
            rightClick();
            return true;
        }
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorListener);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, gSensor, SensorManager.SENSOR_DELAY_GAME);
    }

}
