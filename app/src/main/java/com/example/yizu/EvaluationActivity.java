package com.example.yizu;

import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.yizu.bean.Evaluation;
import com.example.yizu.bean.Record;
import com.example.yizu.tool.ActivityCollecter;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class EvaluationActivity extends AppCompatActivity {

    RatingBar mRatingBar;
    Button SubBtn;
    EditText etEvaluation;
    private Evaluation Evl;
    private Record record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        ActivityCollecter.addActivty(this);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.evlToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Evl =new Evaluation();
        Intent intent = getIntent();
        record = (Record)intent.getSerializableExtra("waitToEval");
        Evl.setEval(record.getRenting());
        Evl.setComment(record);
        Evl.setBelongTo(record.getMake());
        SubBtn=(Button)findViewById(R.id.submitbtn);
        etEvaluation=(EditText)findViewById(R.id.evaluation);

        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Evl.setStarRating(new Double(rating));
            }
        });
        SubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = etEvaluation.getText().toString().trim();
                if (!TextUtils.isEmpty(temp)) {
                    Evl.setTextEvaluation(temp);
                    if (Evl.getStarRating() == null) {
                        Toast.makeText(EvaluationActivity.this, "您还没有评价星级", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saveRecord();
                    saveEval();
                    finish();
                } else{
                    Toast.makeText(EvaluationActivity.this, "请输入您的评价!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
    private void saveEval(){
        Evl.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Toast.makeText(EvaluationActivity.this, "评价了" + Evl.getStarRating() + "星", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void saveRecord(){
        record.setEval(true);
        record.update(record.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }
}
