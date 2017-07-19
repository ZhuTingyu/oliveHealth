package com.biz.share.weixin;

import com.biz.application.BaseApplication;
import com.biz.base.BaseActivity;
import com.biz.http.R;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class SendWX {
    public String APP_ID = "";
    public static String STATE = "EShopApp";
    private Context context;
    private IWXAPI api;

    public SendWX(BaseActivity context) {
        this.context = context;
        this.APP_ID = getAppId();
    }
    public SendWX(Context context) {
        this.context = context;
        this.APP_ID = getAppId();
    }

    public static String getAppId() {
        try {
            return new String(Base64.decode(BaseApplication.getAppContext()
                    .getString(R.string.weixin_appid), Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    public static String getAppSecret()
    {
        try {
            return new String(Base64.decode(BaseApplication.getAppContext()
                    .getString(R.string.weixin_appsecret), Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public Boolean isBinding() {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        api.registerApp(APP_ID);
        return api.isWXAppInstalled();
    }

    public void send(String webpageUrl, String title, String desc, Bitmap thumb) {
        sendWeb(false, webpageUrl, title, desc, thumb);
    }

    public void sendTimeLine(String webpageUrl, String title, String desc, Bitmap thumb) {
        sendWeb(true, webpageUrl, title, desc, thumb);
    }

    public boolean isInstalled() {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        return api.isWXAppInstalled();
    }

    public void authWeiXin() {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        if (!api.isWXAppInstalled()) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
                baseActivity.setProgressVisible(false);
            }
            createDialog();
            return;
        }
        api.registerApp(APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        STATE = "gridyApp" + System.currentTimeMillis();
        req.state = STATE;
        api.sendReq(req);
    }

    public void payWeiXin(PayReq payReq) {
        if (context == null) {
            return;
        }
        if (payReq == null) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
                baseActivity.setProgressVisible(false);
            }
            return;
        }
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        if (!api.isWXAppInstalled()) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
                baseActivity.setProgressVisible(false);
            }
            createDialog();
            return;
        }
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
                baseActivity.setProgressVisible(false);
            }
            createDialogNotPay();
            return;
        }
        api.sendReq(payReq);
    }


    private void sendWeb(boolean isTimelineCb, String webpageUrl, String title, String desc, Bitmap thumb) {
        api = WXAPIFactory.createWXAPI(context, APP_ID);
        api.registerApp(APP_ID);
        if (!api.isWXAppInstalled()) {
            if(context instanceof  BaseActivity){
                BaseActivity baseActivity=(BaseActivity)context;
                baseActivity.setProgressVisible(false);
            }
            createDialog();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        if (thumb != null) {
            msg.setThumbImage(thumb);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + System.currentTimeMillis();
        req.message = msg;
        req.scene = isTimelineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.dialog_share_not_installed);
        builder.setPositiveButton(R.string.btn_confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void createDialogNotPay() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.dialog_not_pay_weixin);
        builder.setPositiveButton(R.string.btn_confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
