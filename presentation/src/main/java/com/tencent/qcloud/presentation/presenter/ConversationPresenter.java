package com.tencent.qcloud.presentation.presenter;

import android.util.Log;

import com.example.common.utils.ToastUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.presentation.viewfeatures.ConversationView;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * 会话界面逻辑
 */
public class ConversationPresenter extends BaseIMPresenter<ConversationView> {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;

    public ConversationPresenter(ConversationView view, LifecycleProvider provider) {
        super(view, provider);
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
        this.view = view;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                view.updateLastMsg(msg);
            }
        } else if (observable instanceof FriendshipEvent) {
            FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) data;
            switch (cmd.type) {
                case ADD_REQ:
                case READ_MSG:
                case ADD:
                    view.updateFriendshipMessage();
                    break;
                default:
            }
        } else if (observable instanceof GroupEvent) {
            GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
            switch (cmd.type) {
                case UPDATE:
                case ADD:
                    view.updateGroupInfo((TIMGroupCacheInfo) cmd.data);
                    break;
                case DEL:
                    view.removeConversation((String) cmd.data);
                    break;
                default:

            }
        } else if (observable instanceof RefreshEvent) {
            getConversationList();
            view.refresh();
        }
    }


    public void getConversationList() {
        List<TIMConversation> list = TIMManagerExt.getInstance().getConversationList();
        Log.d(TAG, "回话列表长度 = " + list.size());
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : list) {
            if (conversation.getType() == TIMConversationType.System) {
                continue;
            }
            result.add(conversation);
            TIMConversationExt conversationExt = new TIMConversationExt(conversation);
            //获取回话的漫游消息， 同步更新最新的一条消息（保证消息的准确）
            conversationExt.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        view.updateLastMsg(timMessages.get(0));
                    }

                }
            });

        }
        view.onLoadConversations(result);
    }

    /**
     * 删除会话
     *
     * @param type 会话类型
     * @param id   会话对象id
     */
    public boolean delConversation(TIMConversationType type, String id) {
        return TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(type, id);
    }


}
