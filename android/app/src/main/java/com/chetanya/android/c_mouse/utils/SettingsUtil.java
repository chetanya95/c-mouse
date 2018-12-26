package com.chetanya.android.c_mouse.utils;

import android.content.SharedPreferences;

import com.chetanya.android.c_mouse.activities.SettingsActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsUtil {

    public static final ArrayList<String> FLING_OPTIONS = new ArrayList<>(Arrays.asList("Windows Key", "Alt + F4", "Alt + Tab"));

    private static SharedPreferences sharedPreferences;
    private static String leftFling;
    private static String rightFling;

    public static boolean initialize(){
        leftFling = sharedPreferences.getString("leftFling", FLING_OPTIONS.get(0));
        rightFling = sharedPreferences.getString("rightFling", FLING_OPTIONS.get(1));
        return true;
    }


    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        SettingsUtil.sharedPreferences = sharedPreferences;
    }

    public static String getLeftFling() {
        return leftFling;
    }

    public static void setLeftFling(String leftFling) {
        SettingsUtil.leftFling = leftFling;
        sharedPreferences.edit().putString("leftFling", leftFling).apply();
    }

    public static String getRightFling() {
        return rightFling;
    }

    public static void setRightFling(String rightFling) {
        SettingsUtil.rightFling = rightFling;
        sharedPreferences.edit().putString("rightFling", rightFling).apply();
    }
}
