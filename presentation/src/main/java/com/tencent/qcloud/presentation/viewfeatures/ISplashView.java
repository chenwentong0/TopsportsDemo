package com.tencent.qcloud.presentation.viewfeatures;

import com.example.common.base.IBaseView;

/**
 * 闪屏界面控制接口
 */
public interface ISplashView extends IBaseView {

    /**
     * 跳转到主界面
     */
    void navToHome();


    /**
     * 跳转到登录界面
     */
    void navToLogin();

    /**
     * 是否已有用户登录
     */
    boolean isUserLogin();

}
