package com.zc.marketdelivery.utils;

import com.zc.marketdelivery.bean.Good;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static List<Good> removeDuplicate(List<Good> list){

        if (list==null){
            return null;
        }
        List<Good> listTemp = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            int times =0;
            for (int j=0;j<listTemp.size();j++){
                if (list.get(i).getKind().equals(listTemp.get(j).getKind())){
                    break;
                }
                else {
                    times++;
                }
            }
            if (times==listTemp.size()){
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }
}
