package com.warehourse.app.ui.coupon;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Utils;
import com.warehourse.app.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PrivateFragment extends BaseFragment {

    boolean isMoney;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isMoney =  getActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);

        TextView textView= new TextView(getContext());
        textView.setText(isMoney?R.string.text_money_info:R.string.text_private_hint);
        int padding = Utils.dip2px(16);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setTitle(isMoney? R.string.text_use_info:R.string.text_settings_private);

    }
}
