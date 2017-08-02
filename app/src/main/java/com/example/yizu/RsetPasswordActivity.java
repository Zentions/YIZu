package com.example.yizu;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.yizu.control.DynamicButton;
import com.example.yizu.tool.ActivityCollecter;


public class RsetPasswordActivity extends Activity {
    private ImageView rps_back1;
    private EditText oldpassord;
    private EditText newpassword;
    private EditText newpassword_r;
    private DynamicButton sure;
    private int value=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rset_password);
        ActivityCollecter.addActivty(this);
        rps_back1 = (ImageView) findViewById(R.id.rps_back1);
        oldpassord = (EditText) findViewById(R.id.oldpassword);
        newpassword = (EditText) findViewById(R.id.newpsaaword);
        newpassword_r = (EditText) findViewById(R.id.newpsaaword_r);
        sure = (DynamicButton) findViewById(R.id.ps_sure);
        rps_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                morphToSuccess(sure);
            }
        });
        morphToSquare(sure,0);

    }
    private void morphToSquare(final DynamicButton btnMorph, long duration) {
        DynamicButton.PropertyParam square = DynamicButton.PropertyParam.build()
                .duration(duration)
                .setCornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .setColor(color(R.color.mb_blue))
                .setPressedColor(color(R.color.mb_blue_dark))
                .text("确认修改");
        btnMorph.startChange(square);
    }

    private void morphToSuccess(final DynamicButton btnMorph) {
        DynamicButton.PropertyParam circle = DynamicButton.PropertyParam.build()
                .duration(500)
                .setCornerRadius(dimen(R.dimen.mb_height_75))
                .setWidth(dimen(R.dimen.mb_height_75))
                .setHeight(dimen(R.dimen.mb_height_75))
                .setColor(color(R.color.mb_green))
                .icon(drawable(R.drawable.ic_done))
                .setPressedColor(color(R.color.mb_green_dark));

        btnMorph.startChange(circle);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    public Drawable drawable(int resId){
        return getResources().getDrawable(resId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}
