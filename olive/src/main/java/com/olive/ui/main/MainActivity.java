package com.olive.ui.main;

import com.biz.base.BaseActivity;
import com.biz.base.FragmentAdapter;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.BadgeView;
import com.biz.widget.BottomNavigationViewEx;
import com.olive.R;
import com.olive.ui.main.cart.CartFragment;
import com.olive.ui.main.category.CategoryFragment;
import com.olive.ui.main.home.HomeFragment;
import com.olive.ui.main.my.UserFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;

/**
 * Title: MainActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:13/03/2017  11:01
 *
 * @author johnzheng
 * @version 1.0
 */

public class MainActivity extends BaseActivity {


    public static final int TAB_CART = 2;
    public static final int TAB_HOME = 0;

    private BottomNavigationViewEx mBottomNavigationView;
    private ViewPager mViewPager;
    private BadgeView cartBadgeView;
    protected Boolean isFirst = true;




    public static final void startMain(Activity activity, int tab) {
        IntentBuilder.Builder()
                .setClass(activity, MainActivity.class)
                .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .addFlag(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                .putExtra(IntentBuilder.KEY_ID, tab)
                .startActivity();
    }

    public static final void startMainWithAnim(Activity activity, int tab) {
        IntentBuilder.Builder()
                .setClass(activity, MainActivity.class)
                .overridePendingTransition(R.anim.right_in, R.anim.left_out)
                .addFlag(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                .putExtra(IntentBuilder.KEY_ID, tab)
                .startActivity();
    }

    public void onBackPressed() {
        for (int i = fragmentBackHelperList.size() - 1; i > -1; i--) {
            if (fragmentBackHelperList.get(i).onBackPressed()) return;
        }
        if (isFirst) {
            showToast(mViewPager, R.string.toast_back_again);
            isFirst = false;
            subscription.add(Observable.just(1).delay(3500, TimeUnit.MILLISECONDS).subscribe(s -> {
                isFirst = true;
            }));
            return;
        }
        ActivityCompat.finishAffinity(this);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Home);
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main_layout);
        mBottomNavigationView = getView(R.id.design_navigation_view);
        mBottomNavigationView.enableAnimation(false);
        mBottomNavigationView.enableItemShiftingMode(false);
        mBottomNavigationView.enableShiftingMode(false);
        mViewPager = getView(R.id.viewpager);
        cartBadgeView = (BadgeView) mBottomNavigationView
                .getBottomNavigationItemViews()[2]
                .findViewById(R.id.text_unread);

        List<String> titles = Lists.newArrayList(
                getString(R.string.action_home),
                getString(R.string.action_category),
                getString(R.string.action_cart),
                getString(R.string.action_my));
        List<Fragment> fragments = Lists.newArrayList(new HomeFragment(),
                new CategoryFragment(),
                new CartFragment(),
                new UserFragment());
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments, titles));
        mBottomNavigationView.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setAnimationCacheEnabled(false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        int tab = intent.getIntExtra(IntentBuilder.KEY_ID, 0);
        mBottomNavigationView.setCurrentItem(tab);

        setIntent(null);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void error(String error) {
        setProgressVisible(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Object event) {
        if (event != null) {

        }
    }
}
