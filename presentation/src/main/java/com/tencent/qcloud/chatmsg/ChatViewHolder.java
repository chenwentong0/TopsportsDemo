package com.tencent.qcloud.chatmsg;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tencent.qcloud.presentation.R;

public class ChatViewHolder extends BaseViewHolder {
    public RelativeLayout leftMessage;
    public RelativeLayout rightMessage;
    public RelativeLayout leftPanel;
    public RelativeLayout rightPanel;
    public ProgressBar sending;
    public ImageView error;
    public TextView sender;
    public TextView systemMessage;
    public TextView rightDesc;

    public ChatViewHolder(View view) {
        super(view);
        leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
        rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
        leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
        rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
        sending = (ProgressBar) view.findViewById(R.id.sending);
        error = (ImageView) view.findViewById(R.id.sendError);
        sender = (TextView) view.findViewById(R.id.sender);
        rightDesc = (TextView) view.findViewById(R.id.rightDesc);
        systemMessage = (TextView) view.findViewById(R.id.systemMessage);
    }
}