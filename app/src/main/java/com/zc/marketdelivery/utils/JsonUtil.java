package com.zc.marketdelivery.utils;
/*
包含了服务器json数据的序列化以及反序列化
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zc.marketdelivery.bean.Good;
import com.zc.marketdelivery.bean.Merchant;
import com.zc.marketdelivery.bean.Order;
import com.zc.marketdelivery.bean.User;

import java.util.List;

public class JsonUtil {
    public static Merchant parseMerchantJsonObject(String jsonData) {
        /*
        解析Json对象
         */
        try {
            if (jsonData != null) {
                //创建一个Gson对象
                Gson gson = new Gson();
                return gson.fromJson(jsonData,Merchant.class);
            }
            else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static Good parseGoodJsonObject(String jsonData) {
        /*
        解析Json对象
         */
        try {
            if (jsonData != null) {
                //创建一个Gson对象
                Gson gson = new Gson();
                return gson.fromJson(jsonData,Good.class);
            }
            else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<Merchant> parseMerchantJsonList(String jsonData) {
        /*
        解析json数组
         */
        try {
            if (jsonData != null) {
                //创建一个Gson对象
                Gson gson = new Gson();
                return gson.fromJson(jsonData, new TypeToken<List<Merchant>>(){}.getType());
            }
            else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static User parseUserJsonObject(String jsonData) {
        /*
        解析Json对象
         */
        try {
            if (jsonData != null) {
                //创建一个Gson对象
                Gson gson = new Gson();
                return gson.fromJson(jsonData, User.class);
            }
            else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<User> parseUserJsonList(String jsonData) {
        /*
        解析json数组
         */
        try {
            if (jsonData != null) {
                //创建一个Gson对象
                Gson gson = new Gson();
                return gson.fromJson(jsonData, new TypeToken<List<User>>(){}.getType());
            }
            else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String jsonUserToString(User user){
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public static List<Good> parseGoodJsonList(String jsonData){
        try {
            if (jsonData != null){
                Gson gson = new Gson();
                return gson.fromJson(jsonData, new TypeToken<List<Good>>(){}.getType());
            }
            else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String jsonOrderToString(Order order) {
        Gson gson = new Gson();
        return gson.toJson(order);
    }

    public static List<Order> parseOrderJsonList(String jsonData) {
        /*
        解析json数组
         */
        try {
            if (jsonData != null) {
                //创建一个Gson对象
                Gson gson = new Gson();
                return gson.fromJson(jsonData, new TypeToken<List<Order>>(){}.getType());
            }
            else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
