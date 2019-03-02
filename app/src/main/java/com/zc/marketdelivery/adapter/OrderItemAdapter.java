package com.zc.marketdelivery.adapter;

/**
 * Created by 16957 on 2018/7/21.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zc.marketdelivery.R;

import java.util.List;
import java.util.Map;


public class OrderItemAdapter extends BaseAdapter {
    String[] data;
    Context context;

    public OrderItemAdapter(Context context, String[] data) {
        this.context = context;
        this.data = data;
    }
    //使Listview不可点击
    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        if(data==null) return 0;
        else return data.length/2;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shops,parent,false);
            viewHolder.tv_shopName = (TextView) convertView.findViewById(R.id.tv_item_shops_name);
            viewHolder.tv_shopNumber = (TextView) convertView.findViewById(R.id.tv_item_shops_number);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();

        }
        //设置内容
        if(position<data.length/2){
        int i = 2*position;
        viewHolder.tv_shopName.setText(""+data[i]);
        viewHolder.tv_shopNumber.setText(""+data[i+1]);}
        return convertView;


    }

    class ViewHolder{
        TextView tv_shopName;
        TextView tv_shopNumber;



    }

}
