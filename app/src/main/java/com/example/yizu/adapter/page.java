package com.example.yizu.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.yizu.R;

/**
 * Created by q on 2017/7/25.
 */

public class page {
    private View view;

    public page(Activity activity) {
        view = LayoutInflater.from(activity).inflate(R.layout.activity_view,null);

    }
    public View getView(){
        return view;
    }
}
