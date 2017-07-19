package com.biz.base;


import com.biz.http.R;
import com.biz.util.ActivityStackManager;
import com.biz.util.DialogUtil;
import com.biz.util.Lists;
import com.biz.util.StatusBarHelper;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

import rx.functions.Action1;

/**
 * Title: BaseActivity
 * Description: activity基类
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:3/8/16  上午10:58
 *
 * @author johnzheng
 * @version 1.0
 */

@SuppressWarnings("deprecation")
public class BaseActivity extends RxBaseActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    ViewGroup rootView;
    protected View mProgressView, mLoadingView;

    protected AppBarLayout mAppBarLayout;
    @Nullable
    protected Toolbar mToolbar;
    protected List<FragmentBackHelper> fragmentBackHelperList;
    private RxPermissions mRxPermission;


    public void setFragmentBackHelper(FragmentBackHelper i) {
        if (i != null)
            fragmentBackHelperList.add(i);
    }

    public RxPermissions getRxPermission() {
        if (mRxPermission == null)
            mRxPermission = new RxPermissions(getActivity());
        return mRxPermission;
    }

    public void removeFragmentBackHelper(FragmentBackHelper i) {
        if (i != null && fragmentBackHelperList.contains(i))
            fragmentBackHelperList.remove(i);
    }

    public void dismissKeyboard() {
        try {
            this.getCurrentFocus().clearFocus();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }

    }

    public void setViewDisableDelay(final View view) {
        view.setEnabled(false);
        view.postDelayed(() -> {
            view.setEnabled(true);
        }, 600);
    }

    protected void initProgressLayout() {
        if (mProgressView == null) {
            mProgressView = getLayoutInflater().inflate(R.layout.loading_layout, rootView
                    , false);
            mProgressView.setOnClickListener(v -> {
            });
            mLoadingView = mProgressView.findViewById(R.id.loading_view);
            setProgressVisible(false);
            rootView.addView(mProgressView);
        }
    }

    public void setProgressVisible(boolean show) {
        if (mProgressView != null) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

        }
    }

    public int getColors(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    @Override
    public void onBackPressed() {
        for (int i = fragmentBackHelperList.size() - 1; i > -1; i--) {
            if (fragmentBackHelperList.get(i).onBackPressed()) return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    public void startActivity(Intent intent, boolean isBack) {
        super.startActivity(intent);
//        if (isBack)
//            overridePendingTransition(R.anim.left_in, R.anim.right_out);
//        else
//            overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    protected <T extends View> T getView(@IdRes int resId) {
        T t = (T) findViewById(resId);
        if (t == null) {
            throw new IllegalArgumentException("view 0x" + Integer.toHexString(resId)
                    + " doesn't exist");
        }
        return t;
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarBackDrawable(mToolbar);
        rootView = (ViewGroup) getWindow().getDecorView();
        initProgressLayout();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarBackDrawable(mToolbar);
        rootView = (ViewGroup) getWindow().getDecorView();
        initProgressLayout();
    }


    public void setToolbarBackDrawable(Toolbar mToolbar) {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);

        if (null != mToolbar) {

            mToolbar.setNavigationOnClickListener(e -> onBackPressed());
        }
        if (null != mAppBarLayout && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            View.inflate(getActivity(), R.layout.line_dark, mAppBarLayout);
        }
    }

    public void showToast(@StringRes int stringRes) {
        Snackbar.make(getWindow().getDecorView(), stringRes, Snackbar.LENGTH_LONG).show();
    }

    public void showToast(View view, @StringRes int stringRes) {
        Snackbar.make(view==null?getWindow().getDecorView():view, stringRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentBackHelperList = Lists.newArrayList();
        StatusBarHelper.Builder(this).setStatusBarLightMode(true);
        super.onCreate(savedInstanceState);
        ActivityStackManager.add(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
        MobclickAgent.onResume(this);
    }

    public void error(String error) {
        setProgressVisible(false);
        if (!TextUtils.isEmpty(error)) {
            DialogUtil.createDialogView(getActivity(), error, (dialog, which) -> {
                dialog.dismiss();
            }, R.string.btn_confirm);
        }
    }

    @Override
    public void error(int code, String error) {
        if (code == 2400) {
            setProgressVisible(false);
            finish();
            return;
        }
        error(error);
    }

    /**
     * 检查权限
     * permission 获取方式： Manifest.permission.CAMERA
     */
    protected void checkAppPermission(String permission, Action1<? super Boolean> onNext) {
        bindUi(getRxPermission().request(permission), onNext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.remove(this);

    }

}
