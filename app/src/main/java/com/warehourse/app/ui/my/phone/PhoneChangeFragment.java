package com.warehourse.app.ui.my.phone;

import com.biz.util.DialogUtil;
import com.biz.util.RxUtil;
import com.biz.widget.CustomCountDownTimer;
import com.warehourse.app.R;
import com.warehourse.app.ui.password.BasePasswordFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class PhoneChangeFragment extends BasePasswordFragment {


    private ChangePhoneViewModel mViewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new ChangePhoneViewModel(this);
        initViewModel(mViewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameText.setHint(getString(R.string.text_hint__new_username));
        codeText.setHint(getString(R.string.text_hint_code));
        line.setVisibility(View.VISIBLE);
        btnCode.setVisibility(View.VISIBLE);
        btnOk.setText(R.string.text_change_confirm);

        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);
        bindUi(RxUtil.click(btnCode), o -> {
            setProgressVisible(true);
            mViewModel.sendCode(b -> {
                countDownTimer.start();
                setProgressVisible(false);
            });
        });
        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            mViewModel.smsValidate(b -> {
                setProgressVisible(false);
                DialogUtil.createDialogViewWithFinish(getActivity(), getString(R.string.text_change_phone_success));
            });
        });
        bindData(mViewModel.getDatValid(), RxUtil.enabled(btnOk));
        bindUi(RxUtil.textChanges(usernameText), mViewModel.setPhone());
        bindUi(RxUtil.textChanges(codeText), mViewModel.setCode());
    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_change_phone);
    }
}