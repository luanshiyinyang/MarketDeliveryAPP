package com.zc.marketdelivery.fragment;

/**
 * Created by 16957 on 2018/7/16.
 * 菜单页面的fragment
 */
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.adapter.OrdersAdapter;
import com.zc.marketdelivery.bean.Order;
import com.zc.marketdelivery.manager.UserStateManager;
import com.zc.marketdelivery.utils.JsonUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OrdersFragment extends Fragment {

    //上下文
    Context mContext;
    //数据列表，仅用于测试
    private List<Order> orders;
    private  RecyclerView rvOrders;

    //创建view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //获得上下文
        mContext = getActivity();
        orders = new ArrayList<>();
        //填充布局
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        bindViews(view);

        initView();

        return view ;
    }



    private void bindViews(View view) {
        Toolbar tb = (Toolbar)view.findViewById(R.id.toolbar_order);
        tb.setTitle("订单");
        tb.setTitleTextColor(Color.BLACK);
        rvOrders = (RecyclerView) view.findViewById(R.id.rv_orders);


    }

    private void initView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(mContext));
        new OrdersTask(mContext, rvOrders).execute();
        OrdersAdapter ordersAdapter = new OrdersAdapter(mContext, orders);
        rvOrders.setAdapter(ordersAdapter);
    }

    class OrdersTask extends AsyncTask<String, String, List<Order>>{
        private Context mContext;
        private RecyclerView rvOrders;

        public OrdersTask(Context mContext, RecyclerView rvOrders) {
            this.mContext = mContext;
            this.rvOrders = rvOrders;
        }

        @Override
        protected List<Order> doInBackground(String... strings) {
            String userID = new UserStateManager().getUserID();
            if (userID.equals("0")){
                Log.i("mmm", userID);
                return null;
            }
            else {

                String baseUrl = "http://13.250.1.159:8000/api/orders/";

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String jsonData = response.body().string();
                            return JsonUtil.parseOrderJsonList(jsonData);

                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

        }

        @Override
        protected void onPostExecute(List<Order> o) {
            super.onPostExecute(o);
            String userID = new UserStateManager().getUserID();

            if (o != null){
                for (Order item:o){
                    if (userID.equals(String.valueOf(item.getUserID()))){
                        orders.add(item);
                    }
                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                rvOrders.setLayoutManager(layoutManager);
                OrdersAdapter adapter = new OrdersAdapter(mContext, orders);
                rvOrders.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(mContext, "登录后，请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }
    }




}

