package com.example.yizu;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yizu.adapter.ArticleAdapter;
import com.example.yizu.bean.Goods;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * ViewPager
 */
public class ArticleFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    private View view;
    int currentPage;
    private Context mContext;
    private List<Goods> articleList = new ArrayList<>();
    private ArticleAdapter adapter;
    private RecyclerView recyclerView;
    private String goodsName;
    private ArticlesActivity activity;
    private String Positioning="青岛";//市
    private String area="黄岛区";//区
    private Double StarRating;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int skip;
    private int limit=6;
    public ArticleFragment() {
        // Required empty public constructor
    }

    public void setNumber(int number) {
        currentPage = number;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.articles, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
//        swipeRefreshLayout= (SwipeRefreshLayout)view.findViewById(R.id.ArtRefresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (ArticlesActivity) getActivity();
       skip=0;
        Intent intent = activity.getIntent();
        goodsName = intent.getStringExtra("name");
//        Positioning=intent.getStringExtra("Positioning");
//        area=intent.getStringExtra("area");

        GridLayoutManager layoutManager = new GridLayoutManager(activity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ArticleAdapter(articleList);
        recyclerView.setAdapter(adapter);
        queryGoods();
    }


  public  void queryGoods() {
         BmobQuery<Goods> query = new BmobQuery<Goods>();
        switch (currentPage) {
            case 0:
                BmobQuery<Goods> eq1 = new BmobQuery<Goods>();
                eq1.addWhereEqualTo("goodsName", goodsName);//物品名
                //--and条件2
                BmobQuery<Goods> eq2 = new BmobQuery<Goods>();
                eq2.addWhereEqualTo("Positioning", Positioning);//市定位
                List<BmobQuery<Goods>> andQuerys1 = new ArrayList<BmobQuery<Goods>>();
                andQuerys1.add(eq1);
                andQuerys1.add(eq2);
////查询符合整个and条件的人
                query.and(andQuerys1);
                break;
            case 1:
                BmobQuery<Goods> eq3 = new BmobQuery<Goods>();
                eq3.addWhereEqualTo("goodsName", goodsName);//物品名
////--and条件2
                BmobQuery<Goods> eq4 = new BmobQuery<Goods>();
                eq4.addWhereEqualTo("area", area);//区定位
                List<BmobQuery<Goods>> andQuerys2 = new ArrayList<BmobQuery<Goods>>();
                andQuerys2.add(eq3);
                andQuerys2.add(eq4);
////查询符合整个and条件的人
                query.and(andQuerys2);
                break;
            case 2:
                query.addWhereEqualTo("goodsName", goodsName);//物品名
                query.order("moneyPer");

                break;
            case 3:
                query.addWhereEqualTo("goodsName", goodsName);//物品名
                query.order("-StarRating");
                break;

        }
     query.setSkip(skip).setLimit(limit);
     skip+=6;
        query.findObjects(new FindListener<Goods>()
        {
            @Override
            public void done(List<Goods> goodsList, BmobException e) {
                if (goodsList == null) {
                    Toast.makeText(activity, "无此物品的相关信息！", Toast.LENGTH_SHORT).show();
                    //swipeRefreshLayout.setRefreshing(false);
                } else {
                    //articleList.clear();
                    articleList.addAll(goodsList);
                    adapter.notifyDataSetChanged();
//                    recyclerView.smoothScrollToPosition(skip);
//                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
//    public void refresh(){
//        queryGoods();
//    }

}












