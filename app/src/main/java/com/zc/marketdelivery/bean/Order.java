package com.zc.marketdelivery.bean;

public class Order {
    private String id;
    private String userID;
    private String name;
    private String state;
    private String price;
    private String phone;
    private String timePlaced;
    private String timeExpected;
    private String timeArrived;
    private String address;
    private String wayOfPay;
    private String goodsNumber;
    private String goodsIDs;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(String timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getTimeExpected() {
        return timeExpected;
    }

    public void setTimeExpected(String timeExpected) {
        this.timeExpected = timeExpected;
    }

    public String getTimeArrived() {
        return timeArrived;
    }

    public void setTimeArrived(String timeArrived) {
        this.timeArrived = timeArrived;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWayOfPay() {
        return wayOfPay;
    }

    public void setWayOfPay(String wayOfPay) {
        this.wayOfPay = wayOfPay;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsIDs() {
        return goodsIDs;
    }

    public void setGoodsIDs(String goodsIDs) {
        this.goodsIDs = goodsIDs;
    }
}
