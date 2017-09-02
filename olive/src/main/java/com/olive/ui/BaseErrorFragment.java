package com.olive.ui;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.olive.model.UserModel;
import com.olive.ui.login.LoginActivity;

/**
 * Created by TingYu Zhu on 2017/9/2.
 */

public class BaseErrorFragment extends BaseFragment {

    @Override
    public void error(int code, String error) {
        if(code == 1004){
            UserModel.getInstance().loginOut();
            IntentBuilder.Builder(getActivity(), LoginActivity.class)
                    .putExtra(IntentBuilder.KEY_TYPE, LoginActivity.TYPE_LOGIN_INVALID)
                    .startActivity();
            getActivity().finish();
        }
    }

}
