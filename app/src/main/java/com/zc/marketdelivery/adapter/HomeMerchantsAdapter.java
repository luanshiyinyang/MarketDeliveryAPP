package com.zc.marketdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.hedgehog.ratingbar.RatingBar;
import com.zc.marketdelivery.R;
import com.zc.marketdelivery.activity.ShoppingCartActivity;
import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.utils.JsonUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeMerchantsAdapter extends RecyclerView.Adapter<HomeMerchantsAdapter.ViewHolder> {
    private List<Merchant> mData;
    private Context mContext;
    private Merchant merchant;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        ImageView icon;
        TextView name;
        RatingBar rating;
        TextView score;
        TextView sales;
        TextView priceSending;
        TextView priceSend;
        TextView address;
        TextView timeSended;
        TextView distance;
        public ViewHolder(View view){
            super(view);
            myView = view;
            icon = (ImageView) view.findViewById(R.id.iv_merchant_icon);
            name = (TextView) view.findViewById(R.id.tv_merchant_name) ;
            rating = (RatingBar) view.findViewById(R.id.rb_merchant_rating);
            score = (TextView) view.findViewById(R.id.tv_merchant_score);
            sales = (TextView) view.findViewById(R.id.tv_merchant_sales);
            priceSending = (TextView) view.findViewById(R.id.tv_merchant_priceSending);
            priceSend = (TextView) view.findViewById(R.id.tv_merchant_priceSend);
            address = (TextView) view.findViewById(R.id.tv_merchant_address);
            timeSended = (TextView) view.findViewById(R.id.tv_merchant_timeSended);
            distance = (TextView) view.findViewById(R.id.tv_merchant_distance);

        }
    }

    public HomeMerchantsAdapter(List<Merchant> Data, Context context){
        this.mContext = context;
        mData = Data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_merchant, null, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                // 获得该商家的起送费
                merchant = mData.get(position);
                new GoodListTask().execute();

            }
        });
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Merchant merchant = mData.get(position);
        holder.icon.setImageResource(R.drawable.baidu);
        holder.name.setText(merchant.getName());
        holder.rating.setStar(merchant.getRating());
        holder.score.setText(String.valueOf(merchant.getRating()));
        holder.sales.setText("月售"+String.valueOf(merchant.getSales())+"单");
        holder.priceSending.setText(String.valueOf(merchant.getPriceSending()));
        holder.priceSend.setText(String.valueOf(merchant.getPriceSend()));
        holder.address.setText(merchant.getAddress());
        holder.timeSended.setText(merchant.getTimeSended());
        holder.distance.setText(String.valueOf(merchant.getDistance())+"km");
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class GoodListTask extends AsyncTask<String, String, List<Good>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Good> doInBackground(String... strings) {
            try {
                String baseUrl = "http://111.231.137.51:8000/api/goods.json";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(baseUrl).build();
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()){
                    if (response.body() != null){
                        return JsonUtil.parseGoodJsonList(response.body().string());
                    }
                    else {
                        return null;
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Good> goods) {
            super.onPostExecute(goods);
            if (goods != null){
                Intent intent = new Intent(mContext,ShoppingCartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) goods);
                bundle.putSerializable("merchant", (Serializable) merchant);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
            else {
                Toast.makeText(mContext, "空数据返回", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

