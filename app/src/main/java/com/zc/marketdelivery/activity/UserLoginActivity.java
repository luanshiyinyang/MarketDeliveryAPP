package com.zc.marketdelivery.activity;
/*
登录界面逻辑
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.bean.User;
import com.zc.marketdelivery.task.UserLoginTask;
import com.zc.marketdelivery.task.UserRegisterTask;
import com.zc.marketdelivery.task.RequestForUserListTask;
import com.zc.marketdelivery.utils.InputMethodUtil;
import com.zc.marketdelivery.utils.Md5Util;


public class UserLoginActivity extends AppCompatActivity{
    private Context mContext;
    private TextInputEditText etUserPhone;
    private TextInputEditText etUserPassword;
    private Button btnUserLogin;
    private Button btnUserRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mContext = this;
        bindViews();
        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputMethodUtil(mContext).hideInputMethod();
                userLogin();
            }
        });
        btnUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputMethodUtil(mContext).hideInputMethod();
                userRegister();
            }
        });



    }



    private void bindViews() {
        etUserPhone = (TextInputEditText) findViewById(R.id.et_user_phone);
        etUserPassword = (TextInputEditText) findViewById(R.id.et_user_password);
        btnUserLogin = (Button) findViewById(R.id.btn_user_login);
        btnUserRegister = (Button)findViewById(R.id.btn_user_register);
    }

    private void userLogin(){
        String phone = etUserPhone.getText().toString().trim();
        String password = Md5Util.md5(etUserPassword.getText().toString().trim());
        UserLoginTask userLoginTask = new UserLoginTask(mContext, UserLoginActivity.this);
        userLoginTask.execute(new User(phone, password));
    }

    private void userRegister() {
        String phone = etUserPhone.getText().toString().trim();
        String password = etUserPassword.getText().toString().trim();
        if (phone.length() != 11){
            Toast.makeText(mContext, "手机号位数不合适", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() <= 8){
            Toast.makeText(mContext, "密码位数不足", Toast.LENGTH_SHORT).show();
        }
        else {
            UserRegisterTask userRegisterTask = new UserRegisterTask(mContext, UserLoginActivity.this);
            userRegisterTask.execute(new User(phone, Md5Util.md5(password)));
        }


    }
}

