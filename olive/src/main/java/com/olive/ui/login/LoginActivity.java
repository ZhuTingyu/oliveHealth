package com.olive.ui.login;

import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.olive.R;
import com.olive.model.UserModel;
import com.olive.ui.main.MainActivity;
import com.olive.ui.main.my.password.FindPassword1Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;



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

    private LoginViewModel viewModel;
    private EditText etUserName;
    private EditText etPassword;
    private TextView tvForgetPassword;

    public static final int TYPE_LOGIN_INVALID = 0;

    private int type;


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
        viewModel = new LoginViewModel(this);
        initViewModel(viewModel);
        type = getIntent().getIntExtra(IntentBuilder.KEY_TYPE,1);

        initView();

        if(type == TYPE_LOGIN_INVALID){
            error(getString(R.string.message_login_invalid));
        }else {
            if(UserModel.getInstance().isLogin()){
                MainActivity.startMainWithAnim(getActivity(), 0);
            }
        }
        etUserName.setText(UserModel.getInstance().hisMobile);
    }

    private void initView() {
        etUserName = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        tvForgetPassword = (TextView) findViewById(R.id.text_forget_password);


        bindUi(RxUtil.textChanges(etUserName), viewModel.setUserName());
        bindUi(RxUtil.textChanges(etPassword), viewModel.setPassword());

        tvForgetPassword.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(this, FindPassword1Fragment.class, true);
        });

        findViewById(R.id.btn_ok).setOnClickListener(v -> {
            viewModel.isCanLogin(aBoolean -> {
                if(aBoolean){
                    setProgressVisible(true);
                    viewModel.login(userEntity -> {
                        setProgressVisible(false);
                        MainActivity.startMainWithAnim(getActivity(), 0);
                    });
                }else {
                    error(getString(R.string.message_input_username_or_password));
                }
            });

        });

    }
}
