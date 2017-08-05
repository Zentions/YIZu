package com.example.yizu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yizu.bean.User;
import com.example.yizu.control.DynamicButton;
import com.example.yizu.control.ResultAnimation;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RsetPassword_pActivity extends Activity{
    private ImageView rps_back2;
    private EditText yanzhengma;
    private EditText newpassword1;
    private EditText newpassword_r1;
    private DynamicButton sure1;
    private Button btn_yanzhengma;
    private int i=60;
    private String countryCode="86";
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rset_password_p);
        ActivityCollecter.addActivty(this);
        rps_back2=(ImageView)findViewById(R.id.rps_back2);
        yanzhengma=(EditText) findViewById(R.id.yanzhengma);
        newpassword1=(EditText) findViewById(R.id.newpassword1);
        newpassword_r1=(EditText) findViewById(R.id.newpsaaword_r1);
        sure1=(DynamicButton) findViewById(R.id.ps_sure1);
        btn_yanzhengma=(Button)findViewById(R.id.btn_yanzhengma);
        number = ShareStorage.getShareString(RsetPassword_pActivity.this,"PN");
        rps_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final ResultAnimation animation = (ResultAnimation)findViewById(R.id.result);

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);


        sure1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String code = yanzhengma.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getApplicationContext(), "验证码不能为空",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.submitVerificationCode(countryCode, number, code);

            }
        });
        btn_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送验证码之前的判断
                final String newp = newpassword1.getText().toString();
                final String newpr = newpassword_r1.getText().toString();
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
                SMSSDK.getVerificationCode(countryCode, number);
                btn_yanzhengma.setClickable(false);
                //开始倒计时
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-1);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-2);
                    }
                }).start();
            }
        });
        morphToSquare(sure1,0);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                btn_yanzhengma.setText(i + " s");
            } else if (msg.what == -2) {
                btn_yanzhengma.setText("重新发送");
                btn_yanzhengma.setClickable(true);
                i = 60;
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("asd", "event=" + event + "  result=" + result + "  ---> result=-1 success , result=0 error");
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//验证成功
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereEqualTo("phoneNumber", number);
                        query.setLimit(10);
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> object, BmobException e) {
                                final String newp = newpassword1.getText().toString();
                                User user = object.get(0);
                                user.update(user.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        btn_yanzhengma.setClickable(false);
                                        sure1.setEnabled(false);
                                        morphToSuccess(sure1);
                                    }
                                });

                            }
                        });

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(getApplicationContext(), "验证码已经发送",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    try {
                        Throwable throwable = (Throwable) data;
                        throwable.printStackTrace();
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status > 0 && !TextUtils.isEmpty(des)) {
                            Log.e("asd", "des: " + des);
                            Toast.makeText(RsetPassword_pActivity.this, des, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    };


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
        SMSSDK.unregisterAllEventHandler();
        ActivityCollecter.removeActivity(this);
    }
}
