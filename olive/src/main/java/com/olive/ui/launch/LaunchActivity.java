package com.olive.ui.launch;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.ui.login.LoginActivity;
import com.olive.ui.main.MainActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Space;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Title: LaunchActivity
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  12:20
 *
 * @author johnzheng
 * @version 1.0
 */

public class LaunchActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        hideNavigation();
        setContentView(new Space(getActivity()));

        subscription.add(Observable.just(1).delay(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(i->{startMain();}));

    }

    private void startMain() {

        if(UserModel.getInstance().isLogin()){
            MainActivity.startMainWithAnim(getActivity(), 0);
        }else {
            IntentBuilder.Builder()
                    .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .setClass(getActivity(), LoginActivity.class).startActivity(this);
        }
    }


    private void hideNavigation() {
        int flags;
        int curApiVersion = android.os.Build.VERSION.SDK_INT;
        if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

        } else {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

}
