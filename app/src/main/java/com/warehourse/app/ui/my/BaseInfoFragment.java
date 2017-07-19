package com.warehourse.app.ui.my;


/**
 * Created by johnzheng on 3/19/16.
 */


import com.biz.base.BaseFragment;
import com.warehourse.app.R;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class BaseInfoFragment extends BaseFragment {

    protected TextView title;
    protected TextView titleLine2;
    protected Button btn2;
    protected ImageView icon;
    protected ImageView icon1;
    protected TextView titleState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_info_layout, container, false);
        getActivity().getWindow().setBackgroundDrawableResource(R.color.white);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        icon =  findViewById(R.id.icon);
        title =  findViewById(R.id.title);
        titleLine2 =  findViewById(R.id.title_line_2);
        btn2 =  findViewById(R.id.btn_2);
        titleState =  findViewById(R.id.title_state);
        icon1 =  findViewById(R.id.icon1);
        btn2.setBackgroundResource(R.drawable.btn_red_selector);
        btn2.setTextColor(getColors(R.color.base_color));
        btn2.setTypeface(Typeface.DEFAULT);
        TextViewCompat.setTextAppearance(title, R.style.style_text_user_1);
        TextViewCompat.setTextAppearance(titleLine2, R.style.style_text_user_2);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

