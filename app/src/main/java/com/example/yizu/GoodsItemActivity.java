package com.example.yizu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.bean.Goods;
import com.example.yizu.bean.Record;
import com.example.yizu.tool.PictureTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zackratos.ultimatebar.UltimateBar;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsItemActivity extends AppCompatActivity {
    private TextView itemName1,itemClassification1,itemState1,detail,totalRentMoney,totalRentTimes;
    private ImageView pic;
    private Button button;
    private Button button1;
    private ImageView back;
    Goods goods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item);
        UltimateBar bar = new UltimateBar(this);
        bar.setImmersionBar();
        itemName1=(TextView)findViewById(R.id.itemName2);
        itemClassification1=(TextView)findViewById(R.id.itemClassification2);
        itemState1=(TextView)findViewById(R.id.itemState2);
        detail=(TextView)findViewById(R.id.itemdetail2);
        totalRentMoney=(TextView)findViewById(R.id.itemtotalRentMoney2);
        totalRentTimes=(TextView)findViewById(R.id.itemtotalRentTimes2);
        back = (ImageView)findViewById(R.id.goodsBack);
        button=(Button)findViewById(R.id.holdon);
        button1=(Button)findViewById(R.id.end);
        pic = (ImageView) findViewById(R.id.itemPic);
        Intent intent = getIntent();
        goods = (Goods)intent.getSerializableExtra("myGoods");
        itemName1.setText(goods.getGoodsName());
        itemClassification1.setText(goods.getClassification());
        detail.setText(goods.getDescription());
        pic.setImageBitmap(PictureTool.showImage(goods.getPath(0)));
        totalMoney();
        totalTime();
        queryState();
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(goods.getState().equals("租用结束")){
                    Toast.makeText(GoodsItemActivity.this,"当前物品可租用！！！",Toast.LENGTH_SHORT).show();
                    goods.setState("可租用");
                    itemState1.setText("可租用");
                    saveGoodsState();
                }else {
                    Toast.makeText(GoodsItemActivity.this,"当前物品已下架或已出租！！！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(goods.getState().equals("租用结束")){
                    Toast.makeText(GoodsItemActivity.this,"下架成功！！！",Toast.LENGTH_SHORT).show();
                    goods.setState("已下架");
                    itemState1.setText("已下架");
                    saveGoodsState();
                }else {
                    Toast.makeText(GoodsItemActivity.this,"当前物品已下架或已出租！！！",Toast.LENGTH_SHORT).show();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void totalTime(){
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.addWhereEqualTo("make", goods);
        query.count(Record.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    totalRentTimes.setText(""+count);
                }else{

                }
            }
        });
    }
    private void totalMoney(){
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.sum(new String[] { "money" });
        query.addWhereEqualTo("make", goods);
        query.findStatistics(Record.class,new QueryListener<JSONArray>() {

            @Override
            public void done(JSONArray ary, BmobException e) {
                if(e==null){
                    if(ary!=null){//
                        try {
                            JSONObject obj = ary.getJSONObject(0);
                            double sum = obj.getDouble("_sumMoney");//_(关键字)+首字母大写的列名
                            totalRentMoney.setText(""+sum);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }else{
                        totalRentMoney.setText("0");
                    }
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }

        });
    }
    private void queryState(){
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        query.addQueryKeys("state");
        query.getObject(goods.getObjectId(), new QueryListener<Goods>() {
            @Override
            public void done(Goods queryGoods, BmobException e) {
                if(e==null){
                    goods.setState(queryGoods.getState());
                    itemState1.setText(goods.getState());
                }
            }
        });
    }
    private void  saveGoodsState(){
       goods.update(goods.getObjectId(), new UpdateListener() {
           @Override
           public void done(BmobException e) {

           }
       });
    }
}
