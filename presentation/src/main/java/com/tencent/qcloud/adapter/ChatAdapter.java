package com.tencent.qcloud.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.qcloud.chatmsg.ChatViewHolder;
import com.tencent.qcloud.chatmsg.Message;
import com.tencent.qcloud.presentation.R;

/**
 * Date 2018/8/27
 * Time 13:30
 *
 * @author wentong.chen
 */
public class ChatAdapter extends BaseQuickAdapter<Message, ChatViewHolder> {
    public ChatAdapter() {
        super(R.layout.item_chat, null);
    }



    @Override
    protected void convert(ChatViewHolder helper, Message item) {
        item.showMessage(helper, mContext);
    }
}
