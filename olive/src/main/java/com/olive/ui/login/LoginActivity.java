package com.olive.ui.login;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.olive.R;
import com.olive.ui.search.SearchActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Title: LoginActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  16:30
 *
 * @author johnzheng
 * @version 1.0
 */

public class LoginActivity extends BaseActivity {

    public static final void startLogin(Activity activity) {
        IntentBuilder.Builder()
                .setClass(activity, LoginActivity.class)
                .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .startActivity();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        mToolbar.setTitle(getString(R.string.text_login));
    }
}
