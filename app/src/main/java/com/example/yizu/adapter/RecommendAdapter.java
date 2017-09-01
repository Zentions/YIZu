package com.example.yizu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yizu.LoginActivity;
import com.example.yizu.R;
import com.example.yizu.ShowActivity;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;
import com.example.yizu.tool.ShowDialog;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by yikuai on 2017/7/27.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {
    private Context mContext;
    private List<Goods> mRecommend;
    private LruCache<String, Bitmap> lruCache;
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
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 使用最大可用内存值的1/8作为缓存的大小。
        int cacheSize = maxMemory / 8;
        lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写此方法来衡量每张图片的大小，默认返回图片数量。
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.recommend_card, parent, false);
        final RecommendAdapter.ViewHolder holder = new RecommendAdapter.ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Goods goods = mRecommend.get(position);
                Intent intent = new Intent(mContext, ShowActivity.class);//跳转到详情页面
                intent.putExtra("searchGoods",goods);
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
        holder.recommend_score.setText("评分："+recommend.getStarRating());
        setItemBitmaps(recommend,holder);
    }
    @Override
    public int getItemCount(){
        return mRecommend.size();
    }
    public Bitmap getBitmapFromMemoryCache(String url) {
        return lruCache.get(url);
    }
    public void setItemBitmaps(final Goods goods, final ViewHolder holder) {
        final BmobFile bmobfile = goods.getPic1();
        if (bmobfile != null) {
            File saveFile = new File(mContext.getExternalFilesDir(null), bmobfile.getFilename());
            Bitmap cache = getBitmapFromMemoryCache(bmobfile.getUrl());
            if (cache != null) {//内存缓存
                holder.recommendImage.setImageBitmap(cache);
            } else {
                String filePath = saveFile.getPath();
                final Bitmap localCache = PictureTool.showImage(filePath);
                if (localCache != null) {//本地缓存
                    holder.recommendImage.setImageBitmap(localCache);
                    lruCache.put(bmobfile.getUrl(), localCache);
                } else {//请求网络
                    bmobfile.download(saveFile, new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                goods.setPath(s, 0);
                                //  holder.articleImage.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(goods.getPath(0), 300, 300));
                                Bitmap bitmap = PictureTool.showImage(goods.getPath(0));
                                holder.recommendImage.setImageBitmap(bitmap);
                                lruCache.put(bmobfile.getUrl(), bitmap);
                            } else
                                Toast.makeText(mContext, e.getErrorCode(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        }
    }
}