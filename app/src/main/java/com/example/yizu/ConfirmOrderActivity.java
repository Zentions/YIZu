package com.example.yizu;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.yizu.bean.Evaluation;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.bean.User;
import com.example.yizu.control.DoubleDatePickerDialog;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;

import org.apache.http.impl.entity.EntityDeserializer;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;


public class ConfirmOrderActivity extends AppCompatActivity {
    TextView Startime,Endtime;
    TextView RentMoney,DepMoney,Count,All,Grade,rentedTV,nameTV;
    LinearLayout showorder;
    String initDateTime="2017年7月23日 14:44";
    Button Settle;
   Goods currentgoods;
    Button pay;
    User rentedUser,rentingUser;
    private String GoodsId;
    Long c;
    Date start,end;
    private double needMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.ConfirmToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ActivityCollecter.addActivty(this);
        Startime =(TextView) findViewById(R.id.starttime);
        Endtime=(TextView) findViewById(R.id.endtime);
        Settle =(Button)findViewById(R.id.settlebtn);
        showorder=(LinearLayout)findViewById(R.id.show);
        RentMoney=(TextView)findViewById(R.id.rentmoney);
        DepMoney=(TextView)findViewById(R.id.depmoney);
        Count=(TextView)findViewById(R.id.count);
        Grade=(TextView)findViewById(R.id.grade);
        All=(TextView)findViewById(R.id.All);
        pay = (Button) findViewById(R.id.lijizhifu);
        rentedTV = (TextView)findViewById(R.id.rentedTextView);
        nameTV = (TextView)findViewById(R.id.goodsTextView);
        Intent intent = getIntent();
        GoodsId = intent.getStringExtra("GoodsId");

        rentingUser = new User();
        rentingUser.setObjectId(ShareStorage.getShareString(ConfirmOrderActivity.this,"ObjectId"));
        queryRentingUser();
        queryGoods();
        Startime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubleDatePickerDialog dateTimePicKDialog=new DoubleDatePickerDialog(ConfirmOrderActivity.this,initDateTime);
                dateTimePicKDialog.dateTimePicKDialog(Startime);
            }
        });

        Endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubleDatePickerDialog dateTimePicKDialog=new DoubleDatePickerDialog(ConfirmOrderActivity.this,initDateTime);
                dateTimePicKDialog.dateTimePicKDialog(Endtime);
            }
        });

        Settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //计算间隔天数
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat smdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    start = smdf.parse( Startime.getText().toString());
                    end =smdf.parse(Endtime.getText().toString());
                    c = (end.getTime()-start.getTime())/ (3600 * 24 * 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(c<=0){
                    Toast.makeText(ConfirmOrderActivity.this, "请输入正确结束日期", Toast.LENGTH_SHORT).show();
                    return;
                }else if(start.getTime()<calendar.getTime().getTime()){
                    Toast.makeText(ConfirmOrderActivity.this, "不能选取今天之前的日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    DepMoney.setText(currentgoods.getDeposit()+"元");//押金
                    //计算本次折扣
                    Double count;
                    if(rentingUser.getGrade()>=50&&rentingUser.getGrade()<100){
                        count=0.98;
                    }
                    else if(rentingUser.getGrade()<200){
                        count=0.95;
                    }
                    else{
                        count=0.90;
                    }
                    Count.setText(count+"折");
                    Grade.setText(rentingUser.getGrade().toString());
                    All.setText(currentgoods.getDeposit()+c*count+"元");
                    RentMoney.setText(count*c+"元");//打折后的租金
                    needMoney = count*c;
                    showorder.setVisibility(View.VISIBLE);
                    Settle.setVisibility(View.GONE);
                }

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Record record = new Record();
                record.setMake(currentgoods);
                record.setRenting(rentingUser);
                record.setRented(rentedUser);
                record.setMoney(needMoney);
                record.setDeposit(currentgoods.getDeposit());
                record.setState("交易中");
                record.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            Toast.makeText(ConfirmOrderActivity.this,"交易成功",Toast.LENGTH_LONG).show();//！！！！！
                        }else{
                            Toast.makeText(ConfirmOrderActivity.this,"订单异常",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
    private void queryGoods(){
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.include("user");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.getObject(GoodsId, new QueryListener<Goods>() {
            @Override
            public void done(Goods object, BmobException e) {
                if(e==null){
                    currentgoods = object;
                    rentedUser = currentgoods.getUser();
                    rentedTV.setText(rentedUser.getName());
                    nameTV.setText(currentgoods.getGoodsName());
                }else{
                    Toast.makeText(ConfirmOrderActivity.this,"订单异常",Toast.LENGTH_LONG).show();
                    finish();
                }
            }

        });
    }
   private void queryRentingUser(){
       BmobQuery<User> query = new BmobQuery<User>();
       query.getObject(rentingUser.getObjectId(), new QueryListener<User>() {
           @Override
           public void done(User object, BmobException e) {
               if(e==null){
                   rentingUser = object;
               }else{
                   Toast.makeText(ConfirmOrderActivity.this,"订单异常",Toast.LENGTH_LONG).show();
                   finish();
               }
           }

       });
   }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
