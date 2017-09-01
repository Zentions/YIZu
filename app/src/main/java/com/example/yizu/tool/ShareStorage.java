package com.example.yizu.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    public static boolean setShareString(Context context,String tag1, String value1,String tag2,String value2){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(tag1,value1);
        editor.putString(tag2,value2);
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
    public static String[] getShareString(Context context,String tag1,String tag2){
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String [] value = new String[2];
        value[0] = pref.getString(tag1,"");
        value[1] = pref.getString(tag2,"");
        return  value;
    }
    public static boolean setStrings(Context context,String tag,String []array){
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        HashSet<String> set = new HashSet<String>();
        for(int i=0;i<array.length;i++){
            set.add(array[i]);
        }
        editor.putStringSet(tag,set);
        editor.apply();
        return  true;
    }
    public static Object[] getStrings(Context context, String tag){
        SharedPreferences pref = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) pref.getStringSet(tag,null);
        Log.d("debug1","$"+set.toString());
        return set.toArray();
    }
    public static void putBoolean(String key, boolean value, Context context){
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(String key, boolean defValue, Context context){
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }
}
