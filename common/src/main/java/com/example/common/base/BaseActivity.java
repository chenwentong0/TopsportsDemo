package com.example.common.base;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.inputmethod.InputMethodManager;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * @author by chen.wentong on 2018/8/21.
 */

public abstract class BaseActivity extends RxLifeActivity implements IBaseView {

    public final String TAG = getClass().getSimpleName() + "%s";
    //生命周期绑定
    protected final LifecycleProvider<Lifecycle.Event> mLifecycleProvider
            = AndroidLifecycle.createLifecycleProvider(this);
    protected BasePresenter mPresenter;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化默认主题
        setContentView(getLayoutId());

        initView();
        initListener();
        initEvent();
        initData();
    }


    /**
     * 设置布局id
     * @return
     */
    protected abstract  @LayoutRes int getLayoutId();

    protected void initView() {

    }

    protected void initListener() {

    }

    /**
     * 添加一些事件监听，如：登录成功后首页状态更新
     */
    protected void initEvent() {

    }

    protected void initData() {

    }

    public void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 开启页面的动画
     */
    public void startActivity(@NonNull final Class<? extends Activity> clz,
                              @AnimRes final int enterAnim,
                              @AnimRes final int exitAnim) {
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(this, enterAnim, exitAnim).toBundle();
        startActivity(new Intent(this, clz), bundle);
    }

    @Override
    public void setPresenter(BasePresenter<? extends IBaseView> basePresenter) {
        mPresenter = basePresenter;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }
}
