package com.example.common.utils;

import android.content.Context;

/**
 * Created by wentong.chen on 18/2/4.
 * 功能：
 */

public class ScreenUtil {

    private static Context sContext = BaseUtil.getContext();

    public static int dp2px(int dpVal) {
        float density = sContext.getResources().getDisplayMetrics().density;
        return (int) (dpVal * density + 0.5f);
    }

    /**
     * sp转px
     * @param spVal 字体大小
     * @return px大小
     */
    public static int sp2px(int spVal) {
        float scaledDensity = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scaledDensity * spVal + 0.5f);
    }
}
