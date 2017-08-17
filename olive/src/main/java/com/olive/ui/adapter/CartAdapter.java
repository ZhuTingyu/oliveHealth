package com.olive.ui.adapter;


import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CheckBox;
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


/**
 * Created by TingYu Zhu on 2017/7/25.
 */

public class CartAdapter extends BaseChooseAdapter<ProductEntity, BaseViewHolder> {

    private CartViewModel viewModel;
    private BaseFragment fragment;
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

        CountEditText countEditText = holder.getView(R.id.ed_count);
        countEditText.setText(productEntity.quantity + "");

        AppCompatCheckBox checkBox = holder.getView(R.id.checkbox);

        checkBox.setOnClickListener(v -> {
            onCheckClickListener.click(checkBox, holder.getAdapterPosition());
        });

        checkBox.setChecked(sparseBooleanArray.get(holder.getAdapterPosition()));



        holder.getView(R.id.btn_min).setOnClickListener(v -> {
            onNumberChangeListener.min(productEntity);
        });

        holder.getView(R.id.btn_add).setOnClickListener(v -> {
            onNumberChangeListener.add(productEntity);
        });

        holder.getView(R.id.ed_count).setOnClickListener(v -> {

        });

    }

    public void isChooseAll(boolean isChooseAll){
        for(int i = 0; i < mData.size(); i++){
            sparseBooleanArray.put(i, isChooseAll);
            notifyDataSetChanged();
            viewModel.getTotalPrice(aLong -> {
                setPrice(aLong);
            });
        }

        if(!isChooseAll){
            setPrice(0);
        }

    }

    public void chooseBuyAgainProducts(){
        for(int i = 0; i < buyAgainProductsNumber; i++){
            sparseBooleanArray.put(i, true);
        }
    }

    private void setPrice(long price){
        tvPrice.setText(PriceUtil.formatRMB(price));
    }

    public void setViewModel(CartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }

    public void setTvPrice(TextView tvPrice) {
        this.tvPrice = tvPrice;
    }

    public void setBuyAgainProductsNumber(int buyAgainProductsNumber) {
        this.buyAgainProductsNumber = buyAgainProductsNumber;
    }

    public interface onNumberChangeListener{
        void add(ProductEntity productEntity);
        void min(ProductEntity productEntity);
    }

    public interface onCheckClickListener{
        void click(CheckBox checkBox, int position);
    }

    public void setOnNumberChangeListener(onNumberChangeListener listener){
        this.onNumberChangeListener = listener;
    }

    public void setOnCheckClickListener(onCheckClickListener listener){
        this.onCheckClickListener = listener;
    }

}
