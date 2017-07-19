package com.warehourse.app.ui.cart;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.biz.widget.CountEditText;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.CartEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: CartAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:12/05/2017  14:52
 *
 * @author johnzheng
 * @version 1.0
 */

public class CartAdapter extends BaseQuickAdapter<ProductEntity, CartViewHolder> {

    private boolean isShowChecked;
    private View.OnClickListener mRemoveOnClickListener;
    private View.OnClickListener mAddOnClickListener;
    private View.OnClickListener mMinOnClickListener;
    private EditCountListener mEditCountListener;
    private AlertDialog dialogCount;
    private CheckedAllListener mCheckedAllListener;


    public void setCheckedAllListener(CheckedAllListener checkedAllListener) {
        mCheckedAllListener = checkedAllListener;
    }

    public void setRemoveOnClickListener(View.OnClickListener removeOnClickListener) {
        mRemoveOnClickListener = removeOnClickListener;
    }

    public void setAddOnClickListener(View.OnClickListener addOnClickListener) {
        mAddOnClickListener = addOnClickListener;
    }

    public void setMinOnClickListener(View.OnClickListener minOnClickListener) {
        mMinOnClickListener = minOnClickListener;
    }

    public void setEditCountListener(EditCountListener editCountListener) {
        mEditCountListener = editCountListener;
    }

    public void setCheckedAll(boolean checkedAll) {
        List<ProductEntity> list = getData();
        if (list != null) {
            for (ProductEntity productEntity : list) {
                if (productEntity == null) continue;
                productEntity.checked = checkedAll;
            }
        }
        notifyDataSetChanged();
    }

    public void setShowChecked(boolean showChecked) {
        isShowChecked = showChecked;
        notifyDataSetChanged();
    }

    public CartAdapter() {
        super(R.layout.item_cart_layout);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(CartViewHolder holder, ProductEntity entity) {
        holder.swipeLayout.setEnabled(true);
        holder.title.setText(entity.name);
        LoadImageUtil.Builder().load(entity.getLogo())
                .build().imageOptions(R.drawable.ic_product_default)
                .displayImage(holder.icon);
        holder.createTagViews(entity.getTags());
        holder.textMinNumber.setText(holder.icon.getResources()
                .getString(R.string.text_min_number) + entity.minQuantity);
        holder.textMaxNumber.setText(holder.icon.getResources()
                .getString(R.string.text_max_number) + entity.stock);
        holder.textPrice.setText(PriceUtil.formatRMB(entity.salePrice));
        holder.editCount.setText("" + entity.quantity);
        if (entity.isOutOfStock()) {
            holder.titleLine3Right.setText(R.string.text_product_out_of_stock);
            holder.icon1.setText(R.string.text_out_of_stock);
            holder.titleLine3Right.setVisibility(View.VISIBLE);
            holder.icon1.setVisibility(View.VISIBLE);
            holder.icon2.setVisibility(View.VISIBLE);
            holder.layoutCount.setVisibility(View.GONE);
        } else {
            holder.titleLine3Right.setVisibility(View.GONE);
            holder.icon1.setVisibility(View.GONE);
            holder.icon2.setVisibility(View.GONE);
            holder.layoutCount.setVisibility(View.VISIBLE);
        }
        if (entity.isOutShelf()) {
            holder.icon1.setText(holder.icon1.getResources().getString(R.string.text_under_shelf));
            holder.titleLine3Right.setText("");
            holder.icon2.setImageResource(R.color.color_white_transparent_50);
            holder.textMaxNumber.setVisibility(View.GONE);
            holder.titleLine3Right.setVisibility(View.VISIBLE);
            holder.titleLine3Right.setText("'");
            holder.layoutCount.setVisibility(View.GONE);
            holder.icon1.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setVisibility(isShowChecked ? View.VISIBLE : View.GONE);
        if (isShowChecked) {
            holder.layoutCount.setVisibility(View.GONE);
            holder.checkBox.setChecked(entity.checked);
            holder.swipeLayout.setEnabled(false);

        }
        holder.checkBox.setTag(entity);
        holder.checkBox.setOnClickListener(v -> {
            ProductEntity productEntity = (ProductEntity) v.getTag();
            productEntity.checked = !productEntity.checked;
            holder.checkBox.setChecked(productEntity.checked);
            isCheckedAll();
        });

        if (mRemoveOnClickListener != null && holder.btnRemove != null) {
            holder.btnRemove.setTag(entity);
            holder.btnRemove.setOnClickListener(mRemoveOnClickListener);
        }
        if (mAddOnClickListener != null && holder.btnAdd != null) {
            holder.btnAdd.setTag(entity);
            holder.btnAdd.setOnClickListener(mAddOnClickListener);
        }
        if (mMinOnClickListener != null && holder.btnMin != null) {
            holder.btnMin.setTag(entity);
            holder.btnMin.setOnClickListener(mMinOnClickListener);
        }
        holder.editCount.setTag(entity);
        holder.editCount.setFocusableInTouchMode(false);
        holder.editCount.setOnClickListener(v -> {
            ProductEntity productEntity = (ProductEntity) v.getTag();
            createInputDialog(v.getContext(), productEntity);
        });


    }

    public void isCheckedAll() {
        List<ProductEntity> list = getData();
        if (list == null || list.size() == 0) {
            if (mCheckedAllListener != null) {
                mCheckedAllListener.checked(false);
            }
            return;
        }
        boolean isCheckedAll = true;
        for (ProductEntity productEntity : list) {
            if (productEntity == null) continue;
            if (!productEntity.checked) {
                isCheckedAll = false;
                break;
            }
        }
        if (mCheckedAllListener != null) {
            mCheckedAllListener.checked(isCheckedAll);
        }
    }

    public List<String> getCheckedIds() {
        List<ProductEntity> list = getData();
        if (list == null) return Lists.newArrayList();
        List<String> listIds = Lists.newArrayList();
        for (ProductEntity productEntity : list) {
            if (productEntity == null) continue;
            if (productEntity.checked) {
                listIds.add(productEntity.getProductId());
            }
        }
        return listIds;
    }

    public ArrayList<ProductEntity> getPayList() {
        List<ProductEntity> list = getData();
        if (list == null) return Lists.newArrayList();
        ArrayList<ProductEntity> listIds = Lists.newArrayList();
        for (ProductEntity productEntity : list) {
            if (productEntity == null) continue;
            if (productEntity.quantity > 0) {
                listIds.add(productEntity);
            }
        }
        return listIds;
    }

    public boolean checkAdd(View view, ProductEntity productEntity, int count) {
        if (productEntity == null) return false;
        int maxCount = productEntity.getMaxQuantity();
        String name = productEntity.name == null ? "" : productEntity.name;
        if (maxCount <= count) {
            Snackbar.make(view, view.getContext().getString(R.string.text_product_max_count_f2, "" + name, "" + maxCount), Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (productEntity.getShowQuantity() <= count) {
            Snackbar.make(view, view.getContext().getString(R.string.text_product_max_count_f, "" + name), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checkMin(View view, ProductEntity productEntity, int count) {
        if (productEntity == null) return false;
        int minCount = productEntity.minQuantity;
        if (minCount >= count) {
            Snackbar.make(view, view.getContext().getString(R.string.text_product_min_count, "" + minCount), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean checkEditCount(View view, ProductEntity productEntity, int count) {
        if (productEntity == null) return false;
        int maxCount = productEntity.getMaxQuantity();
        int minCount = productEntity.minQuantity;
        String name = productEntity.name == null ? "" : productEntity.name;
        if (maxCount < count) {
            Snackbar.make(view, view.getContext().getString(R.string.text_product_max_count_f2, "" + name, "" + maxCount), Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (productEntity.getShowQuantity() < count) {
            Snackbar.make(view, view.getContext().getString(R.string.text_product_max_count_f, "" + name), Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (minCount > count) {
            Snackbar.make(view, view.getContext().getString(R.string.text_product_min_count, "" + minCount), Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void createInputDialog(Context context, ProductEntity productEntity) {
        if (productEntity == null)
            return;
        int count = productEntity.quantity;
        View view = View.inflate(context, R.layout.dialog_cart_input_layout, null);
        if (dialogCount != null) {
            dialogCount.dismiss();
        }
        dialogCount = new AlertDialog.Builder(context).create();
        dialogCount.setView(view);
        DialogViewHolder holder = new DialogViewHolder(view, dialogCount);
        holder.mEditCount.setText("" + count);
        RxUtil.click(holder.mBtnAdd).subscribe(v -> {
            if (checkAdd(holder.mEditCount, productEntity, holder.getCount())) {
                holder.add();
            }
        });
        RxUtil.click(holder.mBtnMin).subscribe(v -> {
            if (checkMin(holder.mEditCount, productEntity, holder.getCount())) {
                holder.min();
            }
        });
        holder.mBtnConfirm.setOnClickListener(view1 -> {
            if (checkEditCount(holder.mEditCount, productEntity, holder.getCount())) {
                if (holder.getCount() != count) {
                    if (mEditCountListener != null) {
                        mEditCountListener.edit(productEntity.getProductId(), holder.getCount());
                    }
                }
                if (dialogCount != null) {
                    dialogCount.dismiss();
                }
            }
        });
        dialogCount.show();

    }

    static class DialogViewHolder extends BaseViewHolder {
        AppCompatImageButton mBtnMin;
        CountEditText mEditCount;
        AppCompatImageButton mBtnAdd;
        TextView mBtnCancel;
        TextView mBtnConfirm;


        DialogViewHolder(View view, Dialog dialog) {
            super(view);
            mBtnMin = (AppCompatImageButton) view.findViewById(R.id.btn_min);
            mEditCount = (CountEditText) view.findViewById(R.id.edit_count);
            mBtnAdd = (AppCompatImageButton) view.findViewById(R.id.btn_add);
            mBtnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            mBtnConfirm = (TextView) view.findViewById(R.id.btn_confirm);
            mBtnCancel.setOnClickListener(v -> dialog.dismiss());
        }

        public int getCount() {
            try {
                return Utils.getInteger(mEditCount.getText().toString());
            } catch (Exception e) {
                return 0;
            }
        }

        public void add() {
            if (mEditCount != null) {
                mEditCount.setText("" + (getCount() + 1));
            }
        }

        public void min() {
            if (mEditCount != null) {
                mEditCount.setText("" + (getCount() - 1));
            }
        }
    }

    public interface EditCountListener {
        void edit(String productId, int count);
    }

    public interface CheckedAllListener {
        void checked(boolean isAll);
    }
}
