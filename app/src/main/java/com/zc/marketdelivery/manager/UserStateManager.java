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
        if(userState.equals("Login")){
            return true;
        }
        else{
            return false;
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
