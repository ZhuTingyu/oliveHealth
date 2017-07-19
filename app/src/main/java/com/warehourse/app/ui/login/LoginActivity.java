package com.warehourse.app.ui.login;


import com.biz.base.BaseActivity;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.ValidUtil;
import com.warehourse.app.R;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.ui.password.FindPasswordFragment;
import com.warehourse.app.ui.register.Register1Fragment;
import com.warehourse.app.ui.register.RegisterDoneFragment;
import com.warehourse.app.ui.register.RegisterStatusFragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.greenrobot.event.EventBus;


public class LoginActivity extends BaseActivity {


    private TextView textForgetPassword;
    private TextView textRegister;
    private EditText mEmailView;
    private EditText mPasswordView;
    private LoginViewModel viewModel;
    public static final void goLogin(Context context) {
        IntentBuilder.Builder().setClass(context, LoginActivity.class)
                .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .startActivity();
    }

    public static final void goLogin(View view) {
        IntentBuilder.Builder().setClass(view.getContext(), LoginActivity.class)
                .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                .overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                .startActivity();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN_LOGIN_OUT, false)){
            IntentBuilder.Builder().setClass(this, MainActivity.class).finish(this).startActivity(this);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_toolbar_layout);
        viewModel=new LoginViewModel(this);
        initViewModel(viewModel);
        getLayoutInflater().inflate(R.layout.activity_login_layout, getView(R.id.frame_holder));
        mToolbar.setTitle(R.string.text_login);
        mToolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);

        mEmailView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((TextView textView, int id, KeyEvent keyEvent)-> {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
        });

        Button btnOK = (Button) findViewById(R.id.btn_ok);
        bindUi(RxUtil.click(btnOK), o -> {
            attemptLogin();
        });
        mEmailView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        bindData(viewModel.getLoginMobile(),RxUtil.text(mEmailView));
        bindUi(RxUtil.textChanges(mEmailView), viewModel.setAccount());
        bindUi(RxUtil.textChanges(mPasswordView), viewModel.setPassword());
        viewModel.request();
    }



    private void attemptLogin() {
        dismissKeyboard();
        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isAccountValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_account));
            focusView = mEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            if (focusView == null)
                focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            if (focusView == null)
                focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            setProgressVisible(true);
            viewModel.login(() -> {
                setProgressVisible(false);
                EventBus.getDefault().post(new LoginEvent());
                if (UserModel.getInstance().isReview()) {
                    IntentBuilder.Builder().startParentActivity(getActivity(), RegisterDoneFragment.class);
                } else if (UserModel.getInstance().isEditDetail()) {
                    IntentBuilder.Builder().startParentActivity(getActivity(), RegisterStatusFragment.class);
                }else{
                    if (getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false)){
                        finish();
                        return;
                    }
                    IntentBuilder.Builder().setClass(this, MainActivity.class).finish(this).startActivity(this);
                }
                finish();
            });

        }
    }


    private boolean isAccountValid(String email) {
        return ValidUtil.phoneNumberValid(email);
    }

    private boolean isPasswordValid(String password) {
        return ValidUtil.pwdValid(password);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_forget_password:
                IntentBuilder.Builder().startParentActivity(getActivity(), FindPasswordFragment.class);
                break;
            case R.id.text_register:
                IntentBuilder.Builder().startParentActivity(getActivity(), Register1Fragment.class);
                break;
            default:
                break;
        }
    }


}

