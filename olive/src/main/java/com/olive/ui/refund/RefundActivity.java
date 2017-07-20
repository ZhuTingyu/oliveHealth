package com.olive.ui.refund;

import com.biz.base.BaseActivity;
import com.biz.base.FragmentAdapter;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.olive.R;
import com.olive.ui.main.my.FavoriteFragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.List;

public class RefundActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    FragmentAdapter mFragmentAdapter;

    @NonNull
    protected List<String> mTitles;
    private List<Fragment> mFragments = Lists.newArrayList();

    public static final void start(Activity activity, int tab) {
        IntentBuilder.Builder()
                .setClass(activity, RefundActivity.class)
                .putExtra(IntentBuilder.KEY_PAGE_INDEX, tab)
                .startActivity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        mToolbar.setTitle(R.string.text_refund);

        mTitles = Lists.newArrayList(getString(R.string.text_refund_apply),
                getString(R.string.text_refund_list));

        mFragments.add(new FavoriteFragment());
        mFragments.add(new FavoriteFragment());

        initTabLayout();
    }

    private void initTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayout.setupWithViewPager(mViewPager);
        Uri uri = getIntent().getData();
        if (uri != null) {
            mViewPager.setCurrentItem(Utils.getInteger(uri.getQueryParameter(IntentBuilder.KEY_PAGE_INDEX)));
        }
    }

}
