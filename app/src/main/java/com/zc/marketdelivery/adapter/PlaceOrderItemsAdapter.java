package com.zc.marketdelivery.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.bean.Good;

import java.util.List;

public class PlaceOrderItemsAdapter extends RecyclerView.Adapter<PlaceOrderItemsAdapter.ViewHolder> {
    private Context mContext;
    private List<Good> mData;

    public PlaceOrderItemsAdapter(Context mContext, List<Good> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_order_goods, null, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Good good = mData.get(position);
        holder.name.setText(good.getName());
        holder.number.setText("*" + String.valueOf(good.getLocalCount()));

    }

    @Override
    public int getItemCount() {
        if (mData == null){
            return 0;

        }
        else {
            return mData.size();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        TextView name;
        TextView number;

        public ViewHolder(View view) {
            super(view);
            myView = view;
            name = (TextView) view.findViewById(R.id.tv_place_order_name);
            number = (TextView) view.findViewById(R.id.tv_place_order_number);
        }
    }




}
