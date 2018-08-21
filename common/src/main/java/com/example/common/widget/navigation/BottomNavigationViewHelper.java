package com.example.common.widget.navigation;

import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * 底部导航栏帮助类
 */
public class BottomNavigationViewHelper {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    private MenuItem mMenuItem;
    private int mCurPosition;

    public BottomNavigationViewHelper(BottomNavigationView navigationView, ViewPager viewPager) {
        mBottomNavigationView = navigationView;
        mViewPager = viewPager;
        addViewPagerChangeListener();
    }

    /**
     * 取消系统默认的动画效果
     */
    public void disableShiftMode() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置滑动监听
     */
    private void addViewPagerChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mMenuItem != null) {
                    mMenuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                mMenuItem = mBottomNavigationView.getMenu().getItem(position);
                mMenuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setTitles(@ArrayRes int res) {
        setTitles(Arrays.asList(mBottomNavigationView.getContext().getResources().getStringArray(res)));
    }

    public void setTitles(List<String> titles) {
        if (titles == null || titles.size() == 0) {
            return;
        }
        for (int i = 0; i < titles.size(); i++) {
            mBottomNavigationView.getMenu().getItem(i).setTitle(titles.get(i));
        }
    }

    public void setNavigationSelectListener(final boolean smoothScroll) {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int size = mBottomNavigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    if (mCurPosition == i) {
                        continue;
                    }
                    MenuItem menuItem = mBottomNavigationView.getMenu().getItem(i);
                    if (menuItem != null && menuItem == item && mViewPager.getAdapter().getCount() > i) {
                        mCurPosition = i;
                        mViewPager.setCurrentItem(i, smoothScroll);
                        break;
                    }
                }
                return false;
//                switch (item.getItemId()) {
//                    case R.id.item_consult:
//                        mViewPager.setCurrentItem(0, smoothScroll);
//                        break;
//                    case R.id.item_market:
//                        mViewPager.setCurrentItem(1, smoothScroll);
//                        break;
//
//                    case R.id.item_datas:
//                        mViewPager.setCurrentItem(2, smoothScroll);
//                        break;
//                    case R.id.item_settings:
//                        mViewPager.setCurrentItem(3, smoothScroll);
//                        break;
//                    default:
//                }
//
//                return false;
            }
        });
    }
}