package com.warehourse.app.ui.my.user;

import com.biz.base.BaseViewHolder;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.util.LoadImageUtil;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.List;

/**
 * Title: UserInfoAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:16/05/2017  13:19
 *
 * @author johnzheng
 * @version 1.0
 */

class UserInfoAdapter extends BaseMultiItemQuickAdapter<UserMultipleItem, BaseViewHolder>
        implements
        HorizontalDividerItemDecoration.MarginProvider,
        FlexibleDividerDecoration.ColorProvider {

    UserInfoAdapter(List<UserMultipleItem> data) {
        super(data);
        addItemType(UserMultipleItem.AVATAR, R.layout.item_avatar_layout);
        addItemType(UserMultipleItem.USERNAME, R.layout.item_settings_layout);
        addItemType(UserMultipleItem.PHONE, R.layout.item_settings_layout);
        addItemType(UserMultipleItem.DELIVERY_NAME, R.layout.item_settings_layout);
        addItemType(UserMultipleItem.DELIVERY_ADDRESS, R.layout.item_settings_layout);
        addItemType(UserMultipleItem.PASSWORD, R.layout.item_settings_layout);

    }

    @Override
    protected void convert(BaseViewHolder holder, UserMultipleItem userMultipleItem) {
        TextView textView = holder.findViewById(R.id.text);
        if (!TextUtils.isEmpty(userMultipleItem.item)) {
            holder.setText(R.id.title, userMultipleItem.item);
        }
        if (!TextUtils.isEmpty(userMultipleItem.text)) {
            textView.setText(userMultipleItem.text);
        }
        if (textView != null) {
            textView.setTextColor(holder.getColors(R.color.color_515151));
        }
        switch (userMultipleItem.getItemType()) {
            case UserMultipleItem.AVATAR:
                getAvatar(holder);
                break;
            case UserMultipleItem.USERNAME:
                textView.setTextColor(holder.getColors(R.color.color_929292));
                textView.setText(UserModel.getInstance().getName());
                textView.setCompoundDrawables(null, null, null, null);
                break;
            case UserMultipleItem.PHONE:
                textView.setText(UserModel.getInstance().getMobile());
                break;
            case UserMultipleItem.DELIVERY_NAME:
                textView.setText(UserModel.getInstance().getUserEntity().deliveryName);
                break;
            case UserMultipleItem.DELIVERY_ADDRESS:
                textView.setTextColor(holder.getColors(R.color.color_d2d2d2));
                break;
            case UserMultipleItem.PASSWORD:
                break;

            default:
        }
    }


    public void notifyItemChangedAvatar() {
        notifyItemChanged(0);
    }

    public void notifyItemChangedPhone() {
        notifyItemChanged(2);
    }

    public void notifyItemChangedDelivery() {
        notifyItemChanged(3);
    }

    private void getAvatar(BaseViewHolder holder) {
        CustomDraweeView avatar = holder.findViewById(R.id.avatar);
        String url = UserModel.getInstance().getAvatar();
        LoadImageUtil.Builder().load(url).build().imageOptions(R.drawable.vector_avatar).displayImage(avatar);
    }

    @Override
    public int dividerColor(int position, RecyclerView parent) {
        return parent.getResources().getColor(R.color.color_divider);
    }

    @Override
    public int dividerLeftMargin(int position, RecyclerView parent) {
        return Utils.dip2px(16);
    }

    @Override
    public int dividerRightMargin(int position, RecyclerView parent) {
        return Utils.dip2px(16);
    }

}
