package com.example.common.net.subscriber;

import java.io.IOException;

/**
 * @author by chen.wentong on 2018/8/17.
 */

public abstract class BaseModelSubscriber<T> extends BaseObjectSubscriber<BaseModel<T>> {

    @Override
    public void onSuccess(BaseModel<T> tBaseModel) {
        int code = tBaseModel.getCode();
        if (code >= 200 && code < 300) {
            onLoadSuccess(tBaseModel.getData());
        } else {
            onFailure(new IOException(tBaseModel.getMsg()));
        }
    }

    public abstract void onLoadSuccess(T t);
}
