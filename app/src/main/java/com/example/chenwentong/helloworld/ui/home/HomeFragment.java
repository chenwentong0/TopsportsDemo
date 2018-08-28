package com.example.chenwentong.helloworld.ui.home;

import android.os.Bundle;

import com.example.chenwentong.helloworld.R;
import com.example.chenwentong.helloworld.base.BaseFragment;

/**
 * @author by chen.wentong on 2018/8/21.
 * 首页
 */

public class HomeFragment extends BaseFragment {
    private static final String KEY_TITLE = "title";

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
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

}
