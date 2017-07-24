package com.example.yizu;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.yizu.bean.User;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "e740da9c36e83e41ae0e7d14d7e5c067", "bmob");

//        BmobQuery<User> bmobQuery = new BmobQuery<User>();
//        bmobQuery.getObject("fa94fbf25a", new QueryListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if(e==null){
//                    Intent intent=new Intent(WelcomeActivity.this,UserMessageActivity.class);
//                    intent.putExtra("user",user);
//                    startActivity(intent);
//                    Toast.makeText(WelcomeActivity.this,"成功",Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(WelcomeActivity.this,"失败",Toast.LENGTH_LONG).show();
//                }
//            }
//        });

//        BmobQuery<User> query = new BmobQuery<User>();
//        query.addWhereEqualTo("phoneNumber", "17806236254");
//        query.setLimit(10);
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> object, BmobException e) {
//                if(e==null){
//                    Intent intent=new Intent(WelcomeActivity.this,UserMessageActivity.class);
//                    intent.putExtra("user",object.get(0));
//                    startActivity(intent);
//                    Toast.makeText(WelcomeActivity.this,"成功",Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(WelcomeActivity.this,"失败",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
        Intent intent=new Intent(this,UserMessageActivity.class);

        startActivity(intent);



    }

}
