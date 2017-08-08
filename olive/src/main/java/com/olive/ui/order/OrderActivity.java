package com.olive.ui.order;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;


import com.biz.base.BaseActivity;
import com.biz.base.FragmentAdapter;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.olive.R;
import com.olive.ui.order.viewModel.OrderListViewModel;

import java.util.List;

public class OrderActivity extends BaseActivity {
    public static final int ORDER_TAB_ALL = 0;
    public static final int ORDER_TAB_PAY = 1;
    public static final int ORDER_TAB_SEND = 2;
    public static final int ORDER_TAB_RECEIVE = 3;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    FragmentAdapter mFragmentAdapter;

    @NonNull
    protected List<String> mTitles;
    private List<Fragment> mFragments = Lists.newArrayList();

    public static final void startOrder(Activity activity, int tab) {
        IntentBuilder.Builder()
                .setClass(activity, OrderActivity.class)
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
        mToolbar.setTitle(getString(R.string.text_my_order));


        mTitles = Lists.newArrayList(getString(R.string.text_order_all),
                getString(R.string.text_waiting_pay),
                getString(R.string.text_wait_send),
                getString(R.string.text_wait_receive),
                getString(R.string.text_order_complete),
                getString(R.string.text_order_cancel));

        for(int i = 0; i < mTitles.size(); i++){
            OrderListInfoFragment fragment = new OrderListInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(OrderListViewModel.KEY_TYPE,mTitles.get(i));
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }

        initTabLayout();
    }

    private void initTabLayout() {
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(mTitles.size());
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Uri uri = getIntent().getData();
        if (uri != null) {
            mViewPager.setCurrentItem(Utils.getInteger(uri.getQueryParameter(IntentBuilder.KEY_PAGE_INDEX)));
        }
    }

}
