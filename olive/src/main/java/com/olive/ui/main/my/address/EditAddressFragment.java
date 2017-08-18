package com.olive.ui.main.my.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.RxUtil;
import com.biz.util.ToastUtils;
import com.biz.widget.picker.WheelView;
import com.olive.R;
import com.olive.model.entity.AddressEntity;
import com.olive.widget.AddressPicker;


/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class EditAddressFragment extends BaseFragment {

    private EditText receiver;
    private EditText phone;
    private EditText area;
    private EditText address1;
    private TextView btnOk;

    private AddressEntity addressEntity;

    private EditAddressViewModel viewModel;

    private AddressPicker picker;


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
        addressEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
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
        btnOk = findViewById(R.id.btn_ok);
        address1 = findViewById(R.id.tv_address1);
        area.setFocusableInTouchMode(false);

        bindUi(RxUtil.textChanges(receiver), viewModel.setConsignee());
        bindUi(RxUtil.textChanges(phone), viewModel.setMobile());
        bindUi(RxUtil.textChanges(area), viewModel.setArea());
        bindUi(RxUtil.textChanges(address1), viewModel.setDetailAddress());

        area.setOnClickListener(v -> {
            initPickerView();
        });

        if(addressEntity != null){
            receiver.setText(addressEntity.consignee);
            phone.setText(addressEntity.mobile);
            area.setText(addressEntity.provinceText+" "+addressEntity.cityText+" "+addressEntity.districtText);
            address1.setText(addressEntity.detailAddress);
            viewModel.setEditAddresEntity(addressEntity);
        }

        btnOk.setOnClickListener(v -> {
            if(addressEntity != null){
                viewModel.updateAddress(addressEntity1 -> {
                    ToastUtils.showLong(getActivity(), getString(R.string.message_modify_success));
                    Intent intent = new Intent();
                    intent.putExtra(IntentBuilder.KEY_BOOLEAN, true);
                    getActivity().setIntent(intent);
                    getActivity().finish();
                });
            }else {
                viewModel.checkAddressInfo(s -> {
                    if(getString(R.string.message_info_is_valid).equals(s)){
                        viewModel.addAddress(addressEntity1 -> {
                            ToastUtils.showLong(getActivity(),getString(R.string.message_add_address_success));
                            getActivity().finish();
                        });
                    }else {
                        error(s);
                    }
                });
            }
        });
    }

    protected void initPickerView(){

        viewModel.cleanAreaData();

        picker = new AddressPicker(getActivity());

        viewModel.getCityList(EditAddressViewModel.TYPE_PROVINCE , stringList -> {
            picker.setProvinceItems(stringList,viewModel.positionProvince);
        });

        picker.setOnWheelViewsListener(new AddressPicker.OnWheelViewsListener() {

            @Override
            public void onProvince(boolean isUserScroll, int selectedIndex, String item) {
                viewModel.positionProvince = selectedIndex;
                viewModel.positionCounty = 0;
                viewModel.setCode(viewModel.provinceList.get(selectedIndex).code);
                viewModel.getCityList(EditAddressViewModel.TYPE_CITY, stringList -> {
                    picker.cityView.setItems(stringList, isUserScroll ? 0 : viewModel.positionCity);
                });
            }

            @Override
            public void onCity(boolean isUserScroll, int selectedIndex, String item) {
                viewModel.positionCity = selectedIndex;
                viewModel.setCode(viewModel.cityList.get(selectedIndex).code);
                viewModel.getCityList(EditAddressViewModel.TYPE_COUNTY, stringList -> {
                    picker.countyView.setItems(stringList, isUserScroll ? 0 : viewModel.positionCounty);
                });
            }

            @Override
            public void onCounty(boolean isUserScroll, int selectedIndex, String item) {
                viewModel.positionCounty = selectedIndex;
            }
        });

        picker.setOnAddressPickListener(() -> {
            area.setText(viewModel.getAreaString());
        });

        picker.show();
    }
}
