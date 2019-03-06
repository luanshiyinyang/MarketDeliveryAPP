package com.zc.marketdelivery.adapter;

/**
 * Created by 16957 on 2018/7/21.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.zc.marketdelivery.R;
import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.bean.Order;

import java.util.List;


public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private Context context;
    private Order order;
    private List<Good> goods;
    private Good good;

    public OrderItemAdapter(Context context, Order order, List<Good> goods) {
        this.context = context;
        this.order = order;
        this.goods = goods;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shops, null, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        good = goods.get(position);
        String [] goodInfo = order.getGoodNames().split("\\+");
        holder.tv_goodName.setText(goodInfo[2*position+1]);
        holder.tv_goodNumber.setText(goodInfo[2*position+2]);

    }

    @Override
    public int getItemCount() {
        if(goods != null){
            return goods.size();
        }
        else {
            return 0;
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        TextView tv_goodName;
        TextView tv_goodNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            tv_goodName = (TextView) itemView.findViewById(R.id.tv_item_good_name);
            tv_goodNumber = (TextView) itemView.findViewById(R.id.tv_item_good_number);

        }
    }

}
