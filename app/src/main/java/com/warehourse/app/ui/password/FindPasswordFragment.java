package com.warehourse.app.ui.password;/**
 * Created by johnzheng on 3/17/16.
 */


import com.biz.util.RxUtil;
import com.biz.widget.CustomCountDownTimer;
import com.warehourse.app.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


public class FindPasswordFragment extends BasePasswordFragment {
    private FindPasswordViewModel viewModel;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new FindPasswordViewModel(this);
        initViewModel(viewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);

        usernameText.setHint(getString(R.string.text_hint_username));
        codeText.setHint(getString(R.string.text_hint_code));
        line.setVisibility(View.VISIBLE);
        btnCode.setVisibility(View.VISIBLE);
        btnOk.setText(R.string.text_find_password);
        bindUi(RxUtil.click(btnCode), o -> {
            dismissKeyboard();
            setProgressVisible(true);
            viewModel.sendCode(b -> {
                countDownTimer.start();
                setProgressVisible(false);
            });
        });
        bindUi(RxUtil.click(btnOk), o -> {
            dismissKeyboard();
            setProgressVisible(true);
            viewModel.smsValidate(b -> {
                setProgressVisible(false);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                        .add(R.id.frame_holder, new ChangeNeedCodeFragment(), ChangeNeedCodeFragment.class.getName())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            });
        });
        bindData(viewModel.getDatValid(), RxUtil.enabled(btnOk));
        bindUi(RxUtil.textChanges(usernameText), viewModel.setPhone());
        bindUi(RxUtil.textChanges(codeText), viewModel.setCode());
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_forget_password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

