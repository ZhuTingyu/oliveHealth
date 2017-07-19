package com.warehourse.app.ui.main;

import com.biz.base.BaseActivity;
import com.biz.base.FragmentAdapter;
import com.biz.http.UrlSinger;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.StatusBarHelper;
import com.biz.widget.BadgeView;
import com.biz.widget.BottomNavigationViewEx;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.event.CartCountEvent;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.adv.AdvertisingFragment;
import com.warehourse.app.ui.cart.CartFragment;
import com.warehourse.app.ui.category.CategoryFragment;
import com.warehourse.app.ui.home.HomeFragment;
import com.warehourse.app.ui.my.UserFragment;
import com.warehourse.app.ui.web.WebViewActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;

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

    private DrawerLayout mDrawerLayout;
    private BottomNavigationViewEx mBottomNavigationView;
    private ViewPager mViewPager;
    private BadgeView cartBadgeView;
    protected Boolean isFirst = true;
    private MainViewModel mViewModel;

    private void startUri(Intent intent) {
        if (intent.getData() != null) {
            String url = intent.getData().toString();
            startUri(getActivity(), url);
        }
    }

    public static void startUri(Context context, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Uri uri = Uri.parse(url);
                boolean isLogin = uri.getBooleanQueryParameter(IntentBuilder.KEY_LOGIN, false);
                boolean isGlobal = uri.getBooleanQueryParameter(IntentBuilder.KEY_GLOBAL, false);

                if (uri.getPath().contains("/search.do")
                        || uri.getPath().contains("/detail.do")) {

                } else if (isLogin && !UserModel.getInstance().isLogin()) {
                    UserModel.getInstance().createContactDialog(context);
                    return;
                }
                if (isGlobal) {
                    url = UrlSinger.builder().url(url)
                            .userId(UserModel.getInstance().getUserId())
                            .toUrl();
                    uri = Uri.parse(url);
                }
                IntentBuilder.Builder(IntentBuilder.ACTION_VIEW)
                        .setData(uri)
                        .startActivity((Activity) context);
            }
        } catch (Exception e) {
            IntentBuilder.Builder(IntentBuilder.ACTION_VIEW_WEB)
                    .setClass(context, WebViewActivity.class)
                    .setData(Uri.parse(url))
                    .startActivity((Activity) context);
        }
    }

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
                .overridePendingTransition(R.anim.left_in, R.anim.right_out)
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
        mViewModel = new MainViewModel(this);
        initViewModel(mViewModel);
        StatusBarHelper.Builder(this).setStatusBarLightMode(false);

        setContentView(R.layout.activity_main_layout);
        mDrawerLayout = getView(R.id.drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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


        mViewModel.queryCartCount();
        startUri(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        int tab = intent.getIntExtra(IntentBuilder.KEY_ID, 0);
        mBottomNavigationView.setCurrentItem(tab);
        startUri(intent);
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

    public void onEventMainThread(CartCountEvent event) {
        if (event != null) {
            if (UserModel.getInstance().isLogin()) {
                cartBadgeView.setText("" + event.count);
            } else {
                cartBadgeView.setText("");
            }
        }
    }
}
