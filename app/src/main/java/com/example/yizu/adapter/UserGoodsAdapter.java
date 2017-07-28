package com.example.yizu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yizu.R;
import com.example.yizu.bean.Goods;

import java.util.List;

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
        TextView INtroduce;
        TextView ALLMONEY;
        TextView ALLTIMES;
        public ViewHolder(View itemView) {
            super(itemView);
            Goodsname=(TextView)itemView.findViewById(R.id.goodsname);
            Goodstate=(TextView)itemView.findViewById(R.id.goodstate);
            Classification=(TextView)itemView.findViewById(R.id.classification);
            INtroduce=(TextView)itemView.findViewById(R.id.introduce);
            ALLMONEY=(TextView)itemView.findViewById(R.id.allmoney);
            ALLTIMES=(TextView)itemView.findViewById(R.id.alltimes);

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
        Button button;
        button =(Button)view.findViewById(R.id.holdon);
        final ViewHolder holder = new ViewHolder(view);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                Goods record = mGoodsList.get(position);
                record.setState("无人租用");
            }
        });
        Button button1;
        button1=(Button)view.findViewById(R.id.end);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position1=holder.getAdapterPosition();
               Goods record1=mGoodsList.get(position1);
                record1.delete();
                record1.setState("已下架");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Goods record=mGoodsList.get(position);
        holder.Goodsname.setText(record.getGoodsName());
        holder.Goodstate.setText(record.getState());
        holder.INtroduce.setText(record.getDescription());
        holder.Classification.setText(record.getClassification());

    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }


}
