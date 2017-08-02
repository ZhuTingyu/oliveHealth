package com.olive.ui.main.home;

import com.biz.base.BaseArrayListAdapter;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.olive.R;
import com.olive.model.entity.CategoryEntity;
import com.olive.util.LoadImageUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Title: CategoryAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  17:30
 *
 * @author johnzheng
 * @version 1.0
 */

class HomeCategoryAdapter extends BaseArrayListAdapter<CategoryEntity> {


    private int size;

    private List<Integer> mDrawableList;


    public HomeCategoryAdapter(Context context) {
        super(context);
        mDrawableList = Lists.newArrayList(
                R.drawable.vector_category_eat,
                R.drawable.vector_category_electronic_equipment,
                R.drawable.vector_category_packing_materials,
                R.drawable.vector_category_daily_use,
                R.drawable.vector_category_all);
        size = context.getResources().getDisplayMetrics().widthPixels/5-Utils.dip2px(24);
        //size = size/4;
    }

    @Override
    public int getCount() {
        return mList != null ? 5 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeViewHolder holder;
        if (convertView == null) {
            holder = new HomeViewHolder(inflater(R.layout.item_home_category_layout, parent));
            ViewGroup.LayoutParams  lp =  holder.icon.getLayoutParams();
            lp.width = size; lp.height = size;
            holder.icon.setLayoutParams(lp);
            //holder.icon.setSize(size, size);
            holder.icon.setRoundAsCircle(true);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (HomeViewHolder) convertView.getTag();
        }



        if(position == 4){
            LoadImageUtil.Builder()
                    .load(mDrawableList.get(4)).drawable().build()
                    .imageOptions(mDrawableList.get(4))
                    .displayImage(holder.icon);
            holder.setTitle("全部");
        }else {
            LoadImageUtil.Builder()
                    .load(getItem(position).icon).http().build()
                    .displayImage(holder.icon);
            holder.setTitle(getItem(position).name);
        }


        return convertView;
    }


}
