package com.olive.ui.adapter;

import android.widget.CheckBox;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.model.entity.AddressEntity;
import com.olive.ui.main.my.address.AddressManageFragment;
import com.olive.ui.main.my.address.EditAddressFragment;
import com.olive.ui.main.my.address.AddressViewModel;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class AddressManageAdapter extends BaseQuickAdapter<AddressEntity, BaseViewHolder> {

    private AddressViewModel viewModel;

    private BaseFragment fragment;

    public AddressManageAdapter(BaseFragment context) {
        super(R.layout.item_address_manage_layout, Lists.newArrayList());
        this.fragment = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, AddressEntity addressEntity) {
        holder.setText(R.id.tv_name, addressEntity.consignee);
        holder.setText(R.id.tv_phone, addressEntity.mobile);
        holder.setText(R.id.tv_address, addressEntity.detailAddress);

        CheckBox chooseDefault = holder.findViewById(R.id.cb_default);
        TextView tvEdit  = holder.findViewById(R.id.tv_edit);
        TextView tvDelete = holder.findViewById(R.id.tv_delete);

        chooseDefault.setOnClickListener(v -> {
            setDefaultAddress(holder);
        });

        chooseDefault.setChecked(addressEntity.isDefault == 1);

        tvEdit.setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA, addressEntity)
                    .startParentActivity(fragment.getBaseActivity(), EditAddressFragment.class, AddressManageFragment.ADDRESS_UPDATE_REQUEST);
        });

        tvDelete.setOnClickListener(v -> {
            createDialog(addressEntity);
        });

    }

    private void createDialog(AddressEntity addressEntity){
        DialogUtil.createDialogView(fragment.getContext(),R.string.text_make_sure_delete_address
                ,null,R.string.btn_cancel
                ,(dialog, which) -> {
                    fragment.setProgressVisible(true);
                    viewModel.setAddressId(addressEntity.id);
                    viewModel.deleteAddress(s -> {
                        fragment.setProgressVisible(false);
                        ToastUtils.showLong(mContext, mContext.getString(R.string.message_delete_success));
                    });
                },R.string.text_action_delete);
    }

    public void setViewModel(AddressViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setDefaultAddress(BaseViewHolder holder){
        fragment.setProgressVisible(true);
        viewModel.cancelDefaultAddress(addressEntity -> {
            viewModel.setAddressEntity(getItem(holder.getAdapterPosition()));
            viewModel.setIsDefault();
            viewModel.updateAddress(s -> {
                fragment.setProgressVisible(false);
                CheckBox checkBox = (CheckBox) getViewByPosition(getRecyclerView(),0, R.id.cb_default);
                checkBox.setChecked(false);
                checkBox.setText(fragment.getString(R.string.text_set_default_address));

                CheckBox chooseCheckBox = (CheckBox) getViewByPosition(getRecyclerView(), holder.getAdapterPosition(), R.id.cb_default);
                chooseCheckBox.setText(fragment.getString(R.string.text_default_address));

                notifyItemMoved(holder.getAdapterPosition(), 0);
            });
        });
    }
}
