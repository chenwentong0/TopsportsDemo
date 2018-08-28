package com.example.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author by chen.wentong on 2018/8/20.
 */

public class ToastUtil {
    private static Context sContext = BaseUtil.getContext();


    public static void showLongToast(String toast) {
        Toast.makeText(sContext, toast, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String toast) {
        Toast.makeText(sContext, toast, Toast.LENGTH_SHORT).show();
    }
}
