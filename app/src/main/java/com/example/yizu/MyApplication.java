package com.example.yizu;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;

import org.litepal.LitePalApplication;

/**
 * Created by q on 2017/7/20.
 */

public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        MobSDK.init(context, "1f6fe120545f1", "1af3024cc2ebad4a2eb99c01219c8051");
    }
    public static Context getContext(){
        return context;
    }
}
