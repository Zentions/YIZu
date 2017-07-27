package com.example.yizu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yizu.R;
import com.example.yizu.RegisterActivity;
import com.example.yizu.ShowActivity;
import com.example.yizu.bean.Goods;

import java.util.List;
/**
 * Created by q on 2017/7/20
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context mContext;
    private List<Goods>   mArticle;
    private int max_count = 6;//最大显示数
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView articleImage;
        TextView  articleName;
        TextView  articleDetailed;
        TextView  articleMoney;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            articleImage=(ImageView)view.findViewById(R.id.article_image);
            articleName=(TextView)view.findViewById(R.id.article_name);
            articleMoney=(TextView)view.findViewById(R.id.article_rentmoney);
            articleDetailed=(TextView)view.findViewById(R.id.article_detailed);
        }
    }
    public ArticleAdapter(List<Goods> articleList){
        mArticle=articleList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.article, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowActivity.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder,int position){

        Goods article=mArticle.get(position);
        holder.articleName.setText(article.getGoodsName());
        holder.articleDetailed.setText(article.getDescription());
        holder.articleMoney.setText(String.valueOf(article.getMoneyPer()));
       // Glide.with(mContext).load(article.getPic()).into(holder.articleImage);
    }
    @Override
    public int getItemCount(){
        return mArticle.size();
    }

}