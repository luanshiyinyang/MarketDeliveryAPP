package com.zc.marketdelivery.activity;
/**
 * Created by 16957 on 2018/7/16.
 * 核心activity，一切Activity和Service的起点
 */

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zc.marketdelivery.fragment.*;
import com.zc.marketdelivery.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // 控件
    private TextView tvBottomHomeIcon;
    private TextView tvBottomOrderIcon;
    private TextView tvBottomPersonalIcon;
    // Fragment
    private HomeFragment homeFragment;
    private OrdersFragment orderFragment;
    private PersonalFragment personalFragment;
    // 碎片管理器
    private FragmentManager fManager ;
    // 上下文
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        Activity创建方法
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        // 创建Activity就建立管理器，知道Activity销毁
        fManager = getFragmentManager();
        // 控件绑定
        bindViews();
        initListener();
        // 点击模拟，创建时默认点击在home那个Fragment上
        tvBottomHomeIcon.performClick();

    }

    @Override
    protected void onResume() {
        /*
        Resume方法
         */
        super.onResume();

    }

    private void bindViews() {
        /*
        控件初始化
         */
        tvBottomHomeIcon = (TextView) findViewById(R.id.tv_bottom_home_icon);
        tvBottomOrderIcon = (TextView) findViewById(R.id.tv_orders_icon);
        tvBottomPersonalIcon = (TextView) findViewById(R.id.tv_personal_icon);
    }


    private void initListener() {
        /*
        设置事件监听
         */
        tvBottomHomeIcon.setOnClickListener(this);
        tvBottomOrderIcon.setOnClickListener(this);
        tvBottomPersonalIcon.setOnClickListener(this);
    }

    private void setSelected(){
        /*
        重置所有图标的选中状态
         */
        tvBottomHomeIcon.setSelected(false);
        tvBottomOrderIcon.setSelected(false);
        tvBottomPersonalIcon.setSelected(false);
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        /*
        隐藏所有fragment
         */
        if(homeFragment != null) fragmentTransaction.hide(homeFragment);
        if(orderFragment != null) fragmentTransaction.hide(orderFragment);
        if(personalFragment != null) fragmentTransaction.hide(personalFragment);
    }

    @Override
    public void onClick(View v) {
        /*
        所有View的点击事件
         */
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.tv_bottom_home_icon:
                setSelected();
                tvBottomHomeIcon.setSelected(true);
                if(homeFragment == null){
                    homeFragment = new HomeFragment();
                }
                fTransaction.replace(R.id.fl_content, homeFragment);
                fTransaction.show(homeFragment);
                break;

            case R.id.tv_orders_icon:
                setSelected();
                tvBottomOrderIcon.setSelected(true);
                if(orderFragment == null){
                    orderFragment = new OrdersFragment();
                }
                fTransaction.replace(R.id.fl_content, orderFragment);
                fTransaction.show(orderFragment);
                break;

            case R.id.tv_personal_icon:
                setSelected();
                tvBottomPersonalIcon.setSelected(true);
                if(personalFragment == null){
                    personalFragment = new PersonalFragment();
                }
                fTransaction.replace(R.id.fl_content, personalFragment);
                fTransaction.show(personalFragment);
                break;
       }
        fTransaction.commit();
    }

    long waitTime = 2000;
    long touchTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
        模拟双击退出
         */
        if(event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            long currentTime = System.currentTimeMillis();
            if((currentTime-touchTime)>=waitTime) {
                // 让Toast的显示时间和等待时间相同
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                // 会跳屏，关闭时会出现黑屏，需要优化
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
