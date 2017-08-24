package com.olive.ui.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.biz.base.BaseFragment;
import com.biz.util.Utils;

/**
 * Created by TingYu Zhu on 2017/8/24.
 */

public class CustomerServicesFragment extends BaseFragment{

    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        webView = new WebView(getContext());
        webView.setPadding(Utils.dip2px(16),Utils.dip2px(16),Utils.dip2px(16),Utils.dip2px(16));
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return webView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);

    }
}

