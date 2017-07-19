package com.warehourse.app.ui.my;


/**
 * Created by johnzheng on 3/18/16.
 */


import com.biz.base.BaseFragment;
import com.biz.util.DrawableHelper;
import com.biz.util.RxUtil;
import com.biz.widget.MaterialEditText;
import com.biz.widget.picker.AddressPicker;
import com.biz.widget.picker.ProvinceEntity;
import com.warehourse.app.R;
import com.warehourse.app.ui.base.BaseProvinceViewModel;
import com.warehourse.app.ui.my.address.AddressPickPagerFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


public class BaseAddressFragment extends BaseFragment {

    protected AddressPicker picker;
    protected
    MaterialEditText editUsername;
    protected
    MaterialEditText editType;
    protected
    MaterialEditText editArea;
    protected
    MaterialEditText editDetail;
    protected
    MaterialEditText editReason;
    protected
    Button btnOk;

    protected BaseProvinceViewModel viewModel;
    protected MaterialEditText editIdentity;
    protected AddressPickPagerFragment mAddressPickPagerFragment;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_2_layout, container, false);
        view.setBackgroundColor(Color.WHITE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editUsername = findViewById(R.id.edit_username);
        editIdentity = findViewById(R.id.edit_identity);
        editType = findViewById(R.id.edit_type);
        editArea = findViewById(R.id.edit_area);
        editDetail = findViewById(R.id.edit_detail);
        editReason = findViewById(R.id.edit_reason);
        btnOk = findViewById(R.id.btn_ok);
        editType.setFocusableInTouchMode(false);
        editArea.setFocusableInTouchMode(false);
        editType.setCompoundDrawables(null, null,
                DrawableHelper.getDrawableWithBounds(getContext(), R.drawable.ic_arrow_right_gray),
                null
        );
        editArea.setCompoundDrawables(null, null,
                DrawableHelper.getDrawableWithBounds(getContext(), R.drawable.ic_arrow_right_gray),
                null
        );

        mAddressPickPagerFragment = new AddressPickPagerFragment();
        mAddressPickPagerFragment.setViewModel(viewModel);
        mAddressPickPagerFragment.setEditText(editArea);
        bindUi(RxUtil.click(editArea), o -> {
            dismissKeyboard();
            setProgressVisible(true);
            viewModel.requestProvince(listProvince -> {
                setProgressVisible(false);
                if (!mAddressPickPagerFragment.isAdded())
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.alpha_in, 0)
                            .add(android.R.id.content,
                                    mAddressPickPagerFragment,
                                    AddressPickPagerFragment.class.getName())
                            .commitAllowingStateLoss();
                else getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.alpha_in, 0)
                        .show(mAddressPickPagerFragment).commitAllowingStateLoss();
            }, throwable -> {
                setProgressVisible(false);
                error(throwable.getMessage());
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void createSheet(List<ProvinceEntity> list) {
        picker = new AddressPicker(getActivity(), list);
        picker.setSelectedItem(viewModel.getProvince(),
                viewModel.getCity(),
                viewModel.getDistrict());
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(String province, String city, String county) {
                editArea.setText(province + " " + city + " " + county);
            }

            @Override
            public void onPicked(int i, int j, int p) {
                viewModel.setSelectedProvince(i, j, p);
            }
        });
        picker.show();
    }

}

