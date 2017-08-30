package com.olive.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.olive.util.Utils;

import java.util.Collection;
import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class AddressManageAdapter extends BaseQuickAdapter<AddressEntity, BaseViewHolder> {

    private AddressViewModel viewModel;

    private BaseFragment fragment;

    private static final int ADDRESS_UPDATE_REQUEST = 0x235;
    public static final int ADDRESS_ADD_REQUEST = 0x236;

    private int updatePosition;

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
        TextView tvEdit = holder.findViewById(R.id.tv_edit);
        TextView tvDelete = holder.findViewById(R.id.tv_delete);

        chooseDefault.setOnClickListener(v -> {
            setDefaultAddress(holder, addressEntity);
        });

        chooseDefault.setChecked(addressEntity.isDefault == AddressViewModel.IS_DEFAULT);

        tvEdit.setOnClickListener(v -> {
            updatePosition = holder.getAdapterPosition();
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_DATA, addressEntity)
                    .startParentActivity(fragment.getBaseActivity(), EditAddressFragment.class, ADDRESS_UPDATE_REQUEST);
        });

        chooseDefault.setText(addressEntity.isDefault == AddressViewModel.IS_DEFAULT ? fragment.getString(R.string.text_default_address) : fragment.getString(R.string.text_set_default_address));

        tvDelete.setOnClickListener(v -> {
            createDialog(addressEntity);
        });

    }

    private void createDialog(AddressEntity addressEntity) {
        DialogUtil.createDialogView(fragment.getContext(), R.string.text_make_sure_delete_address
                , null, R.string.btn_cancel
                , (dialog, which) -> {
                    fragment.setProgressVisible(true);
                    viewModel.setAddressId(addressEntity.id);
                    viewModel.deleteAddress(s -> {
                        fragment.setProgressVisible(false);
                        ToastUtils.showLong(mContext, mContext.getString(R.string.message_delete_success));
                    });
                }, R.string.text_action_delete);
    }

    public void setViewModel(AddressViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setDefaultAddress(BaseViewHolder holder, AddressEntity entity) {
        fragment.setProgressVisible(true);
        viewModel.setAddressEntity(getItem(holder.getAdapterPosition()));
        viewModel.setIsDefault();
        viewModel.updateAddress(s -> {
            fragment.setProgressVisible(false);
            Intent intent = new Intent();
            intent.putExtra(IntentBuilder.KEY_DATA, entity);
            fragment.getBaseActivity().setResult(AddressManageFragment.ADDRESS_RESULT, intent);

            dataUpdate();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDRESS_UPDATE_REQUEST) {
            if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
                AddressEntity entity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                mData.set(updatePosition, entity);
                notifyDataSetChanged();
            }
        }if(requestCode == ADDRESS_ADD_REQUEST){
            dataUpdate();
        }
    }

    private void dataUpdate(){
        viewModel.getAddressList(addressEntities -> {
            replaceData(addressEntities);
        });
    }

    @Override
    public void setNewData(@Nullable List<AddressEntity> data) {
        super.setNewData(data);
        if(data.isEmpty()){
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_address));
        }
    }

    @Override
    public void replaceData(@NonNull Collection<? extends AddressEntity> data) {
        super.replaceData(data);
        if(data.isEmpty()){
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_address));
        }
    }
}


