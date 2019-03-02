package com.zc.marketdelivery.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.activity.ShoppingCartActivity;
import com.zc.marketdelivery.R;

public class ShoppingCartTypeAdapter extends RecyclerView.Adapter<ShoppingCartTypeAdapter.ViewHolder> {
    public int selectTypeId;
    public ShoppingCartActivity activity;
    private List<Good> typeList;

    public ShoppingCartTypeAdapter(ShoppingCartActivity activity, List<Good> typeList) {
        this.activity = activity;
        this.typeList = typeList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_cart_type,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Good item = typeList.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if(typeList==null){
            return 0;
        }
        return typeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvCount,tvType;
        private Good item;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCount = (TextView) itemView.findViewById(R.id.tv_shopping_cart_each_count);
            tvType = (TextView) itemView.findViewById(R.id.tv_shopping_cart_each_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onTypeClicked(item.getTypeID());
                }
            });
        }

        public void bindData(Good item){
            this.item = item;
            tvType.setText(item.getKind());
            int count = activity.getSelectedGroupCountByTypeId(item.getTypeID());
            tvCount.setText(String.valueOf(count));
            if(count<1){
                tvCount.setVisibility(View.GONE);
            }else{
                tvCount.setVisibility(View.VISIBLE);
            }
            if(item.getTypeID()==selectTypeId){
                itemView.setBackgroundColor(Color.WHITE);
            }else{
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

        }
    }
}
