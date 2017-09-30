package com.olive.ui.adapter;


import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.main.cart.CartViewModel;
import com.olive.util.LoadImageUtil;
import com.olive.util.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TingYu Zhu on 2017/7/25.
 */

public class ProductMultiChooseAdapter extends BaseQuickAdapter<ProductEntity, BaseViewHolder> {

    private CartViewModel viewModel;
    private onModifyNumberListener onModifyNumberListener;
    private TextView tvPrice;
    private onCheckClickListener onCheckClickListener;
    private int buyAgainProductsNumber;
    private List<Integer> selectedPositions;

    public ProductMultiChooseAdapter() {
        super(R.layout.item_cart_layout, Lists.newArrayList());
    }

    @Override
    protected void convert(BaseViewHolder holder, ProductEntity productEntity) {
        LoadImageUtil.Builder()
                .load(productEntity.imgLogo).http().build()
                .displayImage(holder.getView(R.id.icon_img));
        holder.setText(R.id.title, productEntity.name);
        holder.setText(R.id.title_line_2, mContext.getString(R.string.text_product_specification, productEntity.standard));
        long price = 0;
        if(productEntity.price == 0){
            if(productEntity.originalPrice != 0){
                price = productEntity.originalPrice;
            }else {
                price = productEntity.salePrice;
            }
        }else {
            price = productEntity.price;
        }
        holder.setText(R.id.title_line_3, PriceUtil.formatRMB(price));

        EditText countEditText = holder.getView(R.id.ed_count);
        if(productEntity.quantity == 0){
            productEntity.quantity = 1;
        }
        countEditText.setText(String.valueOf(productEntity.quantity));
        countEditText.setFocusableInTouchMode(false);

        AppCompatCheckBox checkBox = holder.getView(R.id.checkbox);

        checkBox.setOnClickListener(v -> {
            checkBox.setChecked(!productEntity.isChoose);
            setChoose(productEntity, !productEntity.isChoose);
            onCheckClickListener.checkBoxClick(checkBox, holder.getAdapterPosition());
        });

        checkBox.setChecked(productEntity.isChoose);


        holder.getView(R.id.btn_min).setOnClickListener(v -> {
            onModifyNumberListener.min(productEntity);
        });

        holder.getView(R.id.btn_add).setOnClickListener(v -> {
            onModifyNumberListener.add(productEntity);
        });

        holder.getView(R.id.ed_count).setOnClickListener(v -> {

        });

    }

    public void isChooseAll(boolean isChooseAll) {
        setAllChoose(mData ,isChooseAll);
        notifyDataSetChanged();
        viewModel.getTotalPrice(aLong -> {
            setPrice(aLong);
        });

        if (!isChooseAll) {
            setPrice(0);
        }

    }

    public void setPrice(long price) {
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

    public interface onModifyNumberListener {
        void add(ProductEntity productEntity);

        void min(ProductEntity productEntity);
    }

    public interface onCheckClickListener {
        void checkBoxClick(CheckBox checkBox, int position);
    }

    public void setOnModifyNumberListener(onModifyNumberListener listener) {
        this.onModifyNumberListener = listener;
    }

    public void setOnCheckClickListener(onCheckClickListener listener) {
        this.onCheckClickListener = listener;
    }

    public void deleteChoose() {
        for (int i = 0; i < mData.size();) {
            if (mData.get(i).isChoose) {
                mData.remove(i);
                continue;
            }
            i++;
        }
        notifyDataSetChanged();
    }

    @Override
    public void setNewData(@Nullable List<ProductEntity> data) {

        if(mData.size() != 0){
            //购物车增加了商品
            if(mData.size() <= data.size()){
                setIsChooseData(data);
                //初始化新添数据的选择状态
                for(int i = mData.size(); i < data.size(); i++){
                    setChoose(data.get(i), false);
                }
                //购物车删除了商品
            }else if(mData.size() > data.size()) {
                setAllChoose(data, false);
            }
        }

        if (data.isEmpty()) {
            Utils.setEmptyView(this, mContext.getString(R.string.message_empty_cart));
        }
        super.setNewData(data);
    }



    private void setChoose(ProductEntity productEntity, boolean isChoose){
        productEntity.isChoose = isChoose;
    }

    private void setAllChoose(List<ProductEntity> productEntities, boolean isChoose){
        for(ProductEntity productEntity : productEntities){
            setChoose(productEntity, isChoose);
        }
    }

    /**
     * 设置新数据中已经选择了的商品的状态
     * @param data 购物车数据变化过后新数据
     */
    private void setIsChooseData(List<ProductEntity> data){
        ArrayList<Integer> selectedPotion = (ArrayList<Integer>) getSelectedPotion();
        if(selectedPotion.size() > 0){
            for(int i = 0, len = selectedPotion.size(); i < len; i++){
                data.get(selectedPotion.get(i)).isChoose = true;
            }
        }

    }

    public List<Integer> getSelectedPotion(){
        selectedPositions = Lists.newArrayList();
        for(int i = 0; i < mData.size(); i++){
            if(mData.get(i).isChoose){
                selectedPositions.add(i);
            }
        }
        return selectedPositions;
    }
}
