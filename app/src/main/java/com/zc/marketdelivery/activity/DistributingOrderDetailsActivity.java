package com.zc.marketdelivery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.adapter.OrderItemAdapter;

public class DistributingOrderDetailsActivity extends AppCompatActivity {
    private TextView tv_order_detail_state,tv_order_detail_name;
    private ListView lv_order_detail_shops_detail;
    private TextView tv_order_detail_result;
    private Button btn_order_detail_connectShops;
    private TextView tv_order_detail_numberOfOrder,tv_order_detail_createOrderTime,tv_order_detail_wayOfPay;
    String data;
    String [] datas;
    String order;
    String [] orders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributing_order_details);
        findViews();
        Intent intent = getIntent();

        data = intent.getStringExtra("needValue");
        datas = data.split("&");
        order = intent.getStringExtra("needList");
        orders = order.split("&");
        setViews();
    }



    private void findViews() {
        tv_order_detail_state = (TextView) findViewById(R.id.tv_order_detail2_state);
        tv_order_detail_name = (TextView) findViewById(R.id.tv_order_detail2_name);
        lv_order_detail_shops_detail = (ListView)findViewById(R.id.lv_order_detail2_shops_detail);
        tv_order_detail_result = (TextView) findViewById(R.id.tv_order_detail2_result);
        btn_order_detail_connectShops = (Button) findViewById(R.id.btn_order_detail2_connectShops);
        tv_order_detail_numberOfOrder = (TextView) findViewById(R.id.tv_order_detail2_numberOfOrder);
        tv_order_detail_createOrderTime = (TextView) findViewById(R.id.tv_order_detail2_createOrderTime);
        tv_order_detail_wayOfPay = (TextView) findViewById(R.id.tv_order_detail2_wayOfPay);
    }
    private void setViews() {
        tv_order_detail_state.setText(datas[0]);
        tv_order_detail_name.setText(datas[1]);
        tv_order_detail_result.setText(datas[2]);
        btn_order_detail_connectShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DistributingOrderDetailsActivity.this);
                builder.setTitle("联系商家")
                        .setMessage("将拨号给"+datas[9])
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
                                call.setData(Uri.parse("tel:"+datas[9]));
                                startActivity(call);
                            }
                        })
                        .create().show();
            }
        });
        tv_order_detail_numberOfOrder.setText(datas[6]);
        tv_order_detail_createOrderTime.setText(datas[7]);
        tv_order_detail_wayOfPay.setText(datas[8]);
        lv_order_detail_shops_detail.setAdapter(new OrderItemAdapter(DistributingOrderDetailsActivity.this,orders));


    }
}
