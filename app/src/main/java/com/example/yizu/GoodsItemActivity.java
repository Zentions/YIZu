package com.example.yizu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yizu.bean.Goods;

public class GoodsItemActivity extends AppCompatActivity {
    private TextView itemName1,itemClassification1,itemState1,detail,totalRentMoney,totalRentTimes;
   private Button button;
   private Button button1;
    Goods goods=new Goods();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_item);
        itemName1=(TextView)findViewById(R.id.itemName1);
        itemClassification1=(TextView)findViewById(R.id.itemClassification1);
        itemState1=(TextView)findViewById(R.id.itemState1);
        detail=(TextView)findViewById(R.id.detail);
        totalRentMoney=(TextView)findViewById(R.id.totalRentMoney);
        totalRentTimes=(TextView)findViewById(R.id.totalRentTimes);
        goods.setGoodsName("22");
        goods.setClassification("33");
        goods.setState("22");
        goods.setDescription("33333");
        itemName1.append(goods.getGoodsName());
        itemClassification1.append(goods.getClassification());
        itemState1.append(goods.getState());
        detail.append(goods.getDescription());

        button=(Button)findViewById(R.id.holdon);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goods.setState("无人租用");
            }
        });

        button1=(Button)findViewById(R.id.end);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                goods.setState("已下架");
            }
        });

    }
}
