package com.biz.base;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import java.util.List;

/**
 * Title: FragmentAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/8/4  08:38
 * @author zhengyao
 * @version 1.0
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    RecyclerView.RecycledViewPool mPool;
    private List<Fragment> mFragments;
    private List<String> mTitles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
        mPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    public List<Fragment> getFragments() {
        return mFragments;
    }
    @Override
    public Fragment getItem(int position) {
        if(mFragments.get(position) instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) mFragments.get(position);
            fragment.setPool(mPool);
            return fragment;
        }else {
            return mFragments.get(position);
        }
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
