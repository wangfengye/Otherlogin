package com.maple.wangfeng.otherlogin.callback;

import android.util.Log;

public abstract class IWeChatCallback {

    public abstract void onSuccess(String info);

    public void onError(String msg) {
        Log.i("weChat", "onError: " + msg);
    }
}
