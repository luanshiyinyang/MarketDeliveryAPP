package com.zc.marketdelivery.fragment;

/**
 * Created by 16957 on 2018/7/16.
 * 菜单页面的fragment
 */
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zc.marketdelivery.R;
import com.zc.marketdelivery.activity.FinishedOrderDetailsActivity;
import com.zc.marketdelivery.activity.DistributingOrderDetailsActivity;
import com.zc.marketdelivery.adapter.OrderAdapter;
import com.zc.marketdelivery.bean.OrderItem;




public class OrdersFragment extends Fragment {

    //上下文
    Context context;
    //数据列表，仅用于测试
    private List<OrderItem> orderItemList = new ArrayList<>();
    private ListView lv_all_orders;

    //创建view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        //获得上下文
        context = getActivity();

        //填充布局
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        //为了测试防止多次重复，数据源清空，实际使用从数据库拿数据没有这类问题
        orderItemList.clear();
        //数据源填入数据
        initOrders();
        //组件绑定
        Toolbar tb = (Toolbar)view.findViewById(R.id.toolbar_order);
        tb.setTitle("订单");
        tb.setTitleTextColor(Color.BLACK);
        lv_all_orders = (ListView) view.findViewById(R.id.lv_allOrder);
        //设置自定义适配器
        lv_all_orders.setAdapter(new OrderAdapter(context, orderItemList));
        //设置事件监听
        lv_all_orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //完成的订单进入的activity
                if(orderItemList.get(i).getState().equals("订单已完成")){
                    Intent intent = new Intent(context, FinishedOrderDetailsActivity.class);
                    //数据拼接传递,不建议本地xml存放占内存
                    intent.putExtra("needValue", orderItemList.get(i).getState()+"&"
                            + orderItemList.get(i).getName()+"&"
                            + orderItemList.get(i).getResult()+"&"
                            + orderItemList.get(i).getExpectArrivedTime()+"&"
                            + orderItemList.get(i).getArrivedTime()+"&"
                            + orderItemList.get(i).getArrivedAddress()+ "&"
                            + orderItemList.get(i).getOrderNumber()+"&"
                            + orderItemList.get(i).getOrderSetTime()+"&"
                            + orderItemList.get(i).getWayOfPay()+"&"
                            + orderItemList.get(i).getPhoneNumberOfshops()
                    );
                    List<Map.Entry<String, Integer>> orders = orderItemList.get(i).getOrders();
                    intent.putExtra("needList",dealOrders(orders));
                    startActivity(intent);
                }
                //未完成的订单进入的activity
                else{
                    Intent intent = new Intent(context, DistributingOrderDetailsActivity.class);
                    //数据拼接传递,不建议本地xml存放占内存
                    intent.putExtra("needValue", orderItemList.get(i).getState()+"&"
                            + orderItemList.get(i).getName()+"&"
                            + orderItemList.get(i).getResult()+"&"
                            + orderItemList.get(i).getExpectArrivedTime()+"&"
                            + orderItemList.get(i).getArrivedTime()+"&"
                            + orderItemList.get(i).getArrivedAddress()+ "&"
                            + orderItemList.get(i).getOrderNumber()+"&"
                            + orderItemList.get(i).getOrderSetTime()+"&"
                            + orderItemList.get(i).getWayOfPay()+"&"
                            + orderItemList.get(i).getPhoneNumberOfshops()
                    );
                    List<Map.Entry<String, Integer>> orders = orderItemList.get(i).getOrders();
                    intent.putExtra("needList",dealOrders(orders));
                    startActivity(intent);
                }


            }
        });

        return view ;
    }


    //测试数据：具体数据由首页intent传递
    private void initOrders() {
        Map<String,Integer> map1 = new HashMap<>();
        map1.put("黄焖鸡",1);
        map1.put("黄洋洋",2);
        map1.put("张元澍",3);
        map1.put("林凯",4);
        Set<Map.Entry<String ,Integer>> set = map1.entrySet();
        List<Map.Entry<String, Integer>> list1 = new ArrayList<>(set);
        String order_result = "共"+map1.size()+"件商品，支付5元";

        OrderItem test1 = new OrderItem(R.mipmap.oscs,"欧尚超市1","订单配送中",order_result,list1,"10086","17.00","16.49","江南大学","00000000001","2018-3-2 17:00","在线支付");
        orderItemList.add(test1);
        OrderItem test2 = new OrderItem(R.mipmap.oscs,"欧尚超市2","订单配送中",order_result,list1,"10086","17.00","16.49","江南大学","00000000001","2018-3-2 17:00","在线支付");
        orderItemList.add(test2);
        OrderItem test3 = new OrderItem(R.mipmap.oscs,"欧尚超市3","订单配送中",order_result,list1,"10086","17.00","16.49","江南大学","00000000001","2018-3-2 17:00","在线支付");
        orderItemList.add(test3);
        OrderItem test4 = new OrderItem(R.mipmap.oscs,"欧尚超市4","订单已完成",order_result,list1,"10086","17.00","16.49","江南大学","00000000001","2018-3-2 17:00","在线支付");
        orderItemList.add(test4);

    }
    // 数据处理
    private String dealOrders(List<Map.Entry<String, Integer>> orders){
        String result ="";
        for(int i =0;i<orders.size();i++){
            if(i==0){
                result+=(orders.get(i).getKey()+"&" +orders.get(i).getValue());
            }
            else {
                result+="&"+orders.get(i).getKey()+"&"+orders.get(i).getValue();
            }
        }
        return result;
    }


}

