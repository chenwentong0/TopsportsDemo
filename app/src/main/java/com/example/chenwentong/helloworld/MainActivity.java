package com.example.chenwentong.helloworld;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.chenwentong.helloworld.ui.HomeFragment;
import com.example.common.base.BaseActivity;
import com.example.common.widget.adapter.ViewPagerAdapter;
import com.example.common.widget.navigation.BottomNavigationViewHelper;

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
        viewPagerAdapter.addFragment(HomeFragment.newInstance(getString(R.string.tab_first)));
        viewPagerAdapter.addFragment(HomeFragment.newInstance(getString(R.string.tab_second)));
        viewPagerAdapter.addFragment(HomeFragment.newInstance(getString(R.string.tab_three)));
        viewPagerAdapter.addFragment(HomeFragment.newInstance(getString(R.string.tab_four)));
        mViewPager.setAdapter(viewPagerAdapter);
        initBottomNavigation();

    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper bottomNavigationViewHelper = new BottomNavigationViewHelper(bottomNavigation, mViewPager);
        bottomNavigationViewHelper.disableShiftMode();
        bottomNavigationViewHelper.setNavigationSelectListener(false);
    }
}
