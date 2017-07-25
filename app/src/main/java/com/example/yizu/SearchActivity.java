package com.example.yizu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.yizu.db.HistoryRecord;
import com.example.yizu.tool.ActivityCollecter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Button search;
    ImageButton back,clear;
    TextView t[] = new TextView[5];
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActivityCollecter.addActivty(this);
        back = (ImageButton)findViewById(R.id.back);
        search = (Button)findViewById(R.id.search);
        clear = (ImageButton)findViewById(R.id.clear);
        editText = (EditText)findViewById(R.id.context);
        t[0] = (TextView)findViewById(R.id.his1);
        t[1] = (TextView)findViewById(R.id.his2);
        t[2]= (TextView)findViewById(R.id.his3);
        t[3] = (TextView)findViewById(R.id.his4);
        t[4] = (TextView)findViewById(R.id.his5);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DataSupport.deleteAll(HistoryRecord.class);
                 initHis();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryRecord historyRecord = new HistoryRecord();
                historyRecord.setDate(new Date(System.currentTimeMillis()));
                String temp = editText.getText().toString().trim();
                if(TextUtils.isEmpty(temp)){
                    Toast.makeText(SearchActivity.this,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                historyRecord.setRecord(temp);
                historyRecord.save();
                Intent intent = new Intent(SearchActivity.this,ArticlesActivity.class);
                intent.putExtra("name",temp);
                startActivity(intent);
            }
        });
        addListener();
        initHis();
    }
    void initHis(){

        List<HistoryRecord> list = DataSupport.order("date desc").limit(5).find(HistoryRecord.class);
        int i;
        for(i=0;i<list.size();i++){
            HistoryRecord historyRecord = list.get(i);
            t[i].setText(historyRecord.getRecord());
        }
        for(;i<5;i++){
            t[i].setText("");
        }
    }
    void addListener(){
        for(int i = 0;i<5;i++){
            final int finalI = i;
            t[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchActivity.this,ArticlesActivity.class);
                    intent.putExtra("name",t[finalI].getText());
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}
