package com.example.chenwentong.helloworld.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.base.CBaseFragment;

import butterknife.ButterKnife;

/**
 * Date 2018/8/24
 * Time 12:13
 *
 * @author wentong.chen
 */
public abstract class BaseFragment extends CBaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }
}
