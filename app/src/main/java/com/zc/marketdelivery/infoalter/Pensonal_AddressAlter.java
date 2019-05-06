package com.zc.marketdelivery.infoalter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.bean.User;
import com.zc.marketdelivery.manager.UserStateManager;
import com.zc.marketdelivery.utils.JsonUtil;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Pensonal_AddressAlter extends AppCompatActivity {
    private EditText alter;
    private Button reset;
    private ImageButton back;
    private TextView tvNowAddress;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UserInfoTask().execute();
        setContentView(R.layout.activity_address_alter);
        bindViews();
        initViews();

    }



    private void bindViews() {
        alter = (EditText) findViewById(R.id.altertext2);
        reset = (Button) findViewById(R.id.confirmalter2);
        back  = (ImageButton)findViewById(R.id.personalback);
        textView = (TextView)findViewById(R.id.titlename);
        tvNowAddress = (TextView) findViewById(R.id.tv_alter_address_now_address);
    }

    private void initViews() {
        new UserInfoTask().execute();

        textView.setText("地址管理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                String value1=alter.getText().toString();
                intent1.putExtra("data_return",value1);
                setResult(RESULT_OK,intent1);
                finish();

            }
        });
    }

    class UserInfoTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                String userId = new UserStateManager().getUserID();
                String baseUrl = "http://111.231.137.51:8000/api/users/";
                if (userId.equals("0")){
                    return "请登录";
                }
                else {
                    baseUrl += (userId +"/");
                }
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url(baseUrl).build();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    if (response.body() != null) {
                        return response.body().string();
                    }
                    else {
                       return  "空数据";
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                return "网络异常";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                User user = JsonUtil.parseUserJsonObject(s);

                tvNowAddress.setText(user.getAddress());
            }catch (Exception e){
                e.printStackTrace();

            }
        }
    }
}
