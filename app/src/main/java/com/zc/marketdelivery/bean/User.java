package com.zc.marketdelivery.bean;

import com.zc.marketdelivery.R;

public class User {
    private long id;
    private String name;
    private String password;
    private String phone;
    private String email;
    private String icon;
    private String address;


    public User(String phone, String password) {
        this.password = password;
        this.phone = phone;
        this.name = "用户名";
        this.email = "zc@163.com";
        this.icon = String.valueOf(R.drawable.baidu);
    }

    public User(String name, String password, String phone, String email, String icon) {
        /*
        构造不需要id号，数据库产生id号
         */
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
