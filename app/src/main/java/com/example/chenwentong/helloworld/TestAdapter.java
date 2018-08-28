package com.example.chenwentong.helloworld;

import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Date 2018/8/27
 * Time 11:57
 *
 * @author wentong.chen
 */
public class TestAdapter extends BaseQuickAdapter<String, BaseViewHolder>{
    public TestAdapter() {
        super(R.layout.item_conversation, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        LogUtils.d(TAG, "testadapter 开始刷新" + item);
    }
}
