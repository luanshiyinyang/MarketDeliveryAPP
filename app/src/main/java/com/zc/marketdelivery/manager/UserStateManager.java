package com.zc.marketdelivery.manager;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UserStateManager {
    private String rootFile;

    public UserStateManager() {
        rootFile = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"UserState";
    }

    public boolean getUserState(){
        String userState;
        try {
            File file = new File(rootFile);
            if (!file.exists()){
                file.createNewFile();
                //保证文件绝对存在
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rootFile));
            userState = (String)ois.readObject();
            ois.close();
        }catch (Exception e){
            userState = new String();
        }
        if(!userState.equals("noLogin")){
            return true;
        }
        else{
            return false;
        }
    }

    public String getUserID(){
        if (! getUserState()){
            return "0";
        }
        String userState;
        try {
            File file = new File(rootFile);
            if (!file.exists()){
                file.createNewFile();
                //保证文件绝对存在
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rootFile));
            userState = (String)ois.readObject();
            ois.close();
        }catch (Exception e){
            userState = new String();
        }
        if(userState.split("\\+")[0].equals("登录成功")){
            return userState.split("\\+")[1];
        }
        else{
            return "0";
        }
    }

    public String getUserPhone(){
        String userState;
        try {
            File file = new File(rootFile);
            if (!file.exists()){
                file.createNewFile();
                //保证文件绝对存在
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rootFile));
            userState = (String)ois.readObject();
            ois.close();
        }catch (Exception e){
            userState = new String();
        }
        if(userState.split("\\+")[0].equals("登录成功")){
            return userState.split("\\+")[2];
        }
        else{
            return "null";
        }
    }

    public void updateUserState(String s){
        File file = new File(rootFile);
        try {
            if (!file.exists()){
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rootFile));
            oos.writeObject(s);
            oos.close();
        }catch (Exception E){

        }
    }
}
