package com.example.chenwentong.helloworld.ui.home;

import com.example.chenwentong.helloworld.R;
import com.example.chenwentong.helloworld.base.BaseWebviewFragment;

/**
 * Date 2018/8/28
 * Time 15:29
 *
 * @author wentong.chen
 */
public class CircleFragment extends BaseWebviewFragment {

    public static CircleFragment newInstance() {
        CircleFragment fragment = new CircleFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initData() {
        loadUrl("https://x5.tencent.com/tbs/guide/sdkInit.html");
    }
}
