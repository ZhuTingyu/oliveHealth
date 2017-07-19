package com.warehourse.app.ui.launch;

import com.biz.base.BaseActivity;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.widget.CustomDraweeView;
import com.biz.widget.banner.PageIndicatorView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.util.LoadImageUtil;
import com.warehouse.dao.AvgBean;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Title: LaunchActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/3/21  15:13
 *
 * @author wangwei
 * @version 1.0
 */
public class LaunchActivity extends BaseActivity {

    LaunchViewModel mViewModel;
    private final int PageSize = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        hideNavigation();
        setContentView(R.layout.activity_launch_layout);

        mViewModel = new LaunchViewModel(this);
        initViewModel(mViewModel);


        WareApplication.getApplication().setShowAdv(false);
        subscription.clear();
        if (mViewModel.isFirstLaunch()) {
            setViewPager();
        } else if (getIntent().getData() == null) {
            subscription.add(Observable.just(1).delay(2000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::startMain));
            mViewModel.requestAvg(this::setAdsView);
        } else if (getIntent().getData() != null) {
            startMain();
        }

        getRxPermission().request(mViewModel.permissions).subscribe(b -> {
            if (!b) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.text_error_permission));
                builder.setPositiveButton(R.string.btn_confirm, (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.setOnDismissListener(dialog -> {
                    startMain(dialog);
                });
                builder.show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        subscription.add(Observable.just(1).delay(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::startMain));
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscription.clear();
    }

    private void startMain() {
        startUrl(getIntent().getData().toString());
    }

    private void startUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            IntentBuilder.Builder(this, MainActivity.class)
                    .setData(Uri.parse(url))
                    .addFlag(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .finish(this)
                    .startActivity();
        }
    }

    private void startMain(Object o) {
        IntentBuilder.Builder(this, MainActivity.class)
                .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .finish(this)
                .startActivity();
    }

    private void setAdsView(AvgBean avgBean) {
        subscription.clear();
        subscription.add(Observable.just(1).delay(avgBean.getStopTime(), TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::startMain));
        ViewStub viewStub = (ViewStub) findViewById(R.id.stub_ads);
        viewStub.inflate();
        CustomDraweeView iconAds = (CustomDraweeView) findViewById(R.id.icon_ads);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        iconAds.setSize(dm.widthPixels, dm.heightPixels);
        iconAds.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        LoadImageUtil.Builder().load(avgBean.getImgUrl()).http().build().displayImage(iconAds);

        iconAds.setOnClickListener(v -> {
            subscription.clear();
            startUrl(avgBean.getClickUrl());
        });
    }

    private void setViewPager() {
        ViewStub viewStub = (ViewStub) findViewById(R.id.stub_viewpager);
        ViewStub btnStub = (ViewStub) findViewById(R.id.stub_btn);
        btnStub.inflate();
        viewStub.inflate();
        TextView btn1 = (TextView) findViewById(R.id.btn_1);
        btn1.setVisibility(View.GONE);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setVisibility(View.VISIBLE);
        viewpager.setOffscreenPageLimit(PageSize);
        PageIndicatorView pageIndicatorView = getView(R.id.indicator);
        pageIndicatorView.setIndicatorResId(R.drawable.ic_launch_indicator);
        pageIndicatorView.setIndicatorResIdFocus(R.drawable.ic_launch_indicator_focus);
        pageIndicatorView.setViewPager(viewpager);
        viewpager.setAdapter(new Adapter());
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                btn1.setVisibility((position == PageSize - 1) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn1.setOnClickListener(v -> {
            startMain(v);
            mViewModel.setFirstLaunch();
        });
    }


    private class Adapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PageSize;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CustomDraweeView draweeView = (CustomDraweeView)
                    View.inflate(container.getContext(), R.layout.stub_drawee_layout, null);
            draweeView.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            container.addView(draweeView);
            loadImages(draweeView, position);
            return draweeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.getChildAt(position));
        }

        private void loadImages(CustomDraweeView draweeView, int position) {
            if (position == 0) {
                LoadImageUtil.Builder()
                        .loadAssets("loading1.jpg").build()
                        .imageOptions(R.color.color_transparent)
                        .displayImage(draweeView);
            }
            if (position == 1) {
                LoadImageUtil.Builder()
                        .load("loading2.jpg").assets().build()
                        .imageOptions(R.color.color_transparent)
                        .displayImage(draweeView);
            }
            if (position == 2) {
                LoadImageUtil.Builder()
                        .load("loading3.jpg").assets().build()
                        .imageOptions(R.color.color_transparent)
                        .displayImage(draweeView);
            }
            if (position == 3) {

                LoadImageUtil.Builder()
                        .load("loading4.jpg").assets().build()
                        .imageOptions(R.color.color_transparent)
                        .displayImage(draweeView);
            }
        }
    }

    private void hideNavigation() {
        int flags;
        int curApiVersion = android.os.Build.VERSION.SDK_INT;
        if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

        } else {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

}
