package com.example.yizu;

import android.app.DatePickerDialog;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.apache.http.impl.entity.EntityDeserializer;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;

import static android.R.attr.author;
import static android.R.attr.transcriptMode;
import static android.R.attr.windowTransitionBackgroundFadeDuration;

public class ConfirmOrderActivity extends AppCompatActivity {
    TextView Startime,Endtime;
    TextView RentMoney,DepMoney,Count,All,Grade;
    LinearLayout showorder;
    String initDateTime="2017年7月23日 14:44";
    Button Settle;
    Goods currentgoods;
    User usr;
    Long c;
    Date start,end;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        Startime =(TextView) findViewById(R.id.starttime);
        Endtime=(TextView) findViewById(R.id.endtime);
        Settle =(Button)findViewById(R.id.settlebtn);
        showorder=(LinearLayout)findViewById(R.id.show);
        RentMoney=(TextView)findViewById(R.id.rentmoney);
        DepMoney=(TextView)findViewById(R.id.depmoney);
        Count=(TextView)findViewById(R.id.count);
        Grade=(TextView)findViewById(R.id.grade);
        All=(TextView)findViewById(R.id.All);
        usr =new User();
        usr.setGrade(80);
        currentgoods=new Goods();
        currentgoods.setDeposit(200.0);

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
                if(c<0){
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
                    if(usr.getGrade()>=50&&usr.getGrade()<100){
                        count=9.8;
                    }
                    else if(usr.getGrade()<200){
                        count=9.5;
                    }
                    else{
                        count=9.0;
                    }
                    Count.setText(count+"折");
                    Grade.setText(usr.getGrade().toString());
                    All.setText(currentgoods.getDeposit()+c*count+"元");
                    RentMoney.setText(count*c+"元");//打折后的租金
                    showorder.setVisibility(View.VISIBLE);
                    Settle.setEnabled(false);
                }

            }
        });
    }
}
