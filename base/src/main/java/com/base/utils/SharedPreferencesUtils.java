package com.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.base.BaseApplication;

public class SharedPreferencesUtils {

    public static String SP_DATA = "sp_data";

    public static SharedPreferencesUtils mInstance;
    public static SharedPreferences sharedPreferences;

    public static SharedPreferencesUtils getInstance() {
        if (mInstance == null) {
            synchronized (SharedPreferencesUtils.class){
                if (mInstance == null) {
                    mInstance =  new SharedPreferencesUtils();
                }
            }
        }
        return mInstance;
    }

    public SharedPreferencesUtils() {
        sharedPreferences = BaseApplication.getInstance().getSharedPreferences(SP_DATA, Context.MODE_PRIVATE);
    }

    public static int getNoticeFriendsIndex(int uid) {
        return sharedPreferences.getInt(uid+"noticeFriendsIndex",0);
    }

    public static void setNoticeFriendsIndex(int uid, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(uid+"noticeFriendsIndex", value);
        editor.commit();
    }

    public int getKeyboardHeight() {
        return sharedPreferences.getInt("KeyboardHeight", 800);
    }

    public void setKeyboardHeight(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("KeyboardHeight", value);
        editor.commit();
    }

    public String getJson(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void setJson(String key,String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getCity() {
        return sharedPreferences.getString("City", "北京市");
    }

    public void setCity(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("City", value);
        editor.commit();
    }

    public String getAdvertising() {
        return sharedPreferences.getString("Advertising", "");
    }

    public void setAdvertising(String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Advertising", value);
        editor.commit();
    }

    public String getDownloadWidget() {
        return sharedPreferences.getString("widget", "");
    }

    public void setDownloadWidget(String widget) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("widget", widget);
        editor.commit();
    }

}
