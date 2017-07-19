package com.biz.share.qq;

import com.biz.application.BaseApplication;
import com.biz.http.R;
import com.biz.share.IBSendMessage;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SendQQ {
    public static String APP_ID = "";
    private Context context;
    private Tencent mTencent;
    private QzoneShare mQzoneShare;
    private IUiListener listener;

    public SendQQ(Context context) {
        this.context = context;
        APP_ID = getAppId();
    }

    public static String getAppId() {
        try {
            return new String(Base64.decode(BaseApplication.getAppContext()
                    .getString(R.string.qq_appid), Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.dialog_share_not_installed);
        builder.setNeutralButton(R.string.btn_confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void sendQQ(String url, String title, String txt, String imagelogourl, IBSendMessage myIBSendMessage) {
        mTencent = Tencent.createInstance(APP_ID, context);
        final Bundle params = new Bundle();
        // params.putString(QQShare.SHARE_TO_QQ_SITE, "aaaaaaaaaaaaaa"); //位置。
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title); // 分享的标题, 最长30个字符。
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url); // 这条分享消息被好友点击后的跳转URL。
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, txt); // 分享的消息摘要，最长40个字。
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imagelogourl);// 分享图片的URL或者本地路径
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name)); // 手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);// 分享的类型。图文分享(普通分享)填Tencent.SHARE_TO_QQ_TYPE_DEFAULT
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,
                QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        // QZone按钮且不自动打开分享到QZone的对话框）：
        // QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN
        doShareToQQ(params, myIBSendMessage);
    }

    private void doShareToQQ(final Bundle params, final IBSendMessage myIBSendMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mTencent.shareToQQ((Activity) context, params,
                            listener=new IUiListener() {
                                @Override
                                public void onCancel() {
                                    if(myIBSendMessage!=null)
                                        myIBSendMessage.onComplete(false);
                                }

                                @Override
                                public void onComplete(Object response) {
                                    if (myIBSendMessage != null)
                                        myIBSendMessage.onComplete(true);
                                }

                                @Override
                                public void onError(UiError e) {
                                    if(myIBSendMessage!=null)
                                        myIBSendMessage.onComplete(false);
                                }

                            });
                } catch (Exception e) {
                    if(myIBSendMessage!=null)
                        myIBSendMessage.onComplete(false);
                }
            }
        }).start();
    }

    public void shareToQQzone(String url, String title, String txt, String bitmapurl,IBSendMessage myIBSendMessage) {
        mTencent = Tencent.createInstance(APP_ID, context);
        mQzoneShare = new QzoneShare(context, mTencent.getQQToken());
        final Bundle params = new Bundle();
        // 设置分享类型：图文并茂加链接。其他类型见帮助文档
        int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        // 标题
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        // 内容
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, txt);
        if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
            // app分享不支持传目标链接
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        }
        // // 支持传多个imageUrl，在这里我测试只放一张图片，最多可以放9张
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (!TextUtils.isEmpty(bitmapurl)) {
            imageUrls.add(bitmapurl);
        }
        String imageUrl = "XXX";
        params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        doShareToQzone(params,myIBSendMessage);
    }

    private void doShareToQzone(final Bundle params,final IBSendMessage ibSendMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mQzoneShare.shareToQzone((Activity) context, params,
                            listener=new IUiListener() {

                                @Override
                                public void onCancel() {
                                    if(ibSendMessage!=null)
                                        ibSendMessage.onComplete(false);
                                }

                                @Override
                                public void onError(UiError e) {
                                    if(ibSendMessage!=null)
                                        ibSendMessage.onComplete(false);
                                }

                                @Override
                                public void onComplete(Object response) {
                                    if(ibSendMessage!=null)
                                        ibSendMessage.onComplete(true);
                                }

                            });
                } catch (Exception e) {
                    if(ibSendMessage!=null)
                        ibSendMessage.onComplete(false);
                }
            }
        }).start();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null)
        {
            mTencent.onActivityResultData(requestCode,resultCode,data,listener);
        }
    }
}
