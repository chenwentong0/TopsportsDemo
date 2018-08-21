package com.example.chenwentong.helloworld;

import android.app.Application;

import com.example.common.utils.ToastUtil;

/**
 * @author by chen.wentong on 2018/8/20.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initUtil();
    }

    private void initUtil() {
        ToastUtil.init(this);
    }
}
