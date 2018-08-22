package com.example.common.utils;

import android.content.Context;

/**
 * @author by chen.wentong on 2018/8/22.
 */

public class BaseUtil {

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }
}
