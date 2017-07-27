package com.example.yizu.adapter;

/**
 * Created by yikuai on 2017/7/24.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {//上栏
    //private Context context;

    private ArrayList<View> mImageViewList;
    //item的个数
    private Context context;

    public ImageAdapter( ArrayList<View> mImageViewList){

        this.mImageViewList=mImageViewList;

    }



   public int getCount() {
        return mImageViewList.size();
    }

   @Override
    public boolean isViewFromObject(View view, Object object) {
       return view == object;
    }

//    //初始化item布局
    @Override
   public Object instantiateItem(ViewGroup container, int position) {
        View view = mImageViewList.get(position);

      container.addView(view);

        return view;
    }

   //销毁item
   @Override
   public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
   }

}