package com.tencent.qcloud.presentation.presenter;

import com.example.common.base.BasePresenter;
import com.example.common.base.IBaseView;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.Observer;

/**
 * Date 2018/8/24
 * Time 17:26
 *
 * @author wentong.chen
 */
public abstract class BaseIMPresenter<T extends IBaseView> extends BasePresenter<T> implements Observer {

    public BaseIMPresenter(T view, LifecycleProvider provider) {
        super(view, provider);
    }

}
