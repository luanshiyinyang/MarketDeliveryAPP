package com.zc.marketdelivery.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.zc.marketdelivery.bean.User;
import com.zc.marketdelivery.utils.JsonUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserRegisterTask extends AsyncTask<User, String, String> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public UserRegisterTask(Context mContext) {
        this.mContext = mContext;
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
                    for (User u : usersFromAPI) {
                        if (u.getPhone().equals(user.getPhone())) {
                            return "手机号已经注册";
                        }
                    }
                }
            }

                String jsonData = JsonUtil.jsonUserToString(user);
                Log.i("msg", jsonData);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),jsonData);
                Request request2 = new Request.Builder().url(baseUrl+"/").post(requestBody).build();
                Response response2 = client.newCall(request2).execute();
                if (response2.isSuccessful()){
                    return "注册成功";
            }
        }catch (IOException e){
            return "注册失败，网络异常";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null){
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        }
    }
}
