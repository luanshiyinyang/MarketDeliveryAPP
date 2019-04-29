package com.zc.marketdelivery.infoalter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.zc.marketdelivery.utils.Md5Util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Pensonal_PasswordAlter extends AppCompatActivity{
    private EditText alter;
    private EditText raw;
    private EditText confirm;
    private Button reset;
    private ImageButton back;
    private TextView textView;
    private Context mContext;
    private String valid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_alter);
        mContext = this;
        raw = (EditText) findViewById(R.id.altertext);
        alter = (EditText) findViewById(R.id.password_alter);
        confirm =(EditText) findViewById(R.id.password_confirm);
        reset = (Button) findViewById(R.id.confirmalter4);
        back  = (ImageButton)findViewById(R.id.personalback);
        textView = (TextView)findViewById(R.id.titlename);
        textView.setText("修改密码");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valve1 = alter.getText().toString();
                String valve2 = confirm.getText().toString();

                if(valve1.equals("")){
                    Toast.makeText(Pensonal_PasswordAlter.this,"输入密码不可为空",Toast.LENGTH_SHORT).show();
                }
                if(!valve2.equals(valve1)){
                    Toast.makeText(Pensonal_PasswordAlter.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }
                if (valve1.length() < 8){
                    Toast.makeText(Pensonal_PasswordAlter.this,"密码位数不足",Toast.LENGTH_SHORT).show();
                }
                else{
                    new UserInfoTask().execute(raw.getText().toString());

                }
            }
        });

    }

    class UserInfoTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String baseUrl = "http://111.231.137.51:8000/api/users/";
            String userID = new UserStateManager().getUserID();
            if (userID.equals("0")) {
                return null;
            } else {
                baseUrl += (userID + "/");
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(baseUrl).build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            User user = JsonUtil.parseUserJsonObject(response.body().string());
                            if (user.getPassword().equals(Md5Util.md5(strings[0]))){
                                return "yes";
                            }
                            else {
                                return "no";
                            }
                        }
                    }
                    return null;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null){
                if (s.equals("no")){
                   Toast.makeText(mContext, "原密码输入错误", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent1 = new Intent();
                    intent1.putExtra("data_return", confirm.getText().toString());
                    setResult(RESULT_OK, intent1);
                    finish();
                }

            }
        }
    }
    }
