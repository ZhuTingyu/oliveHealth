package com.warehourse.app.ui.product;

import com.biz.base.FragmentParentActivity;
import com.biz.util.IntentBuilder;
import com.warehourse.app.ui.message.MessagesFragment;

import android.os.Bundle;

/**
 * Title: MessageActivity
 * Description: 如果来自Scheme jump  这个activity 作为Parent Activity。
 * 其他情况FragmentParentActivity 作为Parent Activity
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:27/05/2017  16:22
 *
 * @author johnzheng
 * @version 1.0
 */


public class ProductDetailActivity extends FragmentParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(KEY_FRAGMENT, ProductDetailFragment.class);
        getIntent().putExtra(KEY_TOOLBAR, false);
        getIntent().putExtra(IntentBuilder.KEY_ID, getIntent().getData().getQueryParameter("id"));
        super.onCreate(savedInstanceState);
    }
}
