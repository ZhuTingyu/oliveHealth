package com.olive.ui.upgrade;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.biz.util.DialogUtil;
import com.olive.R;
import com.olive.model.VersionModel;
import com.olive.model.entity.VersionEntity;
import com.olive.util.DownloadUtil;

/**
 * Created by TingYu Zhu on 2017/9/12.
 */

public class UpgradeManager {

    private static final int IS_FORCE_UPDATE = 1;
    public static final int INSTALL_CODE = 0x123;

    public boolean isForceUpdate = false;

    private String filePath;

    Activity mContext;
    ProgressDialog mUpgradeDialog;
    UpgradeViewModel viewModel;
    String apkUrl;
    VersionEntity versionEntity;

    public UpgradeManager(Context context) {
        mContext = (Activity) context;
        viewModel = new UpgradeViewModel(context);

    }

    public void checkUpdate() {
        viewModel.update(versionEntity -> {
            this.versionEntity = versionEntity;
            if(VersionModel.getHisUpgradeVersion() < versionEntity.version){
                apkUrl = versionEntity.url;
                //apkUrl = "http://gdown.baidu.com/data/wisegame/65f486476dcc3567/jinritoutiao_634.apk";
                if(versionEntity.forceUpdate == IS_FORCE_UPDATE){
                    isForceUpdate = true;
                    createUpgradeDialog();
                    update();
                }else {
                    createHintDialog();
                }
            }
        });
    }

    private void createUpgradeDialog() {
        mUpgradeDialog = new ProgressDialog(mContext);
        mUpgradeDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mUpgradeDialog.setMax(100);
        mUpgradeDialog.setCancelable(false);

        if(versionEntity.forceUpdate == IS_FORCE_UPDATE){
            mUpgradeDialog.setTitle(mContext.getResources().getString(R.string.message_is_force_updating));
        }else {
            mUpgradeDialog.setTitle(mContext.getResources().getString(R.string.message_downloading_update_app));
            mUpgradeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.text_cancel), (dialog, which) -> {
                DownloadUtil.getInstance().cancelDownload();
            });
        }
        mUpgradeDialog.show();
    }

    private void update() {
        DownloadUtil.getInstance().download(apkUrl, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String filePath) {
                installApk(filePath);
                setFilePath(filePath);
                mUpgradeDialog.dismiss();
            }

            @Override
            public void onDownloading(int progress) {
                mUpgradeDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed() {

            }
        }, mContext);
    }

    private void createHintDialog(){
        DialogUtil.createDialogView(mContext ,mContext.getResources().getString(R.string.message_whether_upgrade)
                ,(dialog, which) -> {
                    dialog.dismiss();
                },R.string.text_cancel
                ,(dialog, which) -> {
                    createUpgradeDialog();
                    update();
                },R.string.text_sure);
    }

    public void createHintForceDialog(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getResources().getString(R.string.message_force_updating_hint));
        builder.setNegativeButton(R.string.text_cancel, (dialog, which) -> {
            ActivityCompat.finishAffinity(mContext);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addCategory(Intent.CATEGORY_HOME);
            mContext.startActivity(intent);
        });
        builder.setPositiveButton(R.string.text_sure, (dialog, which) -> {
            installApk(filePath);
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void installApk(String filePath){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setDataAndType(Uri.parse("file://" + filePath),"application/vnd.android.package-archive");
        mContext.startActivityForResult(intent, INSTALL_CODE);
    }

    private void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
