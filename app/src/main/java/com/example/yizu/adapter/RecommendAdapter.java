package com.example.yizu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yizu.R;
import com.example.yizu.ShowActivity;
import com.example.yizu.bean.Goods;

import java.util.List;

/**
 * Created by yikuai on 2017/7/27.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private Context mContext;
    private List<Goods> mRecommend;
    private int max_count = 6;//最大显示数
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1;
    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        ImageView recommendImage;
        TextView recommendName;
        TextView  recommendDetailed;
        TextView  recommendMoney;
        TextView  recommend_score;
        public ViewHolder(View view){
            super(view);
            layout=(RelativeLayout) view;
            recommendImage=(ImageView)view.findViewById(R.id.recommend_image);
            recommendName=(TextView)view.findViewById(R.id.recommend_name);
            recommendMoney=(TextView)view.findViewById(R.id.recommend_rentmoney);
            recommendDetailed=(TextView)view.findViewById(R.id.recommend_detailed);
            recommend_score=(TextView)view.findViewById(R.id.recommend_score);
        }
    }
    public RecommendAdapter(List<Goods> recommendList){
        mRecommend=recommendList;
    }
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_card, parent, false);
        RecommendAdapter.ViewHolder holder = new RecommendAdapter.ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowActivity.class);//跳转到详情页面
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    public void onBindViewHolder(RecommendAdapter.ViewHolder holder, int position){

        Goods recommend=mRecommend.get(position);
        holder.recommendName.setText(recommend.getGoodsName());
        holder.recommendDetailed.setText(recommend.getClassification());
        holder.recommendMoney.setText(String.valueOf(recommend.getMoneyPer()));
        holder.recommendMoney.append("元/天");
        holder.recommend_score.append(String.valueOf(recommend.getStarRating()));
         Glide.with(mContext).load(recommend.getPic1()).into(holder.recommendImage);
    }
    @Override
    public int getItemCount(){
        return mRecommend.size();
    }

}