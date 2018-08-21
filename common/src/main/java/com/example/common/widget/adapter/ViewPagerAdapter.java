package com.example.common.widget.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by chen.wentong on 2018/8/21.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment aFragment) {
        mFragmentList.add(aFragment);
    }

    public void addFragment() {

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }

    public List<Fragment> getFragmentsList() {
        return mFragmentList;
    }
}
