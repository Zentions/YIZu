package com.example.yizu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.GoodsItemActivity;
import com.example.yizu.R;
import com.example.yizu.ShowActivity;
import com.example.yizu.UserGoodsActivity;
import com.example.yizu.bean.Goods;
import com.example.yizu.tool.PictureTool;

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
    static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView Goodstate;
        TextView Goodsname;
        TextView Classification;
        ImageView itemImage;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            Goodsname=(TextView)itemView.findViewById(R.id.itemName);
            Goodstate=(TextView)itemView.findViewById(R.id.itemState);
            Classification=(TextView)itemView.findViewById(R.id.itemClassification);
            itemImage=(ImageView) itemView.findViewById(R.id.itemImage);
            layout = (LinearLayout)itemView.findViewById(R.id.goodsitem);
        }

    }
    public UserGoodsAdapter(List<Goods> GoodsList){
        mGoodsList=GoodsList;
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
                Intent intent=new Intent(mContext,GoodsItemActivity.class);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods goods=mGoodsList.get(position);
        holder.Goodsname.setText(goods.getGoodsName());
        holder.Goodstate.setText(goods.getState());
        holder.Classification.setText(goods.getClassification());
      ;downImage(goods,holder);
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }
    void downImage(final Goods goods, final ViewHolder holder){
        BmobFile bmobfile = goods.getPic1();
        if(bmobfile!= null) {
            bmobfile.download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        goods.setPath(s, 0);
                        //  holder.articleImage.setImageBitmap(PictureTool.decodeSampledBitmapFromResource(goods.getPath(0), 300, 300));
                        holder.itemImage.setImageBitmap(PictureTool.showImage(goods.getPath(0)));
                    } else Toast.makeText(mContext, e.getErrorCode(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }

}
