package com.tencent.qcloud.presentation.viewfeatures;


import com.example.common.base.IBaseView;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

/**
 * 好友信息接口
 */
public interface FriendInfoView extends IBaseView {


    /**
     * 显示用户信息
     *
     * @param users 资料列表
     */
    void showUserInfo(List<TIMUserProfile> users);
}
