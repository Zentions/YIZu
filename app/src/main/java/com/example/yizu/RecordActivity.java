package com.example.yizu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.yizu.bean.Record;

import java.io.Serializable;

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
    TextView NOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        rentedPersonName = (TextView)findViewById(R.id.rentedPersonname);
        rentingPersonName=(TextView)findViewById(R.id.rentingPersonname);
        Code=(TextView)findViewById(R.id.code);
        GoodsName=(TextView)findViewById(R.id.goodsName) ;
        Rentmoney=(TextView)findViewById(R.id.RentMoney);
        deposit=(TextView)findViewById(R.id.Deposit );
        othermoney=(TextView)findViewById(R.id.OtherMoney);
        TIME=(TextView)findViewById(R.id.time) ;
        NOW=(TextView)findViewById(R.id.Now);
        Intent intent=getIntent();
        record = (Record) intent.getSerializableExtra("record");
        show();
    }
    void show(){
        rentedPersonName.setText("出租方："+record.getRentedPerson());
        rentingPersonName.setText("租用方："+record.getRentingPerson());
        Code.setText(record.getObjectId());
        GoodsName.setText(record.getGoodsName());
        Rentmoney.setText(record.getMoney().toString());
        deposit.setText(record.getDeposit().toString());
        othermoney.setText(record.getLossOfExpense().toString());
        TIME.setText(record.getCreatedAt());
        NOW.setText(record.getState());

    }


}
