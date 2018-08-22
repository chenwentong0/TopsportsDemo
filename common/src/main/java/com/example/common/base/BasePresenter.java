package com.example.common.base;


import android.arch.lifecycle.Lifecycle;

import com.trello.rxlifecycle2.LifecycleProvider;

public abstract  class BasePresenter<T extends IBaseView> {
    public final String TAG = getClass().getSimpleName();

    /**
     * 绑定生命周期请求请加上.compose(provider.bindToLifecycle())
     */
    protected LifecycleProvider<Lifecycle.Event> mProvider;
    protected T mView;

    public BasePresenter(T view, LifecycleProvider<Lifecycle.Event> provider) {
        this.mProvider = provider;
        this.mView = view;
    }

    public void onDestroy() {
        mView = null;
        mProvider = null;
    }

}
