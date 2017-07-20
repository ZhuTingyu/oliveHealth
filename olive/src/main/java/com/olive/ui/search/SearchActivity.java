package com.olive.ui.search;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.olive.R;
import com.olive.ui.main.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Title: SearchActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  14:00
 *
 * @author johnzheng
 * @version 1.0
 */

public class SearchActivity extends BaseActivity {


    public static final void startSearch(Activity activity) {
        IntentBuilder.Builder()
                .setClass(activity, SearchActivity.class)
                .startActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_toolbar_layout);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_holder, new SearchFragment(), SearchFragment.class.getName())
                .commitAllowingStateLoss();
    }
}
