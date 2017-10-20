package com.olive.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by TingYu Zhu on 2017/10/20.
 */

public class PermissionsChecker {

    Activity context;

    public PermissionsChecker(Context context) {
        this.context = (Activity) context;
    }

    public boolean checkPermissions(String... permissions) {
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

}
