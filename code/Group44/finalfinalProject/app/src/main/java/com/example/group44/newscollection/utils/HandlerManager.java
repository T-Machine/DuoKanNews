package com.example.group44.newscollection.utils;

import android.os.Handler;

// 进行不同类的信息交流
public class HandlerManager {

    private static HandlerManager mInstance;

    public Handler mHandler;
    public synchronized static HandlerManager getInstance() {
        if (mInstance == null) {
            mInstance = new HandlerManager();
        }
        return mInstance;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void sendSuccessMessage() {
        mHandler.sendEmptyMessage(200);
    }

    public void sendFailMessage() {
        mHandler.sendEmptyMessage(404);
    }
}