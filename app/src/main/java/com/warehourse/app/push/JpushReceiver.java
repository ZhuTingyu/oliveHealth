package com.warehourse.app.push;

import com.google.gson.reflect.TypeToken;


import com.biz.push.PushManager;
import com.biz.util.GsonUtil;
import com.biz.util.LogUtil;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.main.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            if (bundle != null) {
                LogUtil.print("" + bundle.getString(JPushInterface.EXTRA_EXTRA));
                try {
                    Map<String, String> stringStringMap = GsonUtil.fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), new TypeToken<Map<String, String>>() {
                    }.getType());
                    if (stringStringMap != null) {
                        String url = stringStringMap.get("url");
                        LogUtil.print("" + stringStringMap.get("url"));
                        if (!PushManager.getInstance().shouldInit()) {
                            LogUtil.print("be killed  launcher" + stringStringMap.get("url"));
                            Intent intent1 = context.getPackageManager().
                                    getLaunchIntentForPackage("com.warehourse.b2b");
                            intent1.setData(Uri.parse(url));
                            intent1.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            context.startActivity(intent1);
                        } else {
                            LogUtil.print("main " + stringStringMap.get("url"));
                            Intent intent1 = new Intent(context, MainActivity.class);
                            intent1.setData(Uri.parse(url));
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        }
                    }
                } catch (Exception e) {
                }
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())
                || JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            UserModel.getInstance().updateUserStatus();
        }
    }

}
