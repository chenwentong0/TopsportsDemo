package com.tencent.qcloud.presentation.viewfeatures;

import com.example.common.base.IBaseView;

/**
 * 消息回调接口
 */
public interface MessageView extends IBaseView {


    void onStatusChange(Status newStatus);


    enum Status{
        NORMAL,
        SENDING,
        ERROR,
    }
}
