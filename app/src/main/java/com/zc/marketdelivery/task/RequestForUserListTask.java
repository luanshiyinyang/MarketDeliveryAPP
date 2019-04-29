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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestForUserListTask extends AsyncTask<String, String, User> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public RequestForUserListTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected User doInBackground(String... strings) {
        List<User> result;
        try {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "http://111.231.137.51:8000/api/";
            String nowUrl = baseUrl + "users" + ".json";
            Request request = new Request.Builder().url(nowUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                assert response.body() != null;
                result = JsonUtil.parseUserJsonList(response.body().string());
            }
            else {
                Toast.makeText(mContext, "无结果", Toast.LENGTH_SHORT).show();
                return null;
            }
        }catch (IOException e){
            return null;
        }
        assert result != null;

        for (User user:result){
            Log.i("ms",strings[0]);
            if (strings[0].equals(user.getPhone())){
                if (strings[1].equals(user.getPassword())){
                    Log.i("mss", "登录成功");
                }
                else {
                    Log.i("mss", "zcnb");
                }
                break;
            }
            else {

            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
    }
}
