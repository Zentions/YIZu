package com.example.yizu.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yizu.OrderActivity;
import com.example.yizu.R;
import com.example.yizu.adapter.BusinessAdapter;
import com.example.yizu.bean.Record;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private List<Record> mList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    RecyclerView recyclerView;
    BusinessAdapter adapter;
    View view;
    private int POLICY =1;
    OrderActivity orderActivity;
    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);//布局创建
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.OrderRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        recyclerView = (RecyclerView) view.findViewById(R.id.OrderRecyclerView);
        return  view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);//活动创建  监听事件
        orderActivity = (OrderActivity)getActivity();
        GridLayoutManager layoutManager = new GridLayoutManager(orderActivity, 1);
        recyclerView.setLayoutManager(layoutManager);
        //  Log.e("debug1",String.valueOf(mList.size()));
        adapter =new BusinessAdapter(mList);
        recyclerView.setAdapter(adapter);
        initRV();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                POLICY = 2;
                refreshBusiness();
            }
        });
    }


    void initRV() {
        BmobQuery<Record> query = new BmobQuery<Record>();
        query.setLimit(10).order("-createdAt");
        if(POLICY==1) query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        else query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> object, BmobException e) {
                //mList = object;
                mList.clear();
                mList.addAll(object);
                Log.e("debug1", object.toString());
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });

    }
    public void refreshBusiness(){
        initRV();
    }
}
