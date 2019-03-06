package com.zc.marketdelivery.task;
/*
请求服务器上的商家的列表数据
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.zc.marketdelivery.adapter.HomeMerchantsAdapter;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.utils.JsonUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestForMerchantListTask extends AsyncTask<String,String, List<Merchant>> {
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;

    public RequestForMerchantListTask(Context context, RecyclerView rv) {
        this.mContext = context;
        this.recyclerView = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<Merchant> doInBackground(String... strings) {
        List<Merchant> result;
        try {
            OkHttpClient client = new OkHttpClient();
            String baseUrl = "http://13.250.1.159:8000/api/";
            String nowUrl = baseUrl + "merchants" + ".json";
            Request request = new Request.Builder().url(nowUrl).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()){
                assert response.body() != null;
                result = JsonUtil.parseMerchantJsonList(response.body().string());
            }
            else {
                Toast.makeText(mContext, "无结果", Toast.LENGTH_SHORT).show();
                return null;
            }
        }catch (IOException e){
            return null;
        }
        if (strings.length==0){
            // 不需要排序
            return result;
        }
        else {
            // 需要排序
            if (strings[0].equals("sales")){
                // 按照销量排序
                assert result != null;
                Collections.sort(result, new Comparator<Merchant>() {
                    @Override
                    public int compare(Merchant o1, Merchant o2) {
                        if (o2.getSales()-o1.getSales() > 0){
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    }
                });
            }
            else {
                // 按照距离排序
                assert result != null;
                Collections.sort(result, new Comparator<Merchant>() {
                    @Override
                    public int compare(Merchant o1, Merchant o2) {
                        if (o2.getDistance()-o1.getDistance() > 0) {
                            return 1;
                        }
                        else {
                            return -1;
                        }
                    }
                });
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Merchant> merchants) {
        if (merchants != null){
            super.onPostExecute(merchants);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            HomeMerchantsAdapter adapter = new HomeMerchantsAdapter(merchants, mContext);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(mContext, "网络连接异常，请检查网络连接",Toast.LENGTH_SHORT).show();
        }

    }
}
