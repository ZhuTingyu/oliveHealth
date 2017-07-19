package com.warehourse.app.ui.password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.biz.util.DialogUtil;
import com.biz.util.Lists;
import com.biz.util.OperatorEditTextInput;
import com.biz.util.RxUtil;
import com.warehourse.app.R;

import rx.Observable;

/**
 * Title: ChangeNeedOldFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:17/05/2017  11:13
 *
 * @author johnzheng
 * @version 1.0
 */

public class ChangeNeedOldFragment extends BaseChangePasswordFragment {
    private ChangePasswordViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ChangePasswordViewModel(this);
        initViewModel(viewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindUi(RxUtil.textChanges(oldPassword), viewModel.setOldPwd());
        bindUi(RxUtil.textChanges(newPassword1), viewModel.setPwd1());
        bindUi(RxUtil.textChanges(newPassword2), viewModel.setPwd2());

        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            viewModel.change(b -> {
                setProgressVisible(false);
                DialogUtil.createDialogViewWithFinish(getContext(), getString(R.string.dialog_change_password_success));
            });
        });
        btnOk.setEnabled(false);
        bindUi(OperatorEditTextInput.create(Observable.just(Lists.newArrayList(oldPassword, newPassword1, newPassword2))), strings -> {
            if (strings != null && strings.length == 3 && !TextUtils.isEmpty(strings[0]) && !TextUtils.isEmpty(strings[1]) && !TextUtils.isEmpty(strings[2])) {
                btnOk.setEnabled(true);
            } else {
                btnOk.setEnabled(false);
            }
        });
    }
}
