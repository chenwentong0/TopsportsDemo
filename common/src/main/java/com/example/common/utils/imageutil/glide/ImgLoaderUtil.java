package com.example.common.utils.imageutil.glide;

import android.text.TextUtils;


import com.example.common.utils.imageutil.config.GlobalConfig;
import com.example.common.utils.imageutil.config.SingleConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by wentong.chen on 18/2/1.
 * 功能：
 */

public class ImgLoaderUtil {
    /**
     * 通过代理让监听回调在主线程中执行
     * @param listener
     * @return
     */
    public static SingleConfig.BitmapListener getBitmapListenerProxy(final SingleConfig.BitmapListener listener) {
        return (SingleConfig.BitmapListener) Proxy.newProxyInstance(SingleConfig.class.getClassLoader(),
                listener.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {

                        runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Object object = method.invoke(listener, args);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return null;
                    }
                });
    }

    public static void runOnUIThread(Runnable runnable) {
        GlobalConfig.getMainHandler().post(runnable);
    }

    public static String appendUrl(String url) {
        String newUrl = url;
        if (TextUtils.isEmpty(newUrl)) {
            return newUrl;
        }
        boolean hasHost = url.contains("http:") || url.contains("https:");
        if (!hasHost) {
            if (!TextUtils.isEmpty(GlobalConfig.baseUrl)) {
                newUrl = GlobalConfig.baseUrl + url;
            }
        }

        return newUrl;
    }

    public static boolean shouldSetPlaceHolder(SingleConfig config) {
        if (config.getPlaceHolderResId() <= 0) {
            return false;
        }

        if (config.getResId() > 0 || !TextUtils.isEmpty(config.getFilePath())
                || GlobalConfig.getLoader().isCache(config.getUrl())) {
            return false;
        } else {//只有在图片源为网络图片,并且图片没有缓存到本地时,才给显示placeholder
            return true;
        }
    }
}
