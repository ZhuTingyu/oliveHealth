package com.warehourse.app.ui.my.address;

/**
 * Created by johnzheng on 3/19/16.
 */

import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.my.BaseInfoFragment;
import com.warehourse.app.ui.my.phone.PhoneChangeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


public class AddressInfoFragment extends BaseInfoFragment {
    private AddressInfoViewModel mViewModel;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel=new AddressInfoViewModel(this);
        initViewModel(mViewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        icon.setImageDrawable(DrawableHelper.getDrawableWithBounds(getContext(), R.drawable.ic_change_address));
        title.setText(R.string.text_for_address);

        btn2.setText(R.string.text_change_address);
        btn2.setOnClickListener(e->{
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA,mViewModel.getAddressStatusInfo())
                    .startParentActivity(this, AddressChangeFragment.class,100);

        });
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_person_address);
//        titleLine2.setText(UserModel.getInstance().getUserEntity().detailAddress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //viewModel.onDestroy();
    }
    private void initData()
    {
        btn2.setEnabled(false);
        setProgressVisible(true);
        mViewModel.request(b->{
            btn2.setEnabled(true);
            setProgressVisible(false);
            if(mViewModel.getAddressStatusInfo()!=null) {
                titleLine2.setText(
                        mViewModel.getAddressStatusInfo().provinceName+
                                mViewModel.getAddressStatusInfo().cityName+
                                mViewModel.getAddressStatusInfo().districtName+
                                (mViewModel.getAddressStatusInfo().deliveryAddress==null?
                                "":mViewModel.getAddressStatusInfo().deliveryAddress));
                titleState.setVisibility(View.VISIBLE);
                titleState.setText(mViewModel.getAddressStatusInfo().rejectReasons);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            initData();
        }
    }
}

