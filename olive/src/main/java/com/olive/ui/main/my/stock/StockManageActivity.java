package com.olive.ui.main.my.stock;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.olive.R;
import com.olive.ui.search.SearchActivity;
import com.olive.ui.search.SearchFragment;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class StockManageActivity extends BaseActivity {


    public static final void startStock(Activity activity) {
        IntentBuilder.Builder()
                .setClass(activity, StockManageActivity.class)
                .startActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_toolbar_layout);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_holder, new StockManagerFragment(), StockManagerFragment.class.getName())
                .commitAllowingStateLoss();
    }
}
