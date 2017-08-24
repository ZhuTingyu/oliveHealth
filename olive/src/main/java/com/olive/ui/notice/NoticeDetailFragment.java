package com.olive.ui.notice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.TimeUtil;
import com.olive.R;
import com.olive.model.entity.NoticeDetailEntity;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class NoticeDetailFragment extends BaseFragment {

    private NoticeDetailViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new NoticeDetailViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_detail_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setTitle(getString(R.string.text_notice_details));
        initData();
    }

    private void initData() {
        setProgressVisible(true);
        viewModel.getNoticeDetail(noticeDetailEntity -> {
            initView(noticeDetailEntity);
            setProgressVisible(false);
        });
    }

    private void initView(NoticeDetailEntity entity) {
        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        WebView webView = findViewById(R.id.webView);

        title.setText(entity.title);
        date.setText(TimeUtil.format(entity.publishDate,TimeUtil.FORMAT_YYYYMMDD));

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, entity.content, "text/html", "utf-8", null);
    }
}
