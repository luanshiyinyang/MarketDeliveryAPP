package com.zc.marketdelivery.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtil {
    private Context mContext;
    public InputMethodUtil(Context mContext) {
        this.mContext = mContext;
    }
    public  void hideInputMethod(){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen){
            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
