package com.warehourse.app.ui.password;

import com.biz.base.BaseFragment;
import com.biz.widget.CustomCountDownTimer;
import com.biz.widget.MaterialEditText;
import com.warehourse.app.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BasePasswordFragment extends BaseFragment {

    protected
    MaterialEditText usernameText;
    protected
    MaterialEditText codeText;
    protected
    View line;

    protected
    Button btnOk;

    protected
    TextView btnCode;
    protected CustomCountDownTimer countDownTimer;
    @Nullable
    RelativeLayout codeLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_password_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameText = findViewById(R.id.username);
        codeLayout = findViewById(R.id.code_layout);
        codeText =  findViewById(R.id.password);
        btnCode = findViewById(R.id.btn_code);
        line = findViewById(R.id.line);
        btnOk =  findViewById(R.id.btn_ok);


    }



    @Override
    public void onStart() {
        super.onStart();
        usernameText.setFloatingLabelText(usernameText.getHint());
        codeText.setFloatingLabelText(codeText.getHint());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                break;
            case R.id.btn_ok:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                        .add(R.id.frame_holder, new BaseChangePasswordFragment(), BaseChangePasswordFragment.class.getName())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();

                break;
        }
    }
}

