package com.zc.marketdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.activity.DistributingOrderDetailsActivity;
import com.zc.marketdelivery.activity.FinishedOrderDetailsActivity;
import com.zc.marketdelivery.bean.Order;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private Context mContext;
    private List<Order> data;
    private Order order;

    public OrdersAdapter(Context mContext, List<Order> data) {
        this.mContext = mContext;
        this.data = data;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, null, false);
        final ViewHolder holder = new ViewHolder(view);
        if (data.size() == 0){
            Toast.makeText(mContext, "无订单数据", Toast.LENGTH_SHORT).show();
        }

        holder.myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), "你点击了第"+Integer.toString(position)+"项", Toast.LENGTH_LONG).show();
                order = data.get(position);
                if (order.getState()) {
                    Intent intent = new Intent(mContext, FinishedOrderDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, DistributingOrderDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order = data.get(holder.getAdapterPosition());
                new DeleteOrderTask().execute(order.getId());
            }
        });

        holder.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = data.get(holder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("联系商家")
                        .setMessage("将拨号给"+order.getPhone())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent call = new Intent();
                                call.setAction(Intent.ACTION_DIAL);
                                call.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                call.setData(Uri.parse("tel:"+order.getPhone()));
                                mContext.startActivity(call);
                            }
                        })
                        .create().show();
            }
        });

        holder.btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order = data.get(holder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("催单成功")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        order = data.get(position);
        holder.icon.setImageResource(R.drawable.baidu);
        holder.name.setText(order.getName());
        if (order.getState()) {
            holder.state.setText("订单已完成");
        }
        else {
            holder.state.setText("订单配送中");
        }

        holder.orderResult.setText("共"+ order.getGoodsNumber()+"件商品" + "共"+order.getPrice()+"元");
        String [] threeGoods = order.getGoodNames().split("\\+");
        holder.goodName1.setText(threeGoods[1]);
        holder.goodNumbers1.setText(threeGoods[2]);
        holder.goodName2.setText(threeGoods[3]);
        holder.goodNumbers2.setText(threeGoods[4]);
        holder.goodName3.setText(threeGoods[5]);
        holder.goodNumbers3.setText(threeGoods[6]);


    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        else {
            return data.size();
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View myView;
        ImageView icon;
        TextView name;
        TextView state;
        TextView goodName1, goodName2, goodName3;
        TextView goodNumbers1, goodNumbers2, goodNumbers3;
        TextView orderResult;
        Button btnCancel, btnConnect, btnNotify;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            icon = (ImageView) itemView.findViewById(R.id.iv_order_icon);
            name = (TextView) itemView.findViewById(R.id.tv_order_name);
            state = (TextView) itemView.findViewById(R.id.tv_order_state);
            goodName1 = (TextView) itemView.findViewById(R.id.tv_goodsName1);
            goodName2 = (TextView) itemView.findViewById(R.id.tv_goodsName2);
            goodName3 = (TextView) itemView.findViewById(R.id.tv_goodsName3);
            goodNumbers1 = (TextView) itemView.findViewById(R.id.tv_goodsNumber1);
            goodNumbers2 = (TextView) itemView.findViewById(R.id.tv_goodsNumber2);
            goodNumbers3 = (TextView) itemView.findViewById(R.id.tv_goodsNumber3);
            orderResult = (TextView) itemView.findViewById(R.id.tv_order_result);
            btnCancel = (Button) itemView.findViewById(R.id.btn_cancel_order);
            btnConnect = (Button) itemView.findViewById(R.id.btn_connect_merchant);
            btnNotify = (Button) itemView.findViewById(R.id.btn_notify_order);

        }
    }



    class DeleteOrderTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String baseUrl = "http://13.250.1.159:8000/api/orders/";
            if (strings[0] != null){
                baseUrl += strings[0];
                baseUrl += "/";
            }
            else {
                return "该订单不存在";
            }
           try {
               OkHttpClient client = new OkHttpClient();
               Request request = new Request.Builder().delete(RequestBody.create(null,"")).url(baseUrl).build();
               Response response = client.newCall(request).execute();
               if (response.isSuccessful()){
                   return "取消成功";
               }
               else {
                   return "网络异常，稍后重试";
               }

           }catch (IOException e){
               e.printStackTrace();
           }
           return "Error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        }
    }
}
