package com.example.yizu;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.yizu.adapter.BusinessAdapter;
import com.example.yizu.bean.Record;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class OrderActivity extends AppCompatActivity {
    private List<Record> mList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;

    RecyclerView recyclerView;
    BusinessAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.Historytoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.HisRefresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshBusiness();
            }
        });


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(OrderActivity.this, 1);
        recyclerView.setLayoutManager(layoutManager);
        //  Log.e("debug1",String.valueOf(mList.size()));
        adapter = new BusinessAdapter(mList);
        recyclerView.setAdapter(adapter);
        initRV();

    }
    void initRV() {

        BmobQuery<Record> query = new BmobQuery<Record>();
        query.setLimit(10).order("-createdAt");
        query.findObjects(new FindListener<Record>() {
            @Override
            public void done(List<Record> object, BmobException e) {
                //mList = object;
                mList.clear();
                mList.addAll(object);
                Log.e("debug1",object.toString());
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
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

    public void refreshBusiness(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRV();
                    }
                });
            }
        }).start();
    }

}
