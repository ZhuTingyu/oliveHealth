package com.warehourse.app.ui.password;

/**
 * Created by johnzheng on 3/17/16.
 */


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.biz.util.RxUtil;
import com.warehourse.app.R;


public class ChangeNeedCodeFragment extends BaseChangePasswordFragment {
    private ForgotPasswordViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ForgotPasswordViewModel(this);
        initViewModel(viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_reset_password);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundResource(R.color.color_background);
        oldPassword.setVisibility(View.GONE);
        btnOk.setText(R.string.text_confirm_change);

        bindUi(RxUtil.textChanges(newPassword1), viewModel.setPwd1());
        bindUi(RxUtil.textChanges(newPassword2), viewModel.setPwd2());
        bindData(viewModel.getDatValid(), RxUtil.enabled(btnOk));
        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            viewModel.forgot(b -> {
                setProgressVisible(false);
                getActivity().finish();
            });
        });
    }

}

