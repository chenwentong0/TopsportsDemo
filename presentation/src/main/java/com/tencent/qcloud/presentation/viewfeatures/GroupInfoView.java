package com.tencent.qcloud.presentation.viewfeatures;


import com.example.common.base.IBaseView;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;

import java.util.List;

/**
 * 群资料接口
 */
public interface GroupInfoView extends IBaseView {


    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    void showGroupInfo(List<TIMGroupDetailInfo> groupInfos);



}
