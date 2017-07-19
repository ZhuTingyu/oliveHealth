package com.warehourse.app.ui.message;

import com.biz.base.FragmentParentActivity;

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


public class MessageActivity extends FragmentParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(KEY_FRAGMENT, MessagesFragment.class);
        super.onCreate(savedInstanceState);
    }
}
