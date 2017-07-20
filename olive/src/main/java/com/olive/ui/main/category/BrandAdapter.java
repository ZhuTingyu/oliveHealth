package com.olive.ui.main.category;

import com.biz.base.BaseRecyclerViewAdapter;
import com.biz.base.BaseViewHolder;
import com.olive.R;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Title: BrandAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:10/05/2017  19:05
 *
 * @author johnzheng
 * @version 1.0
 */

 class BrandAdapter extends BaseRecyclerViewAdapter<Object> {

    private  SparseBooleanArray mSparseBooleanArray;
    private View.OnClickListener mOnClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public boolean isSelected(int position){
        return mSparseBooleanArray.get(position);
    }

    public  void setSelected(int position){
        mSparseBooleanArray.clear();
        mSparseBooleanArray.put(position, true);
        notifyDataSetChanged();
    }

    public BrandAdapter() {
        mSparseBooleanArray = new SparseBooleanArray();
        mSparseBooleanArray.put(0, true);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater(R.layout.item_brand_layout, parent));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
//        BrandItemEntity entity = getItem(position);
//        holder.setTextView(R.id.title, entity.label);
        holder.itemView.setSelected(mSparseBooleanArray.get(position, false));
        if (mOnClickListener!=null){
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mOnClickListener);
        }
    }

}
