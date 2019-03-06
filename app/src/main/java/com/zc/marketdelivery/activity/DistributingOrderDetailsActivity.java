package com.zc.marketdelivery.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.adapter.OrderItemAdapter;
import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.bean.Order;
import com.zc.marketdelivery.utils.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DistributingOrderDetailsActivity extends AppCompatActivity {
    private TextView tv_order_detail_state,tv_order_detail_name;
    private RecyclerView rv_order_detail_shops_detail;
    private TextView tv_order_detail_result;
    private Button btn_order_detail_connectShops;
    private TextView tv_order_detail_numberOfOrder,tv_order_detail_createOrderTime,tv_order_detail_wayOfPay, tv_order_detail_address;
    // 数据
    private List<Good> goods;
    private Order order;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributing_order_details);
        mContext = this;
        bindViews();
        Intent intent = getIntent();
        order = (Order) intent.getExtras().getSerializable("order");
        new GoodsTask().execute();
        initViews();
    }



    private void bindViews() {
        tv_order_detail_state = (TextView) findViewById(R.id.tv_order_detail2_state);
        tv_order_detail_name = (TextView) findViewById(R.id.tv_order_detail2_name);
        rv_order_detail_shops_detail = (RecyclerView) findViewById(R.id.rv_order_detail2_shops_detail);
        tv_order_detail_result = (TextView) findViewById(R.id.tv_order_detail2_result);
        btn_order_detail_connectShops = (Button) findViewById(R.id.btn_order_detail2_connectShops);
        tv_order_detail_numberOfOrder = (TextView) findViewById(R.id.tv_order_detail2_numberOfOrder);
        tv_order_detail_createOrderTime = (TextView) findViewById(R.id.tv_order_detail2_createOrderTime);
        tv_order_detail_wayOfPay = (TextView) findViewById(R.id.tv_order_detail2_wayOfPay);
        tv_order_detail_address = (TextView) findViewById(R.id.tv_order_detail2_address);
    }

    private void initViews() {
        if (order.getState()){
            tv_order_detail_state.setText("订单已完成");
        }
        else {
            tv_order_detail_state.setText("订单配送中");
        }
        tv_order_detail_name.setText(order.getName());
        tv_order_detail_result.setText("共"+ order.getGoodsNumber()+"件商品" + "共"+order.getPrice()+"元");
        tv_order_detail_address.setText(order.getAddress());
        btn_order_detail_connectShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                startActivity(call);
                            }
                        })
                        .create().show();
            }
        });
        tv_order_detail_numberOfOrder.setText(order.getGoodsNumber());
        tv_order_detail_createOrderTime.setText(order.getTimePlaced());
        tv_order_detail_wayOfPay.setText(order.getWayOfPay());
        rv_order_detail_shops_detail.setLayoutManager(new LinearLayoutManager(mContext));
        rv_order_detail_shops_detail.setAdapter(new OrderItemAdapter(mContext, order, goods));


    }


    class GoodsTask extends AsyncTask<String, String, List<Good>> {

        @Override
        protected List<Good> doInBackground(String... strings) {
            // 查询所有订单里面的ID号
            String baseUrl = "http://13.250.1.159:8000/api/goods/";
            List<Good> goods = new ArrayList<>();
            try {
                OkHttpClient client = new OkHttpClient();
                String[] IDs = order.getGoodsIDs().split("\\+");
                for (int i=1;i<IDs.length;i++) {
                    String nowURl = baseUrl + IDs[i]+ "/";
                    Request request = new Request.Builder().url(nowURl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        goods.add(JsonUtil.parseGoodJsonObject(response.body().string()));
                    } else {
                        return null;
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
            return goods;
        }

        @Override
        protected void onPostExecute(List<Good> g) {
            super.onPostExecute(g);
            goods = g;
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(mContext, order, g);
            rv_order_detail_shops_detail.setAdapter(orderItemAdapter);
            orderItemAdapter.notifyDataSetChanged();

        }
    }
}
