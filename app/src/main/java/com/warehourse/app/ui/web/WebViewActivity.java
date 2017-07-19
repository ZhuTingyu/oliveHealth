package com.warehourse.app.ui.web;

import com.biz.base.BaseActivity;
import com.biz.http.UrlSinger;
import com.biz.share.ShareHelper;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.warehourse.app.BuildConfig;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.ShareEntity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

/**
 * Title: WebviewActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:18/05/2017  13:14
 *
 * @author johnzheng
 * @version 1.0
 */
public class WebViewActivity extends BaseActivity {


    private final static String AGREEMENT = "file:///android_asset/agreement.html";
    private final static String SHARE_KEY = "target";
    private final static String SHARE_VALUE = "popupShare";

    private ShareHelper mShareHelper;

    public static void startWebView(Activity context, String url) {
        IntentBuilder.Builder().setData(Uri.parse(url))
                .setClass(context, WebViewActivity.class)
                .startActivity();
    }

    public static boolean startUri(Context context, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                Uri uri = Uri.parse(url);
                boolean isLogin = uri.getBooleanQueryParameter(IntentBuilder.KEY_LOGIN, false);
                boolean isGlobal = uri.getBooleanQueryParameter(IntentBuilder.KEY_GLOBAL, false);
                if (uri.getPath().contains("/search.do")
                        || uri.getPath().contains("/detail.do")){

                }  else if (isLogin  && !UserModel.getInstance().isLogin()) {
                    UserModel.getInstance().createContactDialog(context);
                    return true;
                }
                if (isGlobal) {
                    url = UrlSinger.builder()
                            .url(url)
                            .userId(UserModel.getInstance().getUserId())
                            .toUrl();
                    uri = Uri.parse(url);
                }
                IntentBuilder.Builder(IntentBuilder.ACTION_VIEW)
                        .setData(uri)
                        .startActivity((Activity) context);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public static void startWebViewAgreement(Activity context) {
        IntentBuilder.Builder().setData(Uri.parse(AGREEMENT))
                .setClass(context, WebViewActivity.class)
                .startActivity();
    }

    protected ProgressBar mPageLoadingProgressBar;
    protected WebView mWebView;
    protected String url = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_with_toolbar_layout);
        FrameLayout frameLayout = getView(R.id.frame_holder);
        AppBarLayout appBarLayout = getView(R.id.appbar);
        appBarLayout.addView(mPageLoadingProgressBar =
                (ProgressBar) getLayoutInflater().inflate(R.layout.progressbar, appBarLayout, false), 1);
        frameLayout.addView(mWebView = new WebView(WareApplication.getAppContext()));
        if(DrawableHelper.LOLLIPOP_GE)
            mWebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);

        Uri uri = getIntent().getData();
        if (uri != null) {
            url = uri.toString();
        }
        if (!TextUtils.isEmpty(url)) {
            if (uri.getPath().contains("share.do") &&
                    SystemModel.getInstance().getShare() != null) {
                mWebView.loadUrl(SystemModel.getInstance().getShare().recommendUrl);
            } else {
                mWebView.loadUrl(url);
            }
        }
        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }

        mWebView.setWebViewClient(new MyWebViewClient());

        mWebView.setWebChromeClient(new MyChromeClient());
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        setResult(RESULT_OK);
        finish();
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {

            view.setEnabled(false);
            view.postDelayed(() -> {
                view.setEnabled(true);
            }, 1000);
            if (url.contains(SHARE_KEY) && url.contains(SHARE_VALUE)) {
                if (SystemModel.getInstance().getShare() != null) {
                    ShareEntity entity = SystemModel.getInstance().getShare();
                    mShareHelper = ShareHelper.shareDialog(getActivity(),
                            entity.getIcon(), entity.getShareUrl(),
                            entity.getTitle(), entity.getContent());

                }
                view.stopLoading();
                return true;
            }
            if (startUri(getActivity(), url)) {
                view.stopLoading();
                return true;
            } else {
                view.loadUrl(url);
            }
            return false;
        }
    }

    class MyChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(android.webkit.WebView webView, int i) {
            super.onProgressChanged(webView, i);
            mPageLoadingProgressBar.setProgress(i);
            if (mPageLoadingProgressBar != null && i != 100) {
                mPageLoadingProgressBar.setVisibility(View.VISIBLE);
            } else if (mPageLoadingProgressBar != null) {
                mPageLoadingProgressBar.setVisibility(View.GONE);
            }
        }


        @Override
        public void onReceivedTitle(android.webkit.WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                mToolbar.setTitle(title);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mShareHelper != null) {
            mShareHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
