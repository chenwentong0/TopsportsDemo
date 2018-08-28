package com.tencent.qcloud.presentation.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qcloud.sdk.Constant;


/**
 * 初始化
 * 包括imsdk等
 */
public class InitBusiness {

    private static final String TAG = InitBusiness.class.getSimpleName();
    private static Context sContext;

    private InitBusiness(){}

    public static void start(Context context){
        start(context, TIMLogLevel.DEBUG);
    }

    public static void start(Context context, TIMLogLevel logLevel){
        start(context, logLevel, Constant.SDK_APPID);
    }

    public static void start(Context context, TIMLogLevel logLevel, int appId){
        initImsdk(context, logLevel, appId);
    }


    /**
     * 初始化imsdk
     */
    private static void initImsdk(Context context, TIMLogLevel logLevel, int appId){
        sContext = context;
        SharedPreferences pref = sContext.getSharedPreferences("data", Context.MODE_PRIVATE);
        int loglvl = 0;
        if (logLevel == TIMLogLevel.DEBUG) {
            loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        } else if (logLevel == TIMLogLevel.ERROR) {
            loglvl = pref.getInt("loglvl", TIMLogLevel.ERROR.ordinal());
        }
        else if (logLevel == TIMLogLevel.INFO) {
            loglvl = pref.getInt("loglvl", TIMLogLevel.INFO.ordinal());
        }
        else if (logLevel == TIMLogLevel.WARN) {
            loglvl = pref.getInt("loglvl", TIMLogLevel.WARN.ordinal());
        }
        else if (logLevel == TIMLogLevel.OFF) {
            loglvl = pref.getInt("loglvl", TIMLogLevel.OFF.ordinal());
        }
        TIMSdkConfig config = new TIMSdkConfig(appId);
        config.enableLogPrint(true)
                .setLogLevel(TIMLogLevel.values()[loglvl]);
        //初始化imsdk
        TIMManager.getInstance().init(context, config);
        //禁止服务器自动代替上报已读
        Log.d(TAG, "initIMsdk");

    }


    public static Context getContext() {
        return sContext;
    }


}
