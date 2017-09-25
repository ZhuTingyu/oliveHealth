package com.olive.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.holder.CartProductViewHolder;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/9/8.
 */

public class ProductInfoWithNumberAdapter extends BaseAdapter {

    List<ProductEntity> mData;
    LayoutInflater inflater;
    boolean isLook;

    public ProductInfoWithNumberAdapter(Context context, List<ProductEntity> mData, boolean isLook){
        this.mData = mData;
        inflater = LayoutInflater.from(context);
        this.isLook = isLook;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ProductEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartProductViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_cart_layout, parent,false);
            viewHolder = new CartProductViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (CartProductViewHolder) convertView.getTag();
        }
        viewHolder.bindData(mData.get(position),isLook);
        return convertView;
    }

    public void setData(List<ProductEntity> mData) {
        this.mData = mData;
    }
}
