package com.tencent.qcloud.chatmsg;

import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.common.utils.ToastUtil;
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
    private final Context mContext;
    private final MenuItem.OnMenuItemClickListener mListener;

    public ChatViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
        rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
        leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
        rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
        sending = (ProgressBar) view.findViewById(R.id.sending);
        error = (ImageView) view.findViewById(R.id.sendError);
        sender = (TextView) view.findViewById(R.id.sender);
        rightDesc = (TextView) view.findViewById(R.id.rightDesc);
        systemMessage = (TextView) view.findViewById(R.id.systemMessage);


        view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                MenuInflater inflater = ((Activity)mContext).getMenuInflater();
                MenuItem delete = menu.add(Menu.NONE, 1, 1, "删除");
                MenuItem delete_much = menu.add(Menu.NONE, 2, 2, "批量删除");
                delete.setOnMenuItemClickListener(mListener);            //响应点击事件
                delete_much.setOnMenuItemClickListener(mListener);
//                Log.e(TAG, "in context menu");
            }
        });

        //设置每个菜单的点击动作
//do something
//do something
        mListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {         //设置每个菜单的点击动作
                switch (item.getItemId()){
                    case 1:
                        ToastUtil.showLongToast("删除");
                        //do something
                        return true;
                    case 2:
                        ToastUtil.showLongToast("批量删除");
                        //do something
                    default:
                        return true;
                }
            }
        };
    }
}