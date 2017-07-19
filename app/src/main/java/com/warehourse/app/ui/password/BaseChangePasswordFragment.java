package com.warehourse.app.ui.password;


import com.biz.base.BaseFragment;
import com.biz.widget.MaterialEditText;
import com.warehourse.app.R;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by johnzheng on 5/24/16.
 */
public class BaseChangePasswordFragment extends BaseFragment {

    protected MaterialEditText oldPassword;
    protected MaterialEditText newPassword1;
    protected MaterialEditText newPassword2;
    protected Button btnOk;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.text_person_password);
        oldPassword = getView(R.id.old_password);
        newPassword1 = getView(R.id.new_password_1);
        newPassword2 = getView(R.id.new_password_2);
        btnOk = getView(R.id.btn_ok);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
