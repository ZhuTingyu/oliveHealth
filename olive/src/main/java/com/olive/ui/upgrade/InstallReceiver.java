package com.olive.ui.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.olive.app.OliveApplication;
import com.olive.model.VersionModel;

/**
 * Created by TingYu Zhu on 2017/9/18.
 */

public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            if(packageName.equals(OliveApplication.getApplication().getPackageName())){
                VersionModel.getInstance().saveHisUpgradeVersion();
            }
        }
    }
}
