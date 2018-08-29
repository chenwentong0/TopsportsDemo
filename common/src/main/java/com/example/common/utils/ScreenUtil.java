package com.example.common.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;

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

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) BaseUtil.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return BaseUtil.getContext().getResources().getDisplayMetrics().widthPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) BaseUtil.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return BaseUtil.getContext().getResources().getDisplayMetrics().heightPixels;
        }
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(point);
        } else {
            wm.getDefaultDisplay().getSize(point);
        }
        return point.y;
    }
}
