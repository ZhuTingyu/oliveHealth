package com.olive.ui.main.home;

import com.biz.base.BaseArrayListAdapter;
import com.olive.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

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

class HomeCategoryAdapter extends BaseArrayListAdapter<Object> {



    public HomeCategoryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeViewHolder holder;
        if (convertView == null) {
            holder = new HomeViewHolder(inflater(R.layout.item_home_category_layout, parent));
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {
            holder = (HomeViewHolder) convertView.getTag();
        }
//        HomeItemEntity entity = getItem(position);
//        holder.bindData(entity);
//        holder.setListener(holder.itemView, entity.link);

        return convertView;
    }


}
