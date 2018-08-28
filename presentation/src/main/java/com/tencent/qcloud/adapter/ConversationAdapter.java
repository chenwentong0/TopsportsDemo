package com.tencent.qcloud.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.common.utils.TimeUtil;
import com.example.common.utils.imageutil.ImageLoader;
import com.tencent.qcloud.chatmsg.Conversation;
import com.tencent.qcloud.presentation.R;

/**
 * Date 2018/8/24
 * Time 15:23
 *
 * @author wentong.chen
 * 会话列表
 */
public class ConversationAdapter extends BaseQuickAdapter<Conversation, BaseViewHolder> {


    public ConversationAdapter() {
        super(R.layout.item_conversation, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_last_message, item.getLastMessageSummary())
                .setText(R.id.tv_message_time, TimeUtil.getTimeStr(item.getLastMessageTime()));
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImageLoader.with(mContext)
                .url("https://upload.jianshu.io/users/upload_avatars/972352/8432d981-ac19-450c-bb25-e134d7f26385.jpg")
                .asCircle()
                .into(ivAvatar);

        long unRead = item.getUnreadNum();
        TextView tvUnReadNum = helper.getView(R.id.tv_unread_num);
        if (unRead <= 0){
            tvUnReadNum.setVisibility(View.INVISIBLE);
        }else{
            tvUnReadNum.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                tvUnReadNum.setBackground(mContext.getResources().getDrawable(R.drawable.point1));
            }else{
                tvUnReadNum.setBackground(mContext.getResources().getDrawable(R.drawable.point2));
                if (unRead > 99) {
                    unReadStr = mContext.getResources().getString(R.string.msg_count_more);
                }
            }
            tvUnReadNum.setText(unReadStr);
        }
    }
}
