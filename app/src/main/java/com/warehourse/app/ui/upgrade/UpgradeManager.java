package com.warehourse.app.ui.upgrade;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import com.warehourse.app.R;
import com.warehourse.app.model.entity.UpgradeEntity;
import com.warehourse.app.ui.launch.LaunchActivity;


/**
 * Title: UpgradeManager
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/1/3  15:30
 *
 * @author wangwei
 * @version 1.0
 */
public class UpgradeManager implements Application.ActivityLifecycleCallbacks {
    private UpgradeViewModel viewModel;
    private boolean isOpen;
    private Dialog dialogUpgrade;
    private long updateTime = 0;
    public long intervalTime = 1000 * 60 * 20;
    private boolean isUpdate;
    private Activity activity;
    private UpgradeFragment mUpgradeFragment;

    public static void register(Application application) {
        if (application != null)
            application.registerActivityLifecycleCallbacks(new UpgradeManager());
    }

    public UpgradeManager() {
        viewModel = new UpgradeViewModel(null);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        this.activity = activity;
        if (activity instanceof LaunchActivity) {
            isOpen = false;
            viewModel.update(b -> {
            });
        } else {
            if (isOpen) {
                return;
            }
            viewModel.onResume(upgradeInfo -> {
                isOpen = true;
                showUpgradeDialog(upgradeInfo, UpgradeManager.this.activity);
            });
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 升级处理
     */
    public void showUpgradeDialog(UpgradeEntity upgradeInfo, Activity activity) {
        showFragment(upgradeInfo, activity);
    }
    private void showFragment(UpgradeEntity upgradeInfo, Activity activity){
        if (activity != null&& activity instanceof FragmentActivity
                && upgradeInfo != null && upgradeInfo.need ){
            if (mUpgradeFragment ==null){
                mUpgradeFragment =  UpgradeFragment.newInstance(upgradeInfo);
            }

            FragmentActivity fragmentActivity = (FragmentActivity) activity;
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .add(android.R.id.content, mUpgradeFragment, UpgradeFragment.class.getName())
                    .show(mUpgradeFragment)
                    .commitAllowingStateLoss();
        }
    }

    private void showDialog(UpgradeEntity upgradeInfo, Activity activity){
        if (upgradeInfo != null && upgradeInfo.need && activity != null) {
            if (upgradeInfo.force) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.dialog_title_upgrade, upgradeInfo.version));
                builder.setMessage(upgradeInfo.info);
                builder.setPositiveButton(R.string.btn_upgrade, (d, w) -> {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(upgradeInfo.url));
                    try {
                        activity.startActivity(intent);
                        activity.getWindow().getDecorView().postDelayed(()->{
                            showUpgradeDialog(upgradeInfo, activity);
                        },2000);
                    } catch (Exception e) {
                    }
                });
                builder.setCancelable(false);
                if (dialogUpgrade != null)
                    dialogUpgrade.dismiss();
                dialogUpgrade = builder.show();
                return;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(activity.getString(R.string.dialog_title_upgrade, upgradeInfo.version));
                builder.setMessage(upgradeInfo.info);
                builder.setPositiveButton(R.string.btn_upgrade, (d, w) -> {
                    d.dismiss();
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(upgradeInfo.url));
                    try {
                        activity.startActivity(intent);
                    } catch (Exception e) {
                    }
                });
                builder.setNegativeButton(R.string.btn_cancel, (d, w) -> {
                    d.dismiss();
                    if (viewModel != null)
                        viewModel.cancel();
                });
                if (dialogUpgrade != null)
                    dialogUpgrade.dismiss();
                dialogUpgrade = builder.show();
            }
        }
    }
}