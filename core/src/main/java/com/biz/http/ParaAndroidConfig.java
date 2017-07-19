package com.biz.http;

import com.biz.application.BaseApplication;
import com.biz.http.push.xiaomi.MIUIUtils;
import com.biz.http.sign.SignIgnore;
import com.biz.http.sign.SignIgnoreAttr;
import com.biz.util.MD5;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.util.UUID;

final public class ParaAndroidConfig {
    @SignIgnore
    public final static String APN_WIFI = "wifi";
    @SignIgnore
    public final static String APN_GPRS = "wan";
    @SignIgnore
    public final static String APN_OTHER = "";
    @SignIgnore
    private static ParaAndroidConfig mParaAndroidConfig;

    public static ParaAndroidConfig getInstance() {
        if (mParaAndroidConfig == null) {
            mParaAndroidConfig = new ParaAndroidConfig();
            mParaAndroidConfig.init(BaseApplication.getAppContext());
        }
        return mParaAndroidConfig;
    }

    public static void initialize() {
        mParaAndroidConfig = new ParaAndroidConfig();
        mParaAndroidConfig.init(BaseApplication.getAppContext());
    }

    public String os = "androId";// 手机操作系统,服务器要求的I

    public String osVersion = android.os.Build.VERSION.RELEASE;// 操作系统版本号

    public String deviceId = "";// 设备ID

    public String ver = "";// 客户端版本号

    public String userAgent = (MIUIUtils.isMIUI() ? "xiaomi_" : "") + android.os.Build.MODEL;// 手机型号

    public String apn = "";// 网络类型

    @SignIgnoreAttr
    public String imei = "";
    @SignIgnoreAttr
    public String mac = "";
    @SignIgnoreAttr
    public String imsi = "";
    @SignIgnoreAttr
    public String station = "";
    @SignIgnoreAttr
    public String routerMac = "";

    public Double lat;
    public Double lon;

    public String partner = "";// 推广渠道标志

    public String sub = "";// 推广子渠道标志
    @SignIgnore
    public String Token = "1234567890";


    public void init(Context context) {
        this.ver = getVersion(context);
        setDeviceId(context);
        setLocalMacAddress(context);
        setNetwork(context);
        setPartner(context);
    }

    private void setPartner(Context context) {
        this.partner = getPartnerString(context, context.getResources().getString(R.string.url_partner_name), "web");
        this.sub = getPartnerString(context, context.getResources().getString(R.string.url_sub_name), "");
    }

    public String getVersion(Context context) {
        try {
            this.ver = (context).getPackageManager().getPackageInfo(
                    (context).getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        return this.ver;
    }


    private void setNetwork(Context context) {
        // 检测网络
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State state_wifi = null;
        NetworkInfo.State state_gprs = null;
        try {
            state_wifi = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
        } catch (Exception e) {
        }
        try {
            state_gprs = connManager.getNetworkInfo(
                    ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
        } catch (Exception e) {
        }
        if (null != state_wifi && NetworkInfo.State.CONNECTED == state_wifi) { // 判断是否正在使用WIFI网络
            this.apn = APN_WIFI;
        } else if (null != state_gprs && NetworkInfo.State.CONNECTED == state_gprs) { // 判断是否正在使用GPRS网络
            this.apn = APN_GPRS;
        } else {
            this.apn = APN_OTHER;
        }
    }

    private void setDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        try {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            this.deviceId = MD5.toMD5(deviceUuid.toString());
            this.imei = tm.getDeviceId();
            this.imsi = tm.getSubscriberId();
        } catch (Exception e) {
        }
        try {
            CellLocation cellLocation = tm.getCellLocation();
            int type = tm.getPhoneType();
            if (type == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation location = (CdmaCellLocation) cellLocation;
                int lac = location.getNetworkId();
                int cellid = location.getBaseStationId();
                this.station = "" + lac + "," + cellid;
            } else if (type == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation location = (GsmCellLocation) cellLocation;
                int lac = location.getLac();
                int cellid = location.getCid();
                this.station = "" + lac + "," + cellid;
            }
        } catch (Exception e) {
        }
    }


    private void setLocalMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (wifi.isWifiEnabled()) {
                this.routerMac = info.getBSSID(); //获取被连接网络的mac地址
                this.mac = info.getMacAddress();// 获得本机的MAC地址
            }
        } catch (Exception e) {
        }
    }


    private String getPartnerString(Context context, String name, String dft) {
        String partner = dft;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            partner = appInfo.metaData.getString(name);
        } catch (Exception e) {
        }
        return partner;
    }
}