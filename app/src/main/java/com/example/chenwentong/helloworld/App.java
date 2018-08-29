package com.example.chenwentong.helloworld;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.example.chenwentong.helloworld.common.Constants;
import com.example.chenwentong.helloworld.common.NimSDKOptionConfig;
import com.example.common.utils.BaseUtil;
import com.example.common.utils.CacheDiskUtils;
import com.example.common.utils.imageutil.ImageLoader;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.tencent.smtt.sdk.QbSdk;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author by chen.wentong on 2018/8/20.
 */

public class App extends Application {

    private static Context sContext;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        initUtil();
        initSdk();
    }

    private void initSdk() {
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), NimSDKOptionConfig.options());
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
        }


        InitBusiness.start(getApplicationContext(), TIMLogLevel.DEBUG, Constants.IM_APP_ID);
        TlsBusiness.init(getApplicationContext());

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getContext(), cb);

        //设置上报x5内核错误信息
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getContext());
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            @Override
            public Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                String x5CrashInfo = com.tencent.smtt.sdk.WebView.getCrashExtraMessage(getContext());
                map.put("x5crashInfo", x5CrashInfo);
                return map;
            }

            @Override
            public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType, String errorMessage, String errorStack) {
                try {
                    return "Extra data.".getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }
        });
        //腾讯bugly初始化 第三个参数为SDK调试模式开关，建议在测试阶段建议设置成true，发布时设置为false。crash日志上报
        CrashReport.initCrashReport(getContext(), Constants.BUGLY_APP_ID, true);
    }

    private LoginInfo loginInfo() {
        Object serializable = CacheDiskUtils.getInstance().getSerializable(Constants.NIM_LOGININFO_KEY);
        if (serializable != null) {
            return (LoginInfo) serializable;
        }
        return null;
    }

    private void initUtil() {
        BaseUtil.init(getContext());
        ImageLoader.init(getContext());
    }

    public static Context getContext() {
        return sContext;
    }

}
