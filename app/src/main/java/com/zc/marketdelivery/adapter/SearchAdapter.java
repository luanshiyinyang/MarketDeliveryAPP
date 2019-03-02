package com.zc.marketdelivery.adapter;

/**
 * Created by 16957 on 2018/10/31.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zc.marketdelivery.bean.OrderItem;
import com.zc.marketdelivery.R;

import java.util.List;


public class SearchAdapter extends ArrayAdapter<String> {

    private Context mContext;
    public SearchAdapter(Context context, List<String> data){
        super (context, R.layout.item_search,data);
        this.mContext=context;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent) {
        String order = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_search,parent,false);
        TextView title = (TextView) view.findViewById(R.id.tv_search_result_title);
        if(title!=null)title.setText(order);
        return view;

    }
}



