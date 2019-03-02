package com.zc.marketdelivery.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by 16957 on 2018/7/16.
 */
public class OrderItem {
    private int imageID;//图标id
    private String name;//超市名称
    private String state;//订单状态（订单已完成，订单配送中)
    private String result;//订单数目和总价
    List<Map.Entry<String, Integer>> orders;//订单商品和数目
    private String phoneNumberOfshops;
    //完成订单属性
    private String expectArrivedTime; //期待送达时间
    private String arrivedTime;//实际送达时间
    private String arrivedAddress;//送达地址
    private String orderNumber;//订单编号
    private String orderSetTime;//下单时间
    private String wayOfPay;//支付方式



    public OrderItem(int imageID, String name, String state, String result, List<Map.Entry<String, Integer>> orders, String phoneNumberOfshops,
                     String expectArrivedTime, String arrivedTime, String arrivedAddress, String orderNumber, String orderSetTime, String wayOfPay) {
        this.imageID = imageID;
        this.name = name;
        this.result = result;
        this.orderSetTime = orderSetTime;
        this.wayOfPay = wayOfPay;
        this.orderNumber = orderNumber;
        this.arrivedAddress = arrivedAddress;
        this.arrivedTime = arrivedTime;
        this.expectArrivedTime = expectArrivedTime;
        this.orders = orders;
        this.state = state;
        this.phoneNumberOfshops = phoneNumberOfshops;
    }//已完成订单的构造函数



    //所有属性的set和get方法
    public String getExpectArrivedTime() {
        return expectArrivedTime;
    }

    public void setExpectArrivedTime(String expectArrivedTime) {
        this.expectArrivedTime = expectArrivedTime;
    }

    public String getWayOfPay() {
        return wayOfPay;
    }

    public void setWayOfPay(String wayOfPay) {
        this.wayOfPay = wayOfPay;
    }

    public String getOrderSetTime() {
        return orderSetTime;
    }

    public void setOrderSetTime(String orderSetTime) {
        this.orderSetTime = orderSetTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getArrivedAddress() {
        return arrivedAddress;
    }

    public void setArrivedAddress(String arrivedAddress) {
        this.arrivedAddress = arrivedAddress;
    }



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }



    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Map.Entry<String, Integer>> getOrders() {
        return orders;
    }

    public void setOrders(List<Map.Entry<String, Integer>> orders) {
        this.orders = orders;
    }
    public String getPhoneNumberOfshops() {
        return phoneNumberOfshops;
    }

    public void setPhoneNumberOfshops(String phoneNumberOfshops) {
        this.phoneNumberOfshops = phoneNumberOfshops;
    }


}
