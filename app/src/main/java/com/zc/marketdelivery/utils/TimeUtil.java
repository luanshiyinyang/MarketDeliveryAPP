package com.zc.marketdelivery.utils;

import java.util.Date;
import java.text.SimpleDateFormat;

public class TimeUtil {

    public static String getNowTime(){
        Date nowTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(nowTime);
    }

}
