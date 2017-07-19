package com.biz.http;

import com.biz.application.BaseApplication;
import com.biz.http.dispatcher.DispatcherUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class NetConnectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        if (activeInfo != null) {
            ParaAndroidConfig.getInstance().init(BaseApplication.getAppContext());
            if (mobileInfo != null && mobileInfo.isConnected()) {
                DispatcherUtil.getInstance().dispatcher("3G/4G");
            } else if (wifiInfo != null && wifiInfo.isConnected()) {
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                DispatcherUtil.getInstance().dispatcher(ParaAndroidConfig.getInstance().routerMac);
            }
        }
    }

}