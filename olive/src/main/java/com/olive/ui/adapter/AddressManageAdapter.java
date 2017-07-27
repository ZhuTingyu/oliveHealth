package com.olive.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.ui.main.my.address.AddNewAddressFragment;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class AddressManageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private BaseFragment fragment;

    public AddressManageAdapter(BaseFragment context) {
        super(R.layout.item_address_manage_layout, Lists.newArrayList());
        this.fragment = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        holder.setText(R.id.tv_name, "张三");
        holder.setText(R.id.tv_phone, "1234567890");
        holder.setText(R.id.tv_address, "四川省成都市高新区天府大道北段1700号环球中心E1-1801");

        CheckBox chooseDefault = holder.findViewById(R.id.cb_default);
        TextView tvEdit  = holder.findViewById(R.id.tv_edit);
        TextView tvDelete = holder.findViewById(R.id.tv_delete);

        chooseDefault.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        tvEdit.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(fragment.getBaseActivity(), AddNewAddressFragment.class, true);
        });

        tvDelete.setOnClickListener(v -> {
            createDialog();
        });

    }

    private void createDialog(){
        DialogUtil.createDialogView(fragment.getContext(),R.string.text_make_sure_delete_address
                ,null,R.string.btn_cancel
                ,(dialog, which) -> {

                },R.string.text_action_delete);
    }

}
