package com.zc.marketdelivery.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.zc.marketdelivery.activity.UserLoginActivity;
import com.zc.marketdelivery.bean.User;
import com.zc.marketdelivery.manager.UserStateManager;
import com.zc.marketdelivery.utils.JsonUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserLoginTask extends AsyncTask<User, String, String> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private UserLoginActivity activity;

    public UserLoginTask(Context mContext, UserLoginActivity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    protected String doInBackground(User... users) {
        try {
            User user = users[0];
            String baseUrl = "http://13.250.1.159:8000/api/users";
            // 首先判断手机号是否已经注册
            OkHttpClient client = new OkHttpClient();
            Request request1 = new Request.Builder().url(baseUrl+".json").build();
            Response response1 = client.newCall(request1).execute();
            if (response1.isSuccessful() && response1.body() != null) {
                List<User> usersFromAPI = JsonUtil.parseUserJsonList(response1.body().string());
                if (usersFromAPI != null) {
                    int time = 0;
                    for (User u : usersFromAPI) {
                        time ++;
                        if (u.getPhone().equals(user.getPhone())) {
                            if (u.getPassword().equals(user.getPassword())){

                                return "登录成功"+"+"+String.valueOf(u.getId())+"+"+user.getPhone();
                            }
                            else {
                                return "密码错误";
                            }

                        }
                    }
                    if (time == usersFromAPI.size()){
                        return "用户没有注册";
                    }
                }
            }
        }catch (IOException e){
            return "登录失败，网络异常";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null){
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
            if (s.split("\\+")[0].equals("登录成功")){
                new UserStateManager().updateUserState(s);
                activity.finish();
            }
        }
    }
}
