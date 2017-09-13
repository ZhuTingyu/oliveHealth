package com.olive.ui.upgrade;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

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

    Context mContext;
    ProgressDialog mUpgradeDialog;
    UpgradeViewModel viewModel;
    String apkUrl;
    VersionEntity versionEntity;

    public UpgradeManager(Context context) {
        mContext = context;
        viewModel = new UpgradeViewModel(context);

    }

    public void checkUpdate() {
        viewModel.update(versionEntity -> {
            this.versionEntity = versionEntity;
            if(VersionModel.getHisUpgradeVersion() < versionEntity.version){
                apkUrl = versionEntity.url;
                //apkUrl = "http://gdown.baidu.com/data/wisegame/65f486476dcc3567/jinritoutiao_634.apk";
                if(versionEntity.forceUpdate == IS_FORCE_UPDATE){
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

        if(versionEntity.forceUpdate == IS_FORCE_UPDATE){
            mUpgradeDialog.setTitle(mContext.getResources().getString(R.string.message_is_force_updating));
            mUpgradeDialog.setCanceledOnTouchOutside(false);
        }else {
            mUpgradeDialog.setTitle(mContext.getResources().getString(R.string.message_downloading_update_app));
            mUpgradeDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getResources().getString(R.string.text_sure), (dialog, which) -> {
            });
        }
        mUpgradeDialog.show();
    }

    private void update() {
        DownloadUtil.getInstance().download(apkUrl, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String filePath) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + filePath),"application/vnd.android.package-archive");
                mContext.startActivity(intent);
                mUpgradeDialog.dismiss();
            }

            @Override
            public void onDownloading(int progress) {
                mUpgradeDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed() {

            }
        });
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

}