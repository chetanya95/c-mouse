package com.chetanya.android.c_mouse.inputs;

import android.util.Log;

import com.chetanya.android.c_mouse.inputs.intefaces.InputData;

import org.json.JSONException;
import org.json.JSONObject;

public class Touch implements InputData {

    private static final String TAG = "C-Mouse_Touch";

    private String data;

    public Touch(){ }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public String toJSON(){
        JSONObject innerJSON= new JSONObject();
        JSONObject outerJSON= new JSONObject();
        try {
            innerJSON.put("data", getData());
            outerJSON.put("touch", innerJSON);
            return outerJSON.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "JSONException: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}
