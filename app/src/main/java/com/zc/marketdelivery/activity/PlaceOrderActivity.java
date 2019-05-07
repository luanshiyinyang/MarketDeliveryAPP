package com.zc.marketdelivery.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.adapter.PlaceOrderItemsAdapter;
import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.bean.Order;
import com.zc.marketdelivery.bean.User;
import com.zc.marketdelivery.manager.UserStateManager;
import com.zc.marketdelivery.utils.JsonUtil;
import com.zc.marketdelivery.utils.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlaceOrderActivity extends AppCompatActivity {
    private Context mContext;
    private Order nowOrder;
    private TextView tvTitle;
    private LinearLayout ll;
    private TextView tvAddress, tvTimePlaced, tvCost, tvName;
    private Button btnSubmit;
    private RecyclerView rvPlaceOrderItems;
    private Spinner spinnerTimeOfArrived, spinnerWayOfPay;
    private List<Good> data;
    private double cost;
    private Merchant merchant;
    private String phone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UserInfoTask().execute();
        setContentView(R.layout.activity_place_order);
        mContext = this;

        if (data == null){
            data = new ArrayList<>();
        }
        data = (List<Good>) getIntent().getExtras().getSerializable("data");
        bindViews();
        initData();
        initViews();


    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        nowOrder = new Order();
        nowOrder.setTimePlaced(TimeUtil.getNowTime());

        merchant = (Merchant) bundle.getSerializable("merchant");
        phone = intent.getStringExtra("phone");
        nowOrder.setName(merchant.getName());
        nowOrder.setState(false);
        nowOrder.setPhone(merchant.getPhone());
        nowOrder.setPrice(getIntent().getDoubleExtra("cost", 0.0));
        nowOrder.setUserID(Long.valueOf(getIntent().getStringExtra("userID")));

        if (data != null){
            int goodNumber = 0;
            StringBuilder goodIDs = new StringBuilder();
            for (Good good:data){
                goodNumber += good.getLocalCount();
                goodIDs.append("+").append(good.getId());

            }

            nowOrder.setGoodsNumber(String.valueOf(goodNumber));
            nowOrder.setGoodsIDs(goodIDs.toString());
            nowOrder.setAddress((String) tvAddress.getText());
            String goodNames = "";
            if (data.size() > 2){
                for (int i =0;i<data.size();i++){
                    goodNames += ("+" + data.get(i).getName());
                    goodNames += ("+" + data.get(i).getLocalCount());
                }
            }
            else {
                for (int i=0;i<data.size();i++){
                    goodNames += ("+" + data.get(i).getName());
                    goodNames += ("+" + data.get(i).getLocalCount());
                }
                for (int j=data.size();j<3;j++){
                    goodNames += ("+" + "商品名称");
                    goodNames += ("+" + "0");
                }
            }
            nowOrder.setGoodNames(goodNames);

        }

    }


    private void bindViews() {
        tvTitle = (TextView) findViewById(R.id.tv_order_place_title);
        ll = (LinearLayout) findViewById(R.id.ll_address_choose);
        tvAddress = (TextView) findViewById(R.id.tv_order_place_address);
        tvName = (TextView) findViewById(R.id.tv_order_place_name);
        tvTimePlaced = (TextView) findViewById(R.id.tv_order_place_time_placed);
        tvCost = (TextView) findViewById(R.id.tv_order_place_cost);
        rvPlaceOrderItems = (RecyclerView) findViewById(R.id.rv_place_order_items);
        rvPlaceOrderItems.setLayoutManager(new LinearLayoutManager(mContext));
        rvPlaceOrderItems.setAdapter(new PlaceOrderItemsAdapter(mContext, data));
        spinnerTimeOfArrived = (Spinner) findViewById(R.id.spinner_time_of_arrived);
        spinnerWayOfPay = (Spinner) findViewById(R.id.spinner_way_of_pay);
        btnSubmit = (Button) findViewById(R.id.btn_order_place_submit);
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseAddressDialog();
                tvName.setText(user.getName() + "  " + user.getPhone());
                nowOrder.setAddress(tvAddress.getText() +  "&" + tvName.getText());
            }
        });
        tvTimePlaced.setText(nowOrder.getTimePlaced());

        tvCost.setText(String.valueOf(nowOrder.getPrice()));
        initSpinner();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OrderPlaceTask().execute(nowOrder);
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("PlaceResult", "finish");
                //设置返回数据
                PlaceOrderActivity.this.setResult(1, intent);
                //关闭Activity
                finish();
            }
        });

    }

    private void initSpinner() {
        final String [] timeExpectedChoices = {"立即送达", "稍后送达"};
        final String [] wayPayChoices = {"在线支付", "货到付款"};
        spinnerTimeOfArrived.setAdapter(new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, timeExpectedChoices));
        spinnerTimeOfArrived.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nowOrder.setTimeExpected(timeExpectedChoices[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWayOfPay.setAdapter(new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, wayPayChoices));
        spinnerWayOfPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nowOrder.setWayOfPay(wayPayChoices[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showChooseAddressDialog() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.show();
        final TextView tvAddress1 = (TextView) bottomDialog.findViewById(R.id.tv_user_choose_address1);
        tvAddress1.setText(user.getAddress());
        final TextView tvAddress2 = (TextView) bottomDialog.findViewById(R.id.tv_user_choose_address2);
        final TextView tvAddress3 = (TextView) bottomDialog.findViewById(R.id.tv_user_choose_address3);
        final TextView tvAddress4 = (TextView) bottomDialog.findViewById(R.id.tv_user_choose_address4);
        final TextView tvAddress5 = (TextView) bottomDialog.findViewById(R.id.tv_user_choose_address5);
        tvAddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(tvAddress1.getText());
                bottomDialog.dismiss();
            }
        });
        tvAddress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(tvAddress2.getText());
                bottomDialog.dismiss();
            }
        });
        tvAddress3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(tvAddress3.getText());
                bottomDialog.dismiss();
            }
        });
        tvAddress4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(tvAddress4.getText());
                bottomDialog.dismiss();
            }
        });
        tvAddress5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddress.setText(tvAddress5.getText());
                bottomDialog.dismiss();
            }
        });
    }

    class OrderPlaceTask extends AsyncTask<Order, String, String>{

        @Override
        protected String doInBackground(Order... orders) {
            Order order = orders[0];
            String baseUrl = "http://111.231.137.51:8000/api/orders";
            try {
                OkHttpClient client = new OkHttpClient();
                String jsonData = JsonUtil.jsonOrderToString(order);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonData);
                Request request = new Request.Builder().url(baseUrl+"/").post(requestBody).build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    return "下单成功";
                }
            }catch (IOException e){
                e.printStackTrace();
                return "网络异常";
            }
            return "出现错误";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        }
    }

    class UserInfoTask extends AsyncTask<String, String, User>{

        @Override
        protected User doInBackground(String... strings) {
            String baseUrl = "http://111.231.137.51:8000/api/users/";
            String userID = new UserStateManager().getUserID();
            if (userID.equals("0")){
                return null;
            }
            else {
                baseUrl += (userID + "/");
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        return JsonUtil.parseUserJsonObject(response.body() != null ? response.body().string() : null);
                    }
                    return null;

                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(User u) {
            super.onPostExecute(user);
            if (u == null){
                Toast.makeText(mContext, "请先登录或确认网络连接", Toast.LENGTH_SHORT).show();
            }
            else {
                user = u;
            }
        }
    }


}
