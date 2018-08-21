package com.example.common.net.core;

import android.support.v4.BuildConfig;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * @author by chen.wentong on 2018/8/17.
 * 网络请求配置类
 */

public class NetConfig {
    private NetConfig() {

    }
    private static final long DEFAULT_TIMEOUT = 30 * 1000;
    /**
     * 设置baseUrl
     */
    private String baseUrl;
    /**
     * 连接超时
     */
    private long connectTimeout = DEFAULT_TIMEOUT;
    private long readTimeout = DEFAULT_TIMEOUT;
    private long writeTimeout = DEFAULT_TIMEOUT;
    /**
     * 是否开启日志开关(默认debug开启)
     */
    private boolean logEnable = TextUtils.equals(BuildConfig.BUILD_TYPE, "debug");
    /**
     * 设置的拦截器集合
     */
    private List<Interceptor> mInterceptors;

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public boolean isLogEnable() {
        return logEnable;
    }

    public List<Interceptor> getInterceptors() {
        return mInterceptors;
    }

    public static class Builder {
        /**
         * 设置baseUrl
         */
        private String baseUrl;
        /**
         * 连接超时
         */
        private long connect_timeout = DEFAULT_TIMEOUT;
        private long read_timeout = DEFAULT_TIMEOUT;
        private long write_timeout = DEFAULT_TIMEOUT;
        /**
         * 是否开启日志开关(默认debug开启)
         */
        private boolean logEnable = TextUtils.equals(BuildConfig.BUILD_TYPE, "debug");
        /**
         * 设置的拦截器集合
         */
        private List<Interceptor> mInterceptors;
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setLogEnable(boolean logEnable) {
            this.logEnable = logEnable;
            return this;
        }

        public Builder setConnect_timeout(long connect_timeout) {
            this.connect_timeout = connect_timeout;
            return this;
        }

        public Builder setRead_timeout(long read_timeout) {
            this.read_timeout = read_timeout;
            return this;
        }

        public Builder setWrite_timeout(long write_timeout) {
            this.write_timeout = write_timeout;
            return this;
        }

        /**
         * 添加拦截器
         * @param interceptor
         * @return
         */
        public Builder addInterceptor(Interceptor interceptor) {
            if (mInterceptors == null) {
                mInterceptors = new ArrayList<>();
            }
            if (interceptor == null || mInterceptors.contains(interceptor)) {
                return this;
            }
            mInterceptors.add(interceptor);
            return this;
        }

        public NetConfig build() {
            NetConfig netConfig = new NetConfig();
            netConfig.baseUrl = baseUrl;
            netConfig.connectTimeout = connect_timeout;
            netConfig.readTimeout = read_timeout;
            netConfig.writeTimeout = write_timeout;
            netConfig.logEnable = logEnable;
            netConfig.mInterceptors = mInterceptors;
            return netConfig;
        }
    }
}
