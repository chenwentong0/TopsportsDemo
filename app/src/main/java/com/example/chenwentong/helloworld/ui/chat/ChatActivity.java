package com.example.chenwentong.helloworld.ui.chat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.chenwentong.helloworld.R;
import com.example.chenwentong.helloworld.base.BaseActivity;
import com.example.chenwentong.helloworld.ui.preview.ImagePreviewActivity;
import com.example.chenwentong.helloworld.ui.two.TwoActivity;
import com.example.common.net.subscriber.BaseObjectSubscriber;
import com.example.common.utils.FileUtil;
import com.example.common.utils.MediaUtil;
import com.example.common.utils.RecorderUtil;
import com.example.common.utils.RxJavaUtil;
import com.example.common.utils.ToastUtil;
import com.example.common.widget.TitleBar;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.qcloud.adapter.ChatAdapter;
import com.tencent.qcloud.chatmsg.CustomMessage;
import com.tencent.qcloud.chatmsg.FileMessage;
import com.tencent.qcloud.chatmsg.FriendProfile;
import com.tencent.qcloud.chatmsg.FriendshipInfo;
import com.tencent.qcloud.chatmsg.GroupInfo;
import com.tencent.qcloud.chatmsg.ImageMessage;
import com.tencent.qcloud.chatmsg.Message;
import com.tencent.qcloud.chatmsg.MessageFactory;
import com.tencent.qcloud.chatmsg.TextMessage;
import com.tencent.qcloud.chatmsg.UGCMessage;
import com.tencent.qcloud.chatmsg.VideoMessage;
import com.tencent.qcloud.chatmsg.VoiceMessage;
import com.tencent.qcloud.presentation.presenter.ChatPresenter;
import com.tencent.qcloud.presentation.viewfeatures.ChatView;
import com.tencent.qcloud.view.ChatInput;
import com.tencent.qcloud.view.VoiceSendingView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Flowable;

/**
 * Date 2018/8/27
 * Time 12:14
 *
 * @author wentong.chen
 * 聊天页面
 */
public class ChatActivity extends BaseActivity implements ChatView {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private static final int VIDEO_RECORD = 500;

    @BindView(R.id.title_bar)
    TitleBar mTitleBar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.input_panel)
    ChatInput mChatInput;
    @BindView(R.id.voice_sending)
    VoiceSendingView mVoiceSendingView;

    private ChatAdapter mAdapter;
    private String mIdentify;
    private TIMConversationType mType;
    private ChatPresenter mPresenter;
    private LinearLayoutManager mLayoutManager;
    private List<Message> mMessageList = new ArrayList<>();
    private String mTitleStr;
    private Uri mFileUri;
    private RecorderUtil recorder = new RecorderUtil();

    public static void navToChat(Context context, String identify, TIMConversationType type) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        super.initView();
        mIdentify = getIntent().getStringExtra("identify");
        mType = (TIMConversationType) getIntent().getSerializableExtra("type");
        mPresenter = new ChatPresenter(this, mIdentify, mType);
        mTitleBar.setTitle("聊天页面");
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(mMessageList);
        mChatInput.setChatView(this);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mChatInput.setInputMode(ChatInput.InputMode.NONE);
                        break;
                    default:
                }
                return false;
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int firstItem = mLayoutManager.findFirstVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    mPresenter.getMessage(mMessageList.size() > 0 ? mMessageList.get(0).getMessage() : null);
                }
            }
        });

        //注册列表item长按弹出菜单事件
        registerForContextMenu(mRecyclerView);

        initTitle();
        mPresenter.start();
    }

    private void initTitle() {
        //设置标题栏
        switch (mType) {
            case C2C:
                mTitleBar.setRightDrawable(R.drawable.btn_person);
                if (FriendshipInfo.getInstance().isFriend(mIdentify)) {
                    mTitleBar.getIvRight().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showLongToast("进入好友资料页面");
//                            Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
//                            intent.putExtra("identify", identify);
//                            startActivity(intent);
                        }
                    });
                    FriendProfile profile = FriendshipInfo.getInstance().getProfile(mIdentify);
                    mTitleBar.setTitle(mTitleStr = profile == null ? mIdentify : profile.getName());
                } else {
                    mTitleBar.getIvRight().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showLongToast("进入添加好友页面");
                        }
                    });
                    mTitleBar.setTitle(mTitleStr = mIdentify);
                }
                break;
            case Group:
                mTitleBar.setRightDrawable(R.drawable.btn_group);
                mTitleBar.getIvRight().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showLongToast("进入群资料页面");
                    }
                });
                mTitleBar.setTitle(GroupInfo.getInstance().getGroupName(mIdentify));
                break;
            default:

        }
    }

    /**
     * 添加上下文菜单
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = mMessageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()) {
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        } else if (message.getMessage().isSelf()) {
            menu.add(0, 4, Menu.NONE, getString(R.string.chat_pullback));
        }
        if (message instanceof ImageMessage || message instanceof FileMessage) {
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }

    /**
     * 点击菜单item的回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = mMessageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                mMessageList.remove(info.position);
                mAdapter.notifyDataSetChanged();
                break;
            case 2:
                mMessageList.remove(message);
                mPresenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            case 4:
                mPresenter.revokeMessage(message.getMessage());
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (mChatInput.getText().length() > 0) {
            TextMessage message = new TextMessage(mChatInput.getText());
            mPresenter.saveDraft(message.getMessage());
        } else {
            mPresenter.saveDraft(null);
        }
        mPresenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            mAdapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (mMessage instanceof CustomMessage){
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType){
                        //正在输入
                        case TYPING:
                            mTitleBar.setTitle(getString(R.string.chat_typing));
                            Flowable.timer(3000, TimeUnit.MILLISECONDS)
                                    .compose(RxJavaUtil.<Long>IO2Main())
                                    .compose(this.<Long>bindToLifecycle())
                                    .subscribe(new BaseObjectSubscriber<Long>() {
                                        @Override
                                        public void onSuccess(Long aLong) {
                                            mTitleBar.setTitle(mTitleStr);
                                        }

                                        @Override
                                        public void onFailure(Throwable throwable) {

                                        }
                                    });
                            break;
                        default:
                            break;
                    }
                }else{
                    if (mMessageList.size()==0){
                        mMessage.setHasTime(null);
                    }else{
                        mMessage.setHasTime(mMessageList.get(mMessageList.size()-1).getMessage());
                    }
                    mMessageList.add(mMessage);
                    mAdapter.notifyDataSetChanged();
                    //滑动到底部
                    mRecyclerView.scrollToPosition(mAdapter.getItemCount()-1);
                }

            }
        }
    }

    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i){
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted) {
                continue;
            }
            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) {
                continue;
            }
            ++newMsgNum;
            if (i != messages.size() - 1){
                mMessage.setHasTime(messages.get(i+1));
                mMessageList.add(0, mMessage);
            }else{
                mMessage.setHasTime(null);
                mMessageList.add(0, mMessage);
            }
        }
        if (newMsgNum == 0) {
            return;
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(newMsgNum == 0 ? 0 : newMsgNum - 1);
    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {
        for (Message msg : mMessageList) {
            TIMMessageExt ext = new TIMMessageExt(msg.getMessage());
            if (ext.checkEquals(timMessageLocator)) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void clearAllMessage() {
        mMessageList.clear();
    }

    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : mMessageList){
            if (msg.getMessage().getMsgUniqueId() == id){
                switch (code){
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc(getString(R.string.chat_content_bad));
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }


    @Override
    public void sendPhoto() {
//        startActivity(TwoActivity.class);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
        File tempFile = FileUtil.imageFile();
//        File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
        Uri photoUri;
        if (Build.VERSION.SDK_INT>=24)
        {
            photoUri = FileProvider.getUriForFile(this,"com.example.helloworld.fileprovider",tempFile);
        }
        else
        {
            photoUri = Uri.fromFile(tempFile);
        }
//        Uri photoUri = Uri.fromFile(tempFile); // 传递路径
        mFileUri = photoUri;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intentPhoto.resolveActivity(getPackageManager()) != null) {
//
//            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
//            if (tempFile != null) {
//                mFileUri = Uri.fromFile(tempFile);
//            }
////            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
////            intentPhoto.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//            intentPhoto.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
//            intentPhoto.putExtra("return-data", true);
//            startActivityForResult(intentPhoto, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            ContentValues contentValues = new ContentValues(2);
//            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
//            String filePath = tempFile.getAbsolutePath();//要保存照片的绝对路径
//
//            contentValues.put(MediaStore.Images.Media.DATA, filePath);
//            //如果想拍完存在系统相机的默认目录,改为
//            //contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "111111.jpg");
//
//            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//            Uri mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
//            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }
    }

    @Override
    public void sendText() {
        Message message = new TextMessage(mChatInput.getText());
        mPresenter.sendMessage(message.getMessage());
        mChatInput.setText("");
    }

    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }

    @Override
    public void startSendVoice() {
        mVoiceSendingView.setVisibility(View.VISIBLE);
        mVoiceSendingView.showRecording();
        recorder.startRecording();
    }

    @Override
    public void endSendVoice() {
        mVoiceSendingView.release();
        mVoiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else if (recorder.getTimeInterval() > 60) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_long), Toast.LENGTH_SHORT).show();
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            mPresenter.sendMessage(message.getMessage());
        }
    }

    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        mPresenter.sendMessage(message.getMessage());
    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void sending() {
        if (mType == TIMConversationType.C2C){
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            mPresenter.sendOnlineMessage(message.getMessage());
        }
    }

    @Override
    public void showDraft(TIMMessageDraft draft) {
        mChatInput.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    private void showImagePreview(String path){
        if (path == null) {
            return;
        }
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    private void sendFile(String path){
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (file.exists()){
            if (file.length() > 1024 * 1024 * 10){
                Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
            }else{
                Message message = new FileMessage(path);
                mPresenter.sendMessage(message.getMessage());
            }
        }else{
            Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && mFileUri != null) {
                showImagePreview(FileUtil.imageFile().getAbsolutePath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW){
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri",false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists()){
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    if (file.length() == 0 && options.outWidth == 0) {
                        Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                    }else {
                        if (file.length() > 1024 * 1024 * 10){
                            Toast.makeText(this, getString(R.string.chat_file_too_large),Toast.LENGTH_SHORT).show();
                        }else{
                            Message message = new ImageMessage(path,isOri);
                            mPresenter.sendMessage(message.getMessage());
                        }
                    }
                }else{
                    Toast.makeText(this, getString(R.string.chat_file_not_exist),Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == VIDEO_RECORD) {
            if (resultCode == RESULT_OK) {
                String videoPath = data.getStringExtra("videoPath");
                String coverPath = data.getStringExtra("coverPath");
                long duration = data.getLongExtra("duration", 0);
                Message message = new UGCMessage(videoPath, coverPath, duration);
                mPresenter.sendMessage(message.getMessage());
            }
        }

    }

    @Override
    public void videoAction() {
        ToastUtil.showLongToast("主播端录制页面");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }
}
