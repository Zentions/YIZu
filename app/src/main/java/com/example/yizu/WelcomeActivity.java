package com.example.yizu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        BmobInstallation.getCurrentInstallation().save();
// 启动推送服务
        BmobPush.startWork(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent StartIntent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(StartIntent);
                finish();
            }
        }, 3000);

    }
}
