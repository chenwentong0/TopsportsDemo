package com.example.chenwentong.helloworld.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenwentong.helloworld.R;
import com.example.common.base.BaseFragment;

/**
 * @author by chen.wentong on 2018/8/21.
 * 首页
 */

public class HomeFragment extends BaseFragment {
    private static final String KEY_TITLE = "title";
    private TextView mTextView;

    public static HomeFragment newInstance(String title) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        String title = arguments.getString(KEY_TITLE);
        mTextView = mRootView.findViewById(R.id.text_view);
        if (!TextUtils.isEmpty(title)) {
            mTextView.setText(title);
        }
    }
}
