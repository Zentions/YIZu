package com.example.yizu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yizu.R;
import com.example.yizu.bean.Goods;
import com.example.yizu.control.MyScrollView;
import com.example.yizu.control.PublicStaticClass;

public class OneFragment extends Fragment {
    View mView;
    private Goods goods;
    private TextView name1,rent1,position1,deposit1,classification1,starRating1,description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_one, container, false);

        goods=new Goods();
        goods.setGoodsName("123");
        goods.setArea("黄岛区");
        goods.setPositioning("青岛市");
        goods.setMoneyPer(6.0);
        goods.setDeposit(100.0);
        goods.setClassification("电子产品");
        goods.setStarRating(6.7);
        goods.setDescription("啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊啦啦啦啦啦啊啦拉拉啦啦啦啦啦啊啦啦啦啦啦啊");
        //fragment_one的文件中的TextView的查找
        name1=(TextView)mView.findViewById(R.id.name1);
        rent1=(TextView)mView.findViewById(R.id.rent1);
        position1=(TextView)mView.findViewById(R.id.position1);
        deposit1=(TextView)mView.findViewById(R.id.deposit1);
        classification1=(TextView)mView.findViewById(R.id.classification1);
        starRating1=(TextView)mView.findViewById(R.id.starRating1);
        description=(TextView)mView.findViewById(R.id.description);
        //fragment_one的文件中的TextView的赋值
        name1.append("       ");
        name1.append(goods.getGoodsName());
        rent1.append("       ");
        rent1.append(String.valueOf(goods.getMoneyPer()));
        rent1.append("元/天");
        position1.append("       ");
        position1.append(goods.getPositioning());
        position1.append(goods.getArea());
        deposit1.append("       ");
        deposit1.append(String.valueOf(goods.getDeposit()));
        classification1.append("       ");
        classification1.append(goods.getClassification());
        starRating1.append("       ");
        starRating1.append(String.valueOf(goods.getStarRating()));
        description.append("       ");
        description.append(goods.getDescription());
        initView();
        return mView;
    }

    private void initView() {
        MyScrollView oneScrollView= (MyScrollView) mView.findViewById(R.id.oneScrollview);
        oneScrollView.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
                    PublicStaticClass.IsTop = true;
                } else {
                    PublicStaticClass.IsTop = false;
                }
            }

            @Override
            public void notBottom() {

            }

        });
    }
}
