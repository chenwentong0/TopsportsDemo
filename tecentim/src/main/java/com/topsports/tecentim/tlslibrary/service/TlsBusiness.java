package com.topsports.tecentim.tlslibrary.service;

import android.content.Context;


/**
 * 初始化tls登录模块
 */
public class TlsBusiness {


    private TlsBusiness(){}

    public static void init(Context context, int sdkAppId, int accountType){
        TLSConfiguration.setSdkAppid(sdkAppId);
        TLSConfiguration.setAccountType(accountType);
        TLSConfiguration.setTimeout(8000);
        TLSConfiguration.setQqAppIdAndAppKey("222222", "CXtj4p63eTEB2gSu");
        TLSConfiguration.setWxAppIdAndAppSecret("wx65f71c2ea2b122da", "1d30d40f8db6d3ad0ee6492e62ad5d57");
        TLSService.getInstance().initTlsSdk(context);
    }

    public static void logout(String id){
        TLSService.getInstance().clearUserInfo(id);

    }
}
