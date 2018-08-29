package com.example.chenwentong.helloworld;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.chenwentong.helloworld.base.BaseActivity;
import com.example.chenwentong.helloworld.ui.home.CircleFragment;
import com.example.chenwentong.helloworld.ui.home.ConversationFragment;
import com.example.chenwentong.helloworld.ui.home.HomeFragment;
import com.example.common.base.CBaseActivity;
import com.example.common.widget.adapter.ViewPagerAdapter;
import com.example.common.widget.navigation.BottomNavigationViewHelper;
import com.tencent.bugly.crashreport.CrashReport;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mViewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(ConversationFragment.newInstance());
        viewPagerAdapter.addFragment(HomeFragment.newInstance(getString(R.string.tab_second)));
        viewPagerAdapter.addFragment(CircleFragment.newInstance());
        viewPagerAdapter.addFragment(HomeFragment.newInstance(getString(R.string.tab_four)));
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        initBottomNavigation();

    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper bottomNavigationViewHelper = new BottomNavigationViewHelper(bottomNavigation, mViewPager);
        bottomNavigationViewHelper.disableShiftMode();
        bottomNavigationViewHelper.setNavigationSelectListener(false);
    }
}
