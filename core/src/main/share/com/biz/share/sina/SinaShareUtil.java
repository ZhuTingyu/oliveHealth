package com.biz.share.sina;

import com.google.gson.reflect.TypeToken;

import com.biz.application.BaseApplication;
import com.biz.http.R;
import com.biz.share.IBSendMessage;
import com.biz.util.BitmapUtil;
import com.biz.util.GsonUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwei on 2016/3/29.
 */
public class SinaShareUtil implements IWeiboHandler.Response {
    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public String APP_KEY = "";

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public String REDIRECT_URL = "";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
    private Context context;
    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private AuthInfo mWeiboAuth;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;


    /**
     * 微博微博分享接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI = null;

    private IBSendMessage mIBSendMessage;

    public SinaShareUtil(Context context) {
        this.context = context;
        APP_KEY = getAppKey();
        REDIRECT_URL = getAppRedirectUrl();
    }

    public static String getAppKey() {
        try {
            return new String(Base64.decode(BaseApplication.getAppContext()
                    .getString(R.string.sina_key), Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String getAppRedirectUrl() {
        try {
            return new String(Base64.decode(BaseApplication.getAppContext()
                    .getString(R.string.sina_redirect_url), Base64.DEFAULT), "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public void sendMessage(String str, List<String> url, double lat, double lon, IBSendMessage myIBSendMessage) {
        this.mIBSendMessage = myIBSendMessage;
        // 创建微博实例
        mWeiboAuth = new AuthInfo(this.context, APP_KEY, REDIRECT_URL, SCOPE);
        mAccessToken = AccessTokenKeeper.readAccessToken(this.context);
        if (!mAccessToken.isSessionValid()) {
            mSsoHandler = new SsoHandler((Activity) this.context, mWeiboAuth);
            mSsoHandler.authorize(new AuthListener());
        }
        String urlimage = "";
        if (url == null || url.size() == 0) {

        } else {
            urlimage = url.get(0);
        }
        final android.os.Handler handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {

                if (mIBSendMessage != null) {
                    if (msg.arg1 == 1) {
                        mIBSendMessage.onComplete(true);
                    } else {
                        mIBSendMessage.onComplete(false);
                    }
                }
            }
        };
        new SendSinaPostApi(this.context, APP_KEY, mAccessToken).sendMessage(str, urlimage,
                lat, lon, new RequestListener() {

                    @Override
                    public void onWeiboException(WeiboException arg0) {
                        Message message = new Message();
                        message.arg1 = 2;
                        handler.sendMessage(message);

                    }

                    @Override
                    public void onComplete(String arg0) {
                        Message message = new Message();
                        message.arg1 = 1;
                        handler.sendMessage(message);

                    }
                });
    }

    public void send(String title, String text, String httpImageUrl, String fileImage, String url, IBSinaWeiBoAuthListener ibSinaWeiBoAuthListener, IBSendMessage myIBSendMessage) {
        this.mIBSendMessage = myIBSendMessage;
        this.mIBSinaWeiBoAuthListener = ibSinaWeiBoAuthListener;
        // 创建微博实例
        mWeiboAuth = new AuthInfo(this.context, APP_KEY, REDIRECT_URL, SCOPE);
        mAccessToken = AccessTokenKeeper.readAccessToken(this.context);
        if (!mAccessToken.isSessionValid()) {
            mSsoHandler = new SsoHandler((Activity) this.context, mWeiboAuth);
            mSsoHandler.authorize(new AuthListener());
            return;
        }
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this.context, APP_KEY);
        if (mWeiboShareAPI.isWeiboAppInstalled() && mWeiboShareAPI.getWeiboAppSupportAPI() >= 10351) {
            mWeiboShareAPI.registerApp();
            SendSinaPostApi sendSinaPostApi = new SendSinaPostApi(this.context, APP_KEY, mAccessToken);
            sendSinaPostApi.toShortUrl(url, new RequestListener() {
                @Override
                public void onComplete(String s) {
                    String textActionUrl = getShortUrl(s);
                    String txtObj = text;
                    if (!TextUtils.isEmpty(textActionUrl)) {
                        txtObj = title + "   " + textActionUrl;
                    } else {
                        txtObj = title;
                    }
                    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
                    // 用transaction唯一标识一个请求
                    request.transaction = String.valueOf(System.currentTimeMillis());
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    weiboMessage.textObject = getTextObj(txtObj);
                    try {
                        Bitmap bitmap = BitmapUtil.getCropBitmapFromFile(fileImage, 150, 150).get();
                        weiboMessage.imageObject = getImageObj(bitmap);
                    } catch (Exception e) {

                    }
                    // weiboMessage.mediaObject =getWebpageObj(title, text, textActionUrl, bitmap);
                    request.multiMessage = weiboMessage;
                    mWeiboShareAPI.sendRequest((Activity) context, request);
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    if (mIBSendMessage != null) {
                        mIBSendMessage.onComplete(false);
                    }
                }
            });

        } else {
            SendSinaPostApi sendSinaPostApi = new SendSinaPostApi(this.context, APP_KEY, mAccessToken);
            sendSinaPostApi.toShortUrl(url, new RequestListener() {
                @Override
                public void onComplete(String s) {
                    String text = getShortUrl(s);
                    if (!TextUtils.isEmpty(text)) {
                        text = title + "   " + text;
                    } else {
                        text = title;
                    }
                    sendSinaPostApi.sendMessage(text, httpImageUrl,
                            0, 0, new RequestListener() {
                                @Override
                                public void onWeiboException(WeiboException arg0) {
                                    if (mIBSendMessage != null) {
                                        mIBSendMessage.onComplete(false);
                                    }
                                }

                                @Override
                                public void onComplete(String arg0) {
                                    if (mIBSendMessage != null) {
                                        mIBSendMessage.onComplete(true);
                                    }
                                }
                            });
                }

                @Override
                public void onWeiboException(WeiboException e) {
                    if (mIBSendMessage != null) {
                        mIBSendMessage.onComplete(false);
                    }
                }
            });
        }
    }

    private String getShortUrl(String s) {
        try {
            Map<String, List<Map<String, Object>>> stringListMap =
                    GsonUtil.fromJson(s, new TypeToken<Map<String, List<Map<String, Object>>>>() {
                    }.getType());
            List<Map<String, Object>> list = stringListMap.get("urls");
            for (Map<String, Object> map : list) {
                if (map != null && map.containsKey("result")) {
                    Object oResult = map.get("result");
                    if (oResult instanceof String) {
                        if ("true".equals(oResult)) {
                            Object obj = map.get("url_short");
                            if (obj != null) return obj.toString();
                        }
                    } else if (oResult instanceof Boolean)
                        if ((boolean) oResult) {
                            Object obj = map.get("url_short");
                            if (obj != null) return obj.toString();
                        }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }


    //    /**
//     * 当 SSO 授权 Activity 退出时，该函数被调用。
//     *
//     * @see {@link Activity#onActivityResult}
//     */
//    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void onNewIntent(Intent intent) {
        if (mWeiboShareAPI != null)
            mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            if (mIBSendMessage != null) {
                mIBSendMessage.onComplete(baseResp.errCode == WBConstants.ErrorCode.ERR_OK);
            }
        }
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     * 该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                if (mIBSinaWeiBoAuthListener != null)
                    mIBSinaWeiBoAuthListener.onComplete();
            } else {
                if (mIBSendMessage != null)
                    mIBSendMessage.onComplete(false);
            }
        }

        @Override
        public void onCancel() {
            if (mIBSendMessage != null)
                mIBSendMessage.onComplete(false);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (mIBSendMessage != null)
                mIBSendMessage.onComplete(false);
        }
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj(Bitmap bitmap) {
        ImageObject imageObject = new ImageObject();
//        imageObject.identify = Utility.generateGUID();
//        imageObject.title = "1234566";
//        imageObject.description = "11111111111111111111111111111111111111111111111111111111111111111111111111111111";
        imageObject.setImageObject(bitmap);//BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
//        imageObject.actionUrl ="http://www.gridy.com";
        return imageObject;
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj(String txt) {
        TextObject textObject = new TextObject();
        textObject.text = txt;
        return textObject;
    }

    private VoiceObject getWebpageObj(String title, String txt, String url, Bitmap bitmap) {
        VoiceObject mediaObject = new VoiceObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = title;
        mediaObject.description = txt;
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;

        mediaObject.dataUrl = "www.weibo.com";
        mediaObject.dataHdUrl = "www.weibo.com";
        mediaObject.duration = 10;
        mediaObject.defaultText = "Vedio 默认文案";
        return mediaObject;
    }


    private IBSinaWeiBoAuthListener mIBSinaWeiBoAuthListener;

    public interface IBSinaWeiBoAuthListener {
        void onComplete();
    }
}
