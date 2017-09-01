package com.olive.ui.adapter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.widget.CountEditText;
import com.chad.library.adapter.base.BaseViewHolder;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.main.cart.CartViewModel;
import com.olive.util.LoadImageUtil;
import com.olive.util.Utils;

import java.util.Collection;
import java.util.List;


/**
 * Created by TingYu Zhu on 2017/7/25.
 */

public class CartAdapter extends BaseChooseAdapter<ProductEntity, BaseViewHolder> {

    private CartViewModel viewModel;
    private onNumberChangeListener onNumberChangeListener;
    private TextView tvPrice;
    private onCheckClickListener onCheckClickListener;
    private int buyAgainProductsNumber;

    public CartAdapter() {
        super(R.layout.item_cart_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductEntity productEntity) {
        LoadImageUtil.Builder()
                .load(productEntity.imgLogo).http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, productEntity.name);
        holder.setText(R.id.title_line_2, mContext.getString(R.string.text_product_specification, productEntity.standard));
        holder.setText(R.id.title_line_3, PriceUtil.formatRMB(productEntity.price));

        EditText countEditText = holder.getView(R.id.ed_count);
        countEditText.setText(productEntity.quantity + "");
        countEditText.setFocusableInTouchMode(false);

        AppCompatCheckBox checkBox = holder.getView(R.id.checkbox);

        checkBox.setOnClickListener(v -> {
            if (isSelected(holder.getAdapterPosition())) {
                checkBox.setChecked(false);
                cancelSelected(holder.getAdapterPosition());
            } else {
                checkBox.setChecked(true);
                setSelected(holder.getAdapterPosition());
            }
            onCheckClickListener.click(checkBox, holder.getAdapterPosition());
        });

        checkBox.setChecked(booleanArray.get(holder.getAdapterPosition()));


        holder.getView(R.id.btn_min).setOnClickListener(v -> {
            onNumberChangeListener.min(productEntity);
        });

        holder.getView(R.id.btn_add).setOnClickListener(v -> {
            onNumberChangeListener.add(productEntity);
        });

        holder.getView(R.id.ed_count).setOnClickListener(v -> {

        });

    }

    public void isChooseAll(boolean isChooseAll) {
        for (int i = 0; i < mData.size(); i++) {
            booleanArray.set(i, isChooseAll);
            notifyDataSetChanged();
            viewModel.getTotalPrice(aLong -> {
                setPrice(aLong);
            });
        }

        if (!isChooseAll) {
            setPrice(0);
        }

    }

    public void chooseBuyAgainProducts() {
        for (int i = 0; i < buyAgainProductsNumber; i++) {
            booleanArray.set(i, true);
        }
    }

    private void setPrice(long price) {
        tvPrice.setText(PriceUtil.formatRMB(price));
    }

    public void setViewModel(CartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setTvPrice(TextView tvPrice) {
        this.tvPrice = tvPrice;
    }

    public void setBuyAgainProductsNumber(int buyAgainProductsNumber) {
        this.buyAgainProductsNumber = buyAgainProductsNumber;
    }

    public interface onNumberChangeListener {
        void add(ProductEntity productEntity);

        void min(ProductEntity productEntity);
    }

    public interface onCheckClickListener {
        void click(CheckBox checkBox, int position);
    }

    public void setOnNumberChangeListener(onNumberChangeListener listener) {
        this.onNumberChangeListener = listener;
    }

    public void setOnCheckClickListener(onCheckClickListener listener) {
        this.onCheckClickListener = listener;
    }

    public void deleteChoose() {
        for (int i = 0; i < booleanArray.size(); ) {
            if (booleanArray.get(i)) {
                booleanArray.remove(i);
                mData.remove(i);
                continue;
            }
            i++;
        }
        notifyDataSetChanged();
    }

    @Override
    public void setNewData(@Nullable List<ProductEntity> data) {
        super.setNewData(data);
        if (data.isEmpty()) {
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_cart));
        }
    }

    @Override
    public void replaceData(@NonNull Collection<? extends ProductEntity> data) {
        super.replaceData(data);
        if (data.isEmpty()) {
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_cart));
        }
    }
}
