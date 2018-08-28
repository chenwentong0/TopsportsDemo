package com.tencent.qcloud.presentation.viewfeatures;


import com.example.common.base.IBaseView;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.sns.TIMFriendGroup;

import java.util.List;

/**
 * Created by admin on 16/3/1.
 */
public interface MyFriendGroupInfo extends IBaseView {

    void showMyGroupList(List<TIMFriendGroup> timFriendGroups);

    void showGroupMember(String groupname,List<TIMUserProfile> timUserProfiles);
}
