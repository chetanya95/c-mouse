package com.chetanya.android.c_mouse.datas;

import android.util.Log;

import com.chetanya.android.c_mouse.datas.intefaces.InputData;

import org.json.JSONException;
import org.json.JSONObject;

public class Movement implements InputData {

    private static final String TAG = "C-Mouse_Movement";

    private int x;
    private int y;

    public Movement(){}

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toJSON(){
        JSONObject innerJSON= new JSONObject();
        JSONObject outerJSON= new JSONObject();
        try {
            innerJSON.put("x", getX());
            innerJSON.put("y", getY());
            outerJSON.put("movement", innerJSON);
            return outerJSON.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "JSONException: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}
