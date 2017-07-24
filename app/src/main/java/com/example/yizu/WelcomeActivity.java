package com.example.yizu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import cn.bmob.v3.Bmob;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "e740da9c36e83e41ae0e7d14d7e5c067", "bmob");
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
