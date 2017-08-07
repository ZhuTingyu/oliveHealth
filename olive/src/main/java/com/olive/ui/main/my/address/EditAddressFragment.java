package com.olive.ui.main.my.address;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.olive.R;
import com.olive.model.entity.AddressEntity;

import cn.qqtheme.framework.picker.AddressPicker;


/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class EditAddressFragment extends BaseFragment {

    private EditText receiver;
    private EditText phone;
    private EditText area;
    private EditText address;
    private EditText address1;
    private TextView btnOk;

    private AddressEntity addressEntity;

    private EditAddressViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new EditAddressViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_address_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addressEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_TYPE);
        if(addressEntity == null){
            setTitle(getString(R.string.title_add_new_address));
        }else {
            setTitle(getString(R.string.title_edit_address));
        }
        initView();

    }

    private void initView(){

        receiver = findViewById(R.id.tv_receiver);
        phone = findViewById(R.id.tv_phone);
        area = findViewById(R.id.tv_area);
        //address = findViewById(R.id.tv_address);
        address1 = findViewById(R.id.tv_address1);
        btnOk = findViewById(R.id.btn_ok);
        area.setFocusableInTouchMode(false);
        address.setFocusableInTouchMode(false);

        bindUi(RxUtil.textChanges(receiver), viewModel.setConsignee());
        bindUi(RxUtil.textChanges(phone), viewModel.setMobile());
        bindUi(RxUtil.textChanges(area), viewModel.setArea());
        bindUi(RxUtil.textChanges(address1), viewModel.setDetailAddress());

        if(addressEntity != null){
            receiver.setText(addressEntity.consignee);
            phone.setText(addressEntity.mobile);
            area.setText(addressEntity.provinceText+" "+addressEntity.cityText+" "+addressEntity.districtText);
            address.setText("玉林街道");
            address1.setText(addressEntity.detailAddress);
        }


    }
}
