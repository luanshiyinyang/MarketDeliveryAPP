package com.zc.marketdelivery.adapter;
/**
 * Created by 16957 on 2018/7/16.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.bean.OrderItem;

import java.util.List;


public class OrderAdapter extends BaseAdapter {
    List<OrderItem> data;
    Context context;

    public OrderAdapter(Context context, List<OrderItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data==null) return 0;
        else return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
            viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_order_image);
            viewHolder.tv_name =(TextView) convertView.findViewById(R.id.tv_order_name);
            viewHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_order_state);
            viewHolder.tv_goodsName1=(TextView)convertView.findViewById(R.id.tv_goodsName1);
            viewHolder.tv_goodsName2=(TextView)convertView.findViewById(R.id.tv_goodsName2);
            viewHolder.tv_goodsName3=(TextView)convertView.findViewById(R.id.tv_goodsName3);
            viewHolder.tv_goodsNumber1=(TextView)convertView.findViewById(R.id.tv_goodsNumber1);
            viewHolder.tv_goodsNumber2=(TextView)convertView.findViewById(R.id.tv_goodsNumber2);
            viewHolder.tv_goodsNumber3=(TextView)convertView.findViewById(R.id.tv_goodsNumber3);
            viewHolder.tv_result = (TextView) convertView.findViewById(R.id.tv_order_result);
            viewHolder.btn_cancelOrder = (Button) convertView.findViewById(R.id.btn_cancelOrder);
            viewHolder.btn_connectShops = (Button) convertView.findViewById(R.id.btn_connectShops);
            viewHolder.btn_notifyOrder = (Button)convertView.findViewById(R.id.btn_notifyOrder);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();

        }
        //设置内容
        viewHolder.iv_icon.setImageResource(data.get(position).getImageID());
        viewHolder.tv_name.setText(data.get(position).getName());
        viewHolder.tv_state.setText(data.get(position).getState());
        viewHolder.tv_goodsName1.setText(data.get(position).getOrders().get(0).getKey());
        viewHolder.tv_goodsName2.setText(data.get(position).getOrders().get(1).getKey());
        viewHolder.tv_goodsName3.setText(data.get(position).getOrders().get(2).getKey());
        viewHolder.tv_goodsNumber1.setText(""+data.get(position).getOrders().get(0).getValue());
        viewHolder.tv_goodsNumber2.setText(""+data.get(position).getOrders().get(1).getValue());
        viewHolder.tv_goodsNumber3.setText(""+data.get(position).getOrders().get(2).getValue());


        viewHolder.tv_result.setText(data.get(position).getResult());

        viewHolder.btn_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"hyysb",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.btn_connectShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("联系商家")
                        .setMessage("将拨号给"+data.get(position).getPhoneNumberOfshops())
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
                                call.setData(Uri.parse("tel:"+data.get(position).getPhoneNumberOfshops()));
                                context.startActivity(call);
                            }
                        })
                        .create().show();
            }
        });
        viewHolder.btn_notifyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("催单成功")
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                    })
                        .create().show();
            }
        });
        return convertView;


    }

        class ViewHolder{
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_state;
            TextView tv_result;
            TextView tv_goodsName1;
            TextView tv_goodsNumber1;
            TextView tv_goodsName2;
            TextView tv_goodsNumber2;
            TextView tv_goodsName3;
            TextView tv_goodsNumber3;
            Button btn_cancelOrder;
            Button btn_connectShops;
            Button btn_notifyOrder;


        }

}
