package com.warehourse.app.ui.my.receiver;

import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.DialogUtil;
import com.biz.util.RxUtil;
import com.biz.widget.MaterialEditText;
import com.warehourse.app.R;
import com.warehourse.app.ui.my.BaseAddressFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ReceiverChangeFragment extends BaseAddressFragment implements FragmentBackHelper {


    private String receiver;
    private ReceiverChangeViewModel viewModel;

    public boolean isChanged() {
        return receiver != null && !receiver.equals(editIdentity.getText().toString());
    }



    @Override
    public boolean onBackPressed() {
        if (isChanged()) {
            DialogUtil.createDialogViewWithCancel(getActivity(), R.string.dialog_title_notice,
                    R.string.dialog_msg_saved,
                    (dialog, which) -> {
                        getActivity().finish();
                    }, R.string.btn_confirm);
            return true;
        }
        return false;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ReceiverChangeViewModel(this);
        initViewModel(viewModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editUsername.setVisibility(View.GONE);
        editDetail.setVisibility(View.GONE);
        editType.setVisibility(View.GONE);
        editArea.setVisibility(View.GONE);
        editIdentity.setVisibility(View.VISIBLE);
        editReason.setVisibility(View.GONE);
        editArea.setFloatingLabelText("");
        btnOk.setText(R.string.text_change_confirm);
        receiver = editIdentity.getText().toString();
        editIdentity.setHint(R.string.text_hint_receiver);
        editIdentity.setFloatingLabelText(getString(R.string.text_hint_receiver));
        bindData(RxUtil.textChanges(editIdentity), viewModel.setDeliveryName());
        bindUi(RxUtil.click(btnOk), o -> {
            setProgressVisible(true);
            viewModel.save(b -> {
                setProgressVisible(false);
                finish();
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.text_change_receiver);
    }

}
