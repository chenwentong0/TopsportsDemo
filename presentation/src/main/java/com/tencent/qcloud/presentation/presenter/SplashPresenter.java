package com.tencent.qcloud.presentation.presenter;

import android.os.Handler;
import com.tencent.qcloud.presentation.viewfeatures.ISplashView;


/**
 * 闪屏界面逻辑
 */
public class SplashPresenter {
    ISplashView view;
    private static final String TAG = SplashPresenter.class.getSimpleName();

    public SplashPresenter(ISplashView view) {
        this.view = view;
    }


    /**
     * 加载页面逻辑
     */
    public void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view.isUserLogin()) {
                    view.navToHome();
                } else {
                    view.navToLogin();
                }
            }
        }, 1000);
    }


}
