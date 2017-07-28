package com.example.yizu;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.yizu.adapter.UserGoodsAdapter;
import com.example.yizu.bean.Goods;
import com.example.yizu.bean.User;
import com.example.yizu.control.RefreshHeader;
import com.example.yizu.control.RefreshLoad;
import com.example.yizu.tool.ActivityCollecter;
import com.example.yizu.tool.ShareStorage;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class UserGoodsActivity extends AppCompatActivity {
    private List<Goods> mList = new ArrayList<>();
    private TwinklingRefreshLayout swipeRefresh;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    UserGoodsAdapter adapter;
    View view;
    UserGoodsActivity userGoodsActivity;

    public UserGoodsActivity() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_goods);
        ActivityCollecter.addActivty(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.usergoodsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        swipeRefresh = (TwinklingRefreshLayout)findViewById(R.id.goodRefresh);
        swipeRefresh.setHeaderView(new RefreshHeader(this));
        swipeRefresh.setBottomView(new RefreshLoad(this));
        recyclerView = (RecyclerView) findViewById(R.id.usergoodsRecyclerView);
        linearLayout = (LinearLayout)findViewById(R.id.goodsitem);
        GridLayoutManager layoutManager = new GridLayoutManager(userGoodsActivity, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new UserGoodsAdapter(mList);
        recyclerView.setAdapter(adapter);
        initRV();
        swipeRefresh.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshGoods();
                        refreshLayout.finishRefreshing();
                    }
                },1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });
    }

    void initRV() {
        BmobQuery<Goods> query = new BmobQuery<Goods>();
        User me = new User();
        me.setObjectId(ShareStorage.getShareString(this,"ObjectId"));
        query.addWhereEqualTo("user",me);
        query.findObjects(new FindListener<Goods>() {
                @Override
                public void done(List<Goods> object, BmobException e) {
                    //mList = object;
                    mList.clear();
                    mList.addAll(object);
                    adapter.notifyDataSetChanged();
                }
            });

        }
        public void refreshGoods(){
            initRV();
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}





