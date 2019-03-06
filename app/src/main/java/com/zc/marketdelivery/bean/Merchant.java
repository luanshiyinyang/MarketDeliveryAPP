package com.zc.marketdelivery.bean;

import java.io.Serializable;

/**
 * Created by 16957 on 2018/12/1.
 * 商铺的种子
 */

public class Merchant implements Serializable {
    private int id;
    private int icon;
    private String name;
    private String phone;
    private float rating;
    private int sales;
    private float priceSending;
    private float priceSend;
    private String address;
    private String timeSended;
    private float distance;

    public Merchant(int id, int icon, String name, float rating, int sales, float priceSending, float priceSend, String address, String timeSended, Float distance) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.rating = rating;
        this.sales = sales;
        this.priceSending = priceSending;
        this.priceSend = priceSend;
        this.address = address;
        this.timeSended = timeSended;
        this.distance = distance;
    }
    public Merchant(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public float getPriceSending() {
        return priceSending;
    }

    public void setPriceSending(float priceSending) {
        this.priceSending = priceSending;
    }

    public float getPriceSend() {
        return priceSend;
    }

    public void setPriceSend(float priceSend) {
        this.priceSend = priceSend;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimeSended() {
        return timeSended;
    }

    public void setTimeSended(String timeSended) {
        this.timeSended = timeSended;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
