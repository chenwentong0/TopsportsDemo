package com.example.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author by chen.wentong on 2018/8/20.
 */

public class ToastUtil {
    private static Context sContext;
    private static boolean sIsInit;

    public static void init(Context context) {
        sContext = context;
        sIsInit = true;
    }

    public static boolean isInit() {
        return sIsInit;
    }

    public static void showLongToast(String toast) {
        Toast.makeText(sContext, toast, Toast.LENGTH_LONG).show();
    }
}
