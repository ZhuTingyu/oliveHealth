package com.olive.ui.main.my.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.olive.R;


/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class AddNewAddressFragment extends BaseFragment {

    private EditText receiver;
    private EditText phone;
    private EditText area;
    private EditText address;
    private EditText address1;
    private TextView btnOk;

    private Object addressInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_address_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressInfo = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        setTitle(getString(R.string.title_add_new_address));
        initView(view);

    }

    private void initView(View view) {

        receiver = findViewById(R.id.tv_receiver);
        phone = findViewById(R.id.tv_phone);
        area = findViewById(R.id.tv_area);
        address = findViewById(R.id.tv_address);
        address1 = findViewById(R.id.tv_address1);
        btnOk = findViewById(R.id.btn_ok);
        area.setFocusableInTouchMode(false);
        address.setFocusableInTouchMode(false);

        if(addressInfo != null){
            setTitle(getString(R.string.title_edit_address));
            receiver.setText("高小花");
            phone.setText("123456789098");
            area.setText("四川省"+" "+"成都市"+" "+"武侯区");
            address.setText("玉林街道");
            address1.setText("环球中心E1");
        }

    }
}
