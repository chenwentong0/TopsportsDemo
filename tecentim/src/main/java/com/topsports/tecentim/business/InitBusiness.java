package com.topsports.tecentim.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;


/**
 * 初始化
 * 包括imsdk等
 */
public class InitBusiness {
    private static Context sContext;
    private static final String TAG = InitBusiness.class.getSimpleName();

    private InitBusiness(){}

    public static void start(Context context, int sdkAppId){
        initImsdk(context, 0, sdkAppId);
    }

    public static void start(Context context, int logLevel, int sdkAppId){
        initImsdk(context, logLevel, sdkAppId);
    }


    /**
     * 初始化imsdk
     */
    private static void initImsdk(Context context, int logLevel, int sdkAppId){
        sContext = context;
        //sdk appid 由腾讯分配 SDK_APPID = 1400001533;
        TIMSdkConfig config = new TIMSdkConfig(sdkAppId);
        config.enableLogPrint(true)
                .setLogLevel(TIMLogLevel.values()[logLevel]);
        //初始化imsdk
        TIMManager.getInstance().init(context, config);
        //禁止服务器自动代替上报已读
        Log.d(TAG, "initIMsdk");

    }


    public static Context getContext() {
        return sContext;
    }
}
