package com.example.yizu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yizu.bean.User;
import com.example.yizu.control.DynamicButton;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


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
                final String oldp = oldpassord.getText().toString().trim();
                final String newp = newpassword.getText().toString().trim();
                final String newpr= newpassword_r.getText().toString().trim();
                if(TextUtils.isEmpty(oldp)){
                    Toast.makeText(RsetPasswordActivity.this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newp) ||TextUtils.isEmpty(newpr)) {
                    Toast.makeText(getApplicationContext(), "密码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newp.equals(newpr)) {
                    Toast.makeText(getApplicationContext(), "密码不一致",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newp.length()<6) {
                    Toast.makeText(getApplicationContext(), "密码长度应大于等于6",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                BmobQuery<User> query = new BmobQuery<User>();
                query.setLimit(1);
                String objectId = ShareStorage.getShareString(RsetPasswordActivity.this,"ObjectId");
                query.getObject(objectId, new QueryListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            if (user.getPassword().equals(oldp)) {
                                user.setPassword(newp);
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        sure.setEnabled(false);
                                        morphToSuccess(sure);
                                    }
                                });
                            } else {
                                Toast.makeText(RsetPasswordActivity.this, "旧密码错误！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RsetPasswordActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
