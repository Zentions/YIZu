package com.example.yizu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.bean.User;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;

import java.io.Serializable;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class RecordActivity extends AppCompatActivity implements Serializable{
    Record record;
    TextView rentedPersonName;
    TextView rentingPersonName;
    TextView Code;
    TextView GoodsName;
    TextView Rentmoney;
    TextView deposit;
    TextView othermoney;
    TextView TIME;
    TextView Connect;
    TextView NOW;
    Button eval,finishBusiness;
    private User rented,renting;
    private Goods goods;
    private TableRow row;//跳转物品界面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ActivityCollecter.addActivty(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.RecordToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        rentedPersonName = (TextView)findViewById(R.id.rentedPersonname);
        rentingPersonName=(TextView)findViewById(R.id.rentingPersonname);
        Code=(TextView)findViewById(R.id.code);
        Connect=(TextView)findViewById(R.id.connect);
        GoodsName=(TextView)findViewById(R.id.goodsName) ;
        Rentmoney=(TextView)findViewById(R.id.RentMoney);
        deposit=(TextView)findViewById(R.id.Deposit );
        othermoney=(TextView)findViewById(R.id.OtherMoney);
        TIME=(TextView)findViewById(R.id.time) ;
        NOW=(TextView)findViewById(R.id.Now);
        finishBusiness = (Button)findViewById(R.id.jieshujiaoyi);
        eval = (Button)findViewById(R.id.pingjia);
        row = (TableRow)findViewById(R.id.LookForGoods);
        Intent intent=getIntent();
        record = (Record) intent.getSerializableExtra("record");
        query();
        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent2=new Intent(RecordActivity.this,.class);
//                startActivity(intent2);
            }
        });
        eval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this,EvaluationActivity.class);
                intent.putExtra("waitToEval",record);
                startActivity(intent);
                finish();
            }
        });
        finishBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出支付对话框
            }
        });
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转物品
            }
        });
    }
    void show(){
        String currentUser = ShareStorage.getShareString(RecordActivity.this,"ObjectId");
        Log.d("debug1",currentUser);
        Log.d("debug1",record.getState());
        String state = record.getState();
        if(currentUser.equals(rented.getObjectId()) && state.equals("交易中"))
            finishBusiness.setVisibility(View.VISIBLE);
        if(!currentUser.equals(rented.getObjectId()) && record.getEval()==false && record.getState().equals("交易成功")) eval.setVisibility(View.VISIBLE);
        rentedPersonName.setText(rented.getName());
        rentingPersonName.setText(renting.getName());
        Code.setText(record.getObjectId());
        GoodsName.setText(goods.getGoodsName());
        Rentmoney.setText(record.getMoney().toString());
        deposit.setText(record.getDeposit().toString());
        if(record.getLossOfExpense()==null) othermoney.setText("未录入");
        else othermoney.setText(record.getLossOfExpense().toString());
        TIME.setText(record.getCreatedAt());
        NOW.setText(record.getState());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
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
    void query(){
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.include("rented[objectId|name],renting[objectId|name],make");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.getObject(record.getObjectId(), new QueryListener<Record>() {
            @Override
            public void done(Record object, BmobException e) {
                if(e==null){
                    record = object;
                    rented = object.getRented();
                    renting = object.getRenting();
                    goods = object.getMake();
                    show();
                }else{
                    Toast.makeText(RecordActivity.this,"网络异常",Toast.LENGTH_LONG).show();
                    finish();
                }
            }

        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        query();
    }
}
