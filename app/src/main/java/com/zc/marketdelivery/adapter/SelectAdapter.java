package com.zc.marketdelivery.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

import com.zc.marketdelivery.activity.ShoppingCartActivity;
import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.bean.GoodItem;
import com.zc.marketdelivery.R;


public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder>{
    private ShoppingCartActivity activity;
    private SparseArray<Good> dataList;
    private NumberFormat nf;
    private LayoutInflater mInflater;
    public SelectAdapter(ShoppingCartActivity activity, SparseArray<Good> dataList) {
        this.activity = activity;
        this.dataList = dataList;
        nf = NumberFormat.getCurrencyInstance(Locale.CHINA);
        nf.setMaximumFractionDigits(2);
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_goods,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Good item = dataList.valueAt(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if(dataList==null) {
            return 0;
        }
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Good item;
        private TextView tvCost,tvCount,tvAdd,tvMinus,tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_good_item_name);
            tvCost = (TextView) itemView.findViewById(R.id.tv_good_item_cost);
            tvCount = (TextView) itemView.findViewById(R.id.tv_good_item_count);
            tvMinus = (TextView) itemView.findViewById(R.id.tv_good_item_minus);
            tvAdd = (TextView) itemView.findViewById(R.id.tv_good_item_add);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_good_item_add:
                    activity.add(item, true);
                    break;
                case R.id.tv_good_item_minus:
                    activity.remove(item, true);
                    break;
                default:
                    break;
            }
        }

        public void bindData(Good item){
            this.item = item;
            tvName.setText(item.getName());
            tvCost.setText(nf.format(item.getLocalCount() * Double.valueOf(item.getPrice())));
            tvCount.setText(String.valueOf(item.getLocalCount()));
        }
    }
}
