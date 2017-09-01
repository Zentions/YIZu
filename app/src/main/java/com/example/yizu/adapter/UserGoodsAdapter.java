package com.example.yizu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.GoodsItemActivity;
import com.example.yizu.R;
import com.example.yizu.ShowActivity;
import com.example.yizu.UserGoodsActivity;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by 10591 on 2017/7/27.
 */

public class UserGoodsAdapter extends RecyclerView.Adapter<UserGoodsAdapter.ViewHolder> {
    private Context mContext;
    private List<Goods> mGoodsList;
    private LruCache<String, Bitmap> lruCache;
    static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView Goodstate;
        TextView Goodsname;
        TextView Classification;
        ImageView itemImage;
        RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            Goodsname=(TextView)itemView.findViewById(R.id.itemName);
            Goodstate=(TextView)itemView.findViewById(R.id.itemState);
            Classification=(TextView)itemView.findViewById(R.id.itemClassification);
            itemImage=(ImageView) itemView.findViewById(R.id.itemImage);
            layout = (RelativeLayout)itemView.findViewById(R.id.goodsitem);
        }

    }
    public UserGoodsAdapter(List<Goods> GoodsList){
        mGoodsList=GoodsList;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.goods_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                final Goods goods = mGoodsList.get(position);
                Intent intent=new Intent(mContext,GoodsItemActivity.class);
                intent.putExtra("myGoods",goods);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods goods=mGoodsList.get(position);
        holder.Goodsname.setText("物品名称:"+goods.getGoodsName());
        holder.Goodstate.setText("状态:"+goods.getState());
        holder.Classification.setText("分类:"+goods.getClassification());
      ;setItemBitmaps(goods,holder);
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }
    public Bitmap getBitmapFromMemoryCache(String url) {
        return lruCache.get(url);
    }
    public void setItemBitmaps(final Goods goods, final ViewHolder holder) {
        final BmobFile[] bmobfile = new BmobFile[3];
        bmobfile[0] = goods.getPic1();
        bmobfile[1] = goods.getPic2();
        bmobfile[2] = goods.getPic3();
        for(int i = 0;i<3;i++){
            if(bmobfile[i]!=null){
                final int finalI = i;
                File saveFile = new File(mContext.getExternalFilesDir(null), bmobfile[i].getFilename());
                Bitmap cache = getBitmapFromMemoryCache(bmobfile[i].getUrl());
                if (cache != null) {//内存缓存
                    goods.setPath(saveFile.getPath(), finalI);
                    if(finalI==0){
                        holder.itemImage.setImageBitmap(cache);
                    }
                } else {
                    String filePath = saveFile.getPath();
                    final Bitmap localCache = PictureTool.showImage(filePath);
                    if (localCache != null) {//本地缓存
                        goods.setPath(filePath, finalI);
                        if(finalI==0) {
                            holder.itemImage.setImageBitmap(localCache);
                            lruCache.put(bmobfile[i].getUrl(), localCache);
                        }

                    } else {//请求网络

                        bmobfile[i].download(saveFile, new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    goods.setPath(s, finalI);
                                    //  holder.articleImage.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(goods.getPath(0), 300, 300));
                                    if(finalI==0){
                                        Bitmap bitmap = PictureTool.showImage(goods.getPath(finalI));
                                        holder.itemImage.setImageBitmap(bitmap);
                                        lruCache.put(bmobfile[finalI].getUrl(), bitmap);
                                    }

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

}
