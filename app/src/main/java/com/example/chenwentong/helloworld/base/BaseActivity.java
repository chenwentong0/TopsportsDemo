package com.example.chenwentong.helloworld.base;

import android.view.View;

import com.example.chenwentong.helloworld.R;
import com.example.common.base.CBaseActivity;
import com.example.common.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date 2018/8/24
 * Time 12:06
 *
 * @author wentong.chen
 */
public abstract class BaseActivity extends CBaseActivity {
    protected TitleBar mTitleBar;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mTitleBar = findViewById(R.id.title_bar);
        if (mTitleBar != null) {
            mTitleBar.setLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickBackBtn(v);
                }
            });
        }
    }

    /**
     * 点击返回按钮的回调
     */
    protected void onClickBackBtn(View view) {
        finish();
    }
}
