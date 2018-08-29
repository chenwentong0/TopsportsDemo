package com.example.chenwentong.helloworld.ui.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chenwentong.helloworld.R;
import com.example.chenwentong.helloworld.base.BaseFragment;
import com.example.chenwentong.helloworld.model.UserInfo;
import com.example.chenwentong.helloworld.ui.chat.ChatActivity;
import com.example.common.utils.ToastUtil;
import com.example.common.widget.TitleBar;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.qcloud.adapter.ConversationAdapter;
import com.tencent.qcloud.chatmsg.Conversation;
import com.tencent.qcloud.chatmsg.NomalConversation;
import com.tencent.qcloud.presentation.presenter.ConversationPresenter;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipMessageView;
import com.tencent.qcloud.presentation.viewfeatures.GroupManageMessageView;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Date 2018/8/24
 * Time 13:53
 *
 * @author wentong.chen
 * 会话列表
 */
public class ConversationFragment extends BaseFragment implements ConversationView, FriendshipMessageView, GroupManageMessageView {
    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ConversationAdapter mAdapter;
    private FriendshipManagerPresenter mFriendshipPresenter;
    private ConversationPresenter mPresenter;
    private GroupManagerPresenter mGroupManagerPresenter;
    /**
     * 回话列表
     */
    private List<Conversation> mConversationList = new ArrayList<>();
    private List<String> mGroupList = new ArrayList<>();

    public static ConversationFragment newInstance() {
        ConversationFragment fragment = new ConversationFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void initView() {
        mTitleBar.setTitle("回话列表");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ConversationAdapter();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Conversation item = mAdapter.getItem(position);
                ChatActivity.navToChat(getContext(), item.getIdentify(), item.getConversationType());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        mGroupManagerPresenter = new GroupManagerPresenter(this, mLifecycleProvider);
        mFriendshipPresenter = new FriendshipManagerPresenter(this, mLifecycleProvider);
        mPresenter = new ConversationPresenter(this, mLifecycleProvider);
        addPresenter(mGroupManagerPresenter)
                .addPresenter(mFriendshipPresenter)
                .addPresenter(mPresenter);

        //初始化聊天界面的列表数据
        mPresenter.getConversationList();
    }

    @Override
    protected boolean isUseLazyLoad() {
        return true;
    }

    @Override
    protected void onVisibleRefresh() {
        if (mAdapter.getItemCount() != 0) {
            mAdapter.notifyDataSetChanged();
        } else {
            mPresenter.getConversationList();
        }
    }

    /**
     * 聊天界面列表返回结果
     *
     * @param conversationList
     */
    @Override
    public void onLoadConversations(List<TIMConversation> conversationList) {
        this.mConversationList.clear();
        mGroupList.clear();
        conversationList.add(new TIMConversation());
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.mConversationList.add(new NomalConversation(item));
                    mGroupList.add(item.getPeer());
                    break;
                default:
            }
        }
        mFriendshipPresenter.getFriendshipLastMessage();
        mGroupManagerPresenter.getGroupManageLastMessage();
        mAdapter.setNewData(mConversationList);
        LogUtils.d(TAG, "回话列表的长度 size = " + mAdapter.getItemCount());

//        mTestAdapter.setNewData(TestUtil.getStringList(20));
    }

    @Override
    public void updateLastMsg(TIMMessage message) {

    }

    @Override
    public void updateFriendshipMessage() {

    }

    @Override
    public void removeConversation(String identify) {

    }

    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {

    }

    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {

    }

    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {

    }

    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {

    }
}
