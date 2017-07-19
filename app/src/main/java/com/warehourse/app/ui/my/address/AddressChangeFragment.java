package com.warehourse.app.ui.my.address;

import com.biz.base.FragmentBackHelper;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.AddressStatusEntity;
import com.warehourse.app.ui.my.BaseAddressFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

public class AddressChangeFragment extends BaseAddressFragment implements FragmentBackHelper {



    String reason;
    String area;
    String detail;
    private AddressChangeViewModel viewModel;


    public boolean isChanged(){
        if (reason!=null&&!reason.equals(editReason.getText().toString()))return true;
        if (area!=null&&!area.equals(editArea.getText().toString()))return true;
        return detail != null && !detail.equals(editDetail.getText().toString());
    }

    @Override
    public boolean onBackPressed() {
        if (isChanged()) {
            DialogUtil.createDialogViewWithCancel(getActivity(), R.string.dialog_title_notice,
                    R.string.dialog_msg_saved,
                    (dialog, which) -> {getActivity().finish();}, R.string.btn_confirm);
            return true;
        }
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new AddressChangeViewModel(this);
        super.viewModel = viewModel;
        initViewModel(viewModel);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editUsername.setVisibility(View.GONE);
        editType.setVisibility(View.GONE);
        editIdentity.setVisibility(View.GONE);
        editReason.setVisibility(View.VISIBLE);
        editArea.setFloatingLabelText("");
        btnOk.setText(R.string.text_to_submit);

//        bindUi(RxUtil.click(editArea), o -> {
//            setProgressVisible(true);
//            viewModel.province(list -> {
//                setProgressVisible(false);
//                createSheet(list);
//            });
//        });
        AddressChangeViewModel editShopAddressViewModel = (AddressChangeViewModel) viewModel;
        bindUi(RxUtil.textChanges(editDetail), editShopAddressViewModel.setAddress());
        bindUi(RxUtil.textChanges(editReason), editShopAddressViewModel.setReason());
        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            editShopAddressViewModel.updateData(b -> {
                getActivity().setResult(Activity.RESULT_OK);
                setProgressVisible(false);
                DialogUtil.createDialogViewWithFinish(getActivity(), getString(R.string.text_success_submit));
            });

        });
        bindData(editShopAddressViewModel.getAddress(),RxUtil.text(editDetail));
        bindData(editShopAddressViewModel.getCityCh(),RxUtil.text(editArea));


        bindData(viewModel.getCurrAddress(), currAddress -> {
            if (currAddress != null) {
                mAddressPickPagerFragment.provinceAreaInfo.id = currAddress.provinceId;
                mAddressPickPagerFragment.districtAreaInfo.id = currAddress.districtId;
                mAddressPickPagerFragment.cityAreaInfo.id = currAddress.cityId;
                mAddressPickPagerFragment.provinceAreaInfo.name = currAddress.provinceName;
                mAddressPickPagerFragment.districtAreaInfo.name = currAddress.districtName;
                mAddressPickPagerFragment.cityAreaInfo.name = currAddress.cityName;

                String text = currAddress.provinceName + " " + currAddress.cityName
                        + " " + currAddress.districtName;
                editArea.setText(TextUtils.isEmpty(text.replace(" ", "")) ? "" : text);
                editDetail.setText(currAddress.deliveryAddress);

            }
        });
        reason = editReason.getText().toString();
        area = editArea.getText().toString();
        detail = editDetail.getText().toString();
        editShopAddressViewModel.request(b->{});


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_change_address);
    }

}
