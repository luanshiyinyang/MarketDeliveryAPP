package com.zc.marketdelivery.task;
/*
向服务器请求单个商家信息，按照id检索
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.utils.JsonUtil;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestForMerchantItemTask extends AsyncTask<String, String, Merchant> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    @SuppressLint("StaticFieldLeak")
    private TextView tv;

    public RequestForMerchantItemTask(Context context, TextView tv) {
        this.mContext = context;
        this.tv = tv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("msg", "开始寻找网络资源");
    }

    @Override
    protected Merchant doInBackground(String... strings) {
        Merchant result;
        try {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "http://111.231.137.51:8000/api/";
            String nowUrl = baseUrl + "merchants/" + strings[0] + ".json";
            Request request = new Request.Builder().url(nowUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                result = JsonUtil.parseMerchantJsonObject(response.body().string());
            }
            else {
                Toast.makeText(mContext, "无结果", Toast.LENGTH_SHORT).show();
                return null;
            }
        }catch (IOException e){
            Toast.makeText(mContext, "网络连接异常", Toast.LENGTH_SHORT).show();
            return null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Merchant merchant) {
        super.onPostExecute(merchant);
        if (tv != null){
            tv.setText(merchant.getName());
        }

    }
}
