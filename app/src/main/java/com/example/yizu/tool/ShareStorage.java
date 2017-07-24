package com.example.yizu.tool;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by q on 2017/7/23.
 */

public class ShareStorage {
    public static boolean setShareInt(Context context,String tag, int value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putInt(tag,value);
        editor.apply();
        return true;
    }
    public static boolean setShareFloat(Context context,String tag, float value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putFloat(tag,value);
        editor.apply();
        return true;
    }
    public static boolean setShareString(Context context,String tag, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(tag,value);
        editor.apply();
        return true;
    }
    public static int getShareInt(Context context,String tag){
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return  pref.getInt(tag,0);
    }
    public static float getShareFloat(Context context,String tag){
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return  pref.getFloat(tag,0.0f);
    }
    public static String getShareString(Context context,String tag){
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return  pref.getString(tag,"");
    }
}
