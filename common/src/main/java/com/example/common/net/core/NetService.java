package com.example.common.net.core;

import android.text.TextUtils;

import com.example.common.utils.ToastUtil;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableOperator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author by chen.wentong on 2018/8/17.
 * 网络请求封装
 */
public class NetService {
    private static NetService sInstance;
    private String mDefaultUrl;
    private Class mService;

    private NetService() {
    }

    public static NetService getInstance() {
        if (sInstance == null) {
            synchronized (NetService.class) {
                if (sInstance == null) {
                    sInstance = new NetService();
                }
            }
        }
        return sInstance;
    }

    public <S> void init(String baseUrl, Class<S> service) {
        mDefaultUrl = baseUrl;
        mService = service;
    }

    public <S> S getService() {
        return (S) getService(mDefaultUrl, mService);
    }

    public <S> S getService(String baseUrl, Class<S> service) {
        return getService(new NetConfig.Builder().setBaseUrl(baseUrl).build(), service);
    }

    /**
     * 获取service
     * @param netConfig
     * @param service
     * @param <S>
     * @return
     */
    public <S> S getService(NetConfig netConfig, Class<S> service) {
        ObjectHelper.requireNonNull(netConfig, "netConfig cant be null");
        ObjectHelper.requireNonNull(service, "service cant be null");
        //添加线程切换代理
        S s = createRetrofit(netConfig).create(service);
        return (S)Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new NetInvocationHandler<>(s));
    }

    public Retrofit createRetrofit(String baseUrl) {
        return createRetrofit(new NetConfig.Builder().setBaseUrl(baseUrl).build());
    }

    /**
     * 创建baseurl的retrofit
     * @param
     * @return
     */
    public Retrofit createRetrofit(NetConfig netConfig) {
        ObjectHelper.requireNonNull(netConfig, "netConfig cant be null");
        validateUrl(netConfig.getBaseUrl());
        return new Retrofit.Builder()
                .baseUrl(netConfig.getBaseUrl())
                .client(createClient(netConfig))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public OkHttpClient createClient(NetConfig netConfig) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时时间
        builder.connectTimeout(netConfig.getConnectTimeout(), TimeUnit.MILLISECONDS);
        builder.readTimeout(netConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(netConfig.getWriteTimeout(), TimeUnit.MILLISECONDS);

        //添加拦截器（日志，请求头，token等）
        Interceptor logInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //打印请求信息

                //打印响应信息
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(logInterceptor);
        List<Interceptor> interceptors = netConfig.getInterceptors();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }

    private void validateUrl(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http")) {
            throw new IllegalArgumentException("url must start with http or url is empty");
        }
    }

    /**
     * 网络请求线程切换代理类
     */
    public static class NetInvocationHandler<T> implements InvocationHandler {
        private T mActualService;
        public NetInvocationHandler(T service) {
            mActualService = service;
        }

        @Override
        public Flowable invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //代理对象调用指定方法后，方法的返回值
            Object result = method.invoke(mActualService, args);

            if (result != null && result instanceof Flowable) {

                Flowable flowable = (Flowable) result;
                return flowable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            } else {
                return (Flowable) result;
            }
        }
    }
}
