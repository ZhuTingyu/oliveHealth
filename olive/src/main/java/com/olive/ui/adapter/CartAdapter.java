package com.olive.ui.adapter;


import android.support.v7.widget.AppCompatCheckBox;
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
    private int productNumber;
    private TextView tvPrice;

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
            if(isSelected(holder.getAdapterPosition())){
                checkBox.setChecked(false);
                cancelSelected(holder.getAdapterPosition());
            }else {
                checkBox.setChecked(true);
                setSelected(holder.getAdapterPosition());
            }
            viewModel.getTotalPrice(aLong -> {
                setPrice(aLong);
            });
        });

        checkBox.setChecked(sparseBooleanArray.get(holder.getAdapterPosition()));



        holder.getView(R.id.btn_min).setOnClickListener(v -> {
            if (productEntity.quantity <= productEntity.orderCardinality) {
                fragment.error(mContext.getString(R.string.message_add_cart_min_number, productEntity.orderCardinality + ""));
            } else {
                fragment.setProgressVisible(true);
                productNumber = productEntity.quantity - productEntity.orderCardinality;
                viewModel.setProductNo(productEntity.productNo);
                viewModel.setQuantity(productNumber);
                viewModel.updateProductNumber(s -> {
                    viewModel.getCartProductList(productEntities -> {
                        fragment.setProgressVisible(false);
                        replaceData(productEntities);
                        viewModel.getTotalPrice(aLong -> {
                            setPrice(aLong);
                        });
                    });
                });
            }
        });

        holder.getView(R.id.btn_add).setOnClickListener(v -> {
            fragment.setProgressVisible(true);
            productNumber = productEntity.quantity + productEntity.orderCardinality;
            viewModel.setProductNo(productEntity.productNo);
            viewModel.setQuantity(productNumber);
            viewModel.updateProductNumber(s -> {
                fragment.setProgressVisible(false);
                viewModel.getCartProductList(productEntities -> {
                    replaceData(productEntities);
                    viewModel.getTotalPrice(aLong -> {
                        setPrice(aLong);
                    });
                });
            });
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

}
