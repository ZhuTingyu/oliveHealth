package com.olive.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.biz.util.DialogUtil;
import com.olive.R;


/**
 * Created by TingYu Zhu on 2017/10/30.
 */

public class PermissionManager {

    private static final String SCHEME = "package";
    public static final int PERMISSION_CODE = 0X234;

    Activity context;

    public PermissionManager(Activity context){
        this.context = context;
    }

    public void requestPermissions(String... permissions){

        if(checkPermissions(permissions)){
            createHintSetPermissionsDialog();
        }

    }

    private boolean checkPermissions(String... permissions) {
        for (String permission : permissions) {
            if(lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void createHintSetPermissionsDialog(){
        DialogUtil.createNotCancelableDialogView(context,R.string.message_permission_hint, (dialog, which) -> {
                    ActivityCompat.finishAffinity(context);
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    context.startActivity(intent);
        }, R.string.btn_cancel
        ,(dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    Uri uri = Uri.fromParts(SCHEME, context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivityForResult(intent, PERMISSION_CODE);

                }, R.string.string_set);
    }

}
