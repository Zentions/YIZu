package com.example.yizu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.Toast;
import android.text.TextUtils;
import android.util.Log;


import com.example.yizu.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    //private static final String TAG = "LoginActivity";
    Button login;
    TextView forget,register;
    EditText userNameEdit,passwordEdit;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= getSharedPreferences("data", Context.MODE_PRIVATE);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        userNameEdit=(EditText)findViewById(R.id.account);
        passwordEdit=(EditText)findViewById(R.id.password);
        remember=(CheckBox)findViewById(R.id.remember);
        login=(Button)findViewById(R.id.login);
        forget=(TextView) findViewById(R.id.forget);
        register=(TextView)findViewById(R.id.register);
        boolean isRemember=pref.getBoolean("remember",false);
        if(isRemember){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            userNameEdit.setText(account);
            passwordEdit.setText(password);
            remember.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String un = userNameEdit.getText().toString().trim();
                final String up = passwordEdit.getText().toString().trim();
                if (TextUtils.isEmpty(un) || TextUtils.isEmpty(up)) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // 登陆
                    //是否保存密码
                    BmobQuery<User> query = new BmobQuery<User>();// 查询类
                    query.setLimit(1);
                    query.addWhereEqualTo("phoneNumber", un);// 查询条件
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> userList, BmobException e) {
                            if (e == null) {
                                User user =userList.get(0);
                                if (user.getPassword().equals(up)) {
                                    editor = pref.edit();
                                    if (remember.isChecked()) {
                                        editor.putBoolean("remember", true);
                                        editor.putString("account", user.getPhoneNumber());
                                        editor.putString("password", user.getPassword());

                                    } else {
                                        editor.clear();
                                    }
                                    editor.putString("ObjectId",user.getObjectId());
                                    editor.apply();
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "网络异常！", Toast.LENGTH_SHORT).show();
                            }

                        }

                    });


                }

            }
        });

        forget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(LoginActivity.this,PhoneVerificationActivity.class);//跳转到忘记密码的页面
                startActivityForResult(intent,1);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);//跳转到注册的页面
                startActivityForResult(intent,1);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    User user = (User)data.getSerializableExtra("NewUser");
                    userNameEdit.setText(user.getPhoneNumber());
                    passwordEdit.setText(user.getPassword());
                }
                break;
            default:;
        }
    }
}
