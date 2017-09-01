package com.example.yizu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.yizu.adapter.TitleFragmentPagerAdapter;
import com.example.yizu.fragment.OrderFragment;
import com.example.yizu.tool.ActivityCollecter;

import java.util.ArrayList;
import java.util.List;


public class OrderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView oreder_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ActivityCollecter.addActivty(this);
        tabLayout=(TabLayout)findViewById(R.id.tab);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        oreder_back = (ImageView) findViewById(R.id.order_back1);
        final List<Fragment> fragments = new ArrayList<>();
        OrderFragment a = new OrderFragment();
        OrderFragment b = new OrderFragment();
        a.setCurrentPage(1);
        b.setCurrentPage(2);
        fragments.add(a);
        fragments.add(b);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(),fragments, new String[]{"未完成", "已完成"});
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        oreder_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollecter.removeActivity(this);
    }
}