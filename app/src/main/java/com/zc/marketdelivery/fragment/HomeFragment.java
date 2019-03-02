package com.zc.marketdelivery.fragment;
/**
 * Created by 16957 on 2018/7/16.
 * 首页
 */
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.activity.*;
import com.zc.marketdelivery.adapter.HomeMerchantsAdapter;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.listener.MyLocationListener;
import com.zc.marketdelivery.loader.MyImageLoader;
import com.zc.marketdelivery.task.RequestForMerchantListTask;
import com.zc.marketdelivery.utils.PermissionUtil;

public class HomeFragment extends Fragment{
    // 上下文
    private Context mContext;
    // 用来发起定位，添加取消监听
    private LocationClient mLocationClient;
    // 控件
    private TextView tvAddressIcon;
    private Button btnSales;
    private Button btnDistance;
    private TextView tvSearch;
    private RecyclerView rvMerchants;
    // 轮播图
    private Banner banner;
    // 数据源
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 获取上下文
        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bindViews(view);
        // 数据初始化的位置是有考究的
        initBannerData();
        initControls();
        requestPermission();
        return view;
    }

    private void bindViews(View view) {
        tvAddressIcon = (TextView) view.findViewById(R.id.location_text);
        tvSearch = (TextView) view.findViewById(R.id.text_search);
        banner =  (Banner)view.findViewById(R.id.banner);
        btnSales = (Button) view.findViewById(R.id.button_sell);
        btnDistance = (Button) view.findViewById(R.id.button_distance);
        rvMerchants = (RecyclerView) view.findViewById(R.id.rv_merchants);
    }

    private void initBannerData() {
        // 轮播图数据
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.baidu);
        imagePath.add(R.drawable.baidu);
        imagePath.add(R.drawable.baidu);
        imageTitle.add("百度");
        imageTitle.add("阿里");
        imageTitle.add("腾讯");
    }

    private void initControls() {
        initMap();
        initBanner();
        initRecyclerView(null);
        initListener();

    }

    private void initMap() {
        /*
        地图数据更新
         */
        mLocationClient = new LocationClient(mContext);
        mLocationClient.registerLocationListener(new MyLocationListener(tvAddressIcon));
    }

    private void initBanner() {
        MyImageLoader mMyImageLoader = new MyImageLoader();
        // 设置样式，里面有很多种样式可以自己都看看效果
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        // 设置图片加载器
        banner.setImageLoader(mMyImageLoader);
        // 设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        // 轮播图片的文字
        banner.setBannerTitles(imageTitle);
        // 设置轮播间隔时间
        banner.setDelayTime(3000);
        // 设置是否为自动轮播，默认是true
        banner.isAutoPlay(true);
        // 设置指示器的位置，小点点，居中显示
        banner.setIndicatorGravity(BannerConfig.CENTER);
        // 设置图片加载地址
        banner.setImages(imagePath);
        banner.start();
        // 设置点击监听
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                Toast.makeText(mContext, "你点了第" + (i + 1) + "张轮播图", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView(String sortMethod) {
        List<Merchant> merchants = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvMerchants.setLayoutManager(layoutManager);
        if (sortMethod == null){
            new RequestForMerchantListTask(mContext, rvMerchants).execute();
            rvMerchants.setAdapter( new HomeMerchantsAdapter(merchants,mContext));
        }
        else if (sortMethod.equals("sales")){
            new RequestForMerchantListTask(mContext, rvMerchants).execute(sortMethod);
            rvMerchants.setAdapter( new HomeMerchantsAdapter(merchants,mContext));
        }
        else {
            new RequestForMerchantListTask(mContext, rvMerchants).execute(sortMethod);
            rvMerchants.setAdapter( new HomeMerchantsAdapter(merchants,mContext));
        }


    }

    private void initListener() {
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(mContext,HomeSearchActivity.class);
                startActivity(intent1);
            }
        });
        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 按照销量进行排序
                initRecyclerView("sales");
            }
        });

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 按照距离排序
                initRecyclerView("distance");
            }
        });
    }

    private void requestPermission() {
        List<String> permissionList = PermissionUtil.checkPermission(mContext);
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[0]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            requestLocation();
        }

    }

    private void requestLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(mContext, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(mContext, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}
