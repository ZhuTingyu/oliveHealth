package com.warehourse.app.ui.register;


/**
 * Created by johnzheng on 3/18/16.
 */


import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.RxUtil;
import com.biz.widget.CustomCountDownTimer;
import com.biz.widget.MaterialEditText;
import com.warehourse.app.R;
import com.warehourse.app.ui.web.WebViewActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class Register1Fragment extends BaseFragment implements FragmentBackHelper {

    MaterialEditText editUsername;
    MaterialEditText editCode;
    TextView btnCode;
    View line;
    MaterialEditText editPassword;
    MaterialEditText editPromo;
    TextView textProtocol;
    Button btnOk;

    CustomCountDownTimer countDownTimer;
    private RegisterViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new RegisterViewModel(this);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_1_layout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editUsername =  findViewById(R.id.edit_username);
        editCode = findViewById(R.id.edit_code);
        btnCode =  findViewById(R.id.btn_code);
        line =  findViewById(R.id.line);
        editPassword = findViewById(R.id.edit_password);
        editPromo = findViewById(R.id.edit_promo);
        textProtocol =  findViewById(R.id.text_protocol);
        btnOk = findViewById(R.id.btn_ok);


        countDownTimer = new CustomCountDownTimer(getActivity(),
                btnCode, R.string.text_send_code, R.string.btn_resend_count, 60000, 1000);
        textProtocol.setText(Html.fromHtml("点击注册即表示同意 <font color='#004ba3' >《隔壁仓库服务协议》</font>"));
        bindUi(RxUtil.click(textProtocol), o->{
            WebViewActivity.startWebViewAgreement(getActivity());
        });
        bindUi(RxUtil.click(btnOk), o -> {
            dismissKeyboard();
            setProgressVisible(true);
            viewModel.register(b -> {
                setProgressVisible(false);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                        .add(R.id.frame_holder, new Register2Fragment(), Register2Fragment.class.getName())
                        .commitAllowingStateLoss();
            });

        });
        bindUi(RxUtil.click(btnCode), o -> {
            dismissKeyboard();
            setProgressVisible(true);
            viewModel.sendCode(b -> {
                setProgressVisible(false);
                countDownTimer.start();
            });
        });
        bindUi(RxUtil.textChanges(editUsername), viewModel.setPhone());
        bindUi(RxUtil.textChanges(editCode), viewModel.setCode());
        bindUi(RxUtil.textChanges(editPassword), viewModel.setPwd());
        bindUi(RxUtil.textChanges(editPromo), viewModel.setRecommendCode());
        bindData(viewModel.getDataValid(), RxUtil.enabled(btnOk));
        btnOk.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_register);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public boolean isChanged(){
        return true;
    }

    @Override
    public boolean onBackPressed() {
//        if (isChanged())
//            return true;
        return false;
    }
}

