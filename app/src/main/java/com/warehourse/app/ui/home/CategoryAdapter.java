package com.warehourse.app.ui.home;

import com.biz.base.BaseArrayListAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.widget.ExpandGridView;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.HomeEntity;
import com.warehourse.app.model.entity.HomeEntity.HomeItemEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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

class CategoryAdapter extends BaseArrayListAdapter<HomeItemEntity> {

    public static GridView createGridView(ViewGroup parent, HomeEntity entity) {
        if (HomeEntity.TYPE_IMAGE_NAVIGATION.equals(entity.type)&&entity.items!=null&&entity.items.size()>0) {
            ExpandGridView gridView = new ExpandGridView(parent.getContext());

            CategoryAdapter categoryAdapter = new CategoryAdapter(parent.getContext());
            categoryAdapter.setList(entity.items);
            gridView.setAdapter(categoryAdapter);
            gridView.setNumColumns(3);
            gridView.setBackgroundColor(Color.WHITE);
            //parent.addView(gridView);
            return gridView;
        }
        return null;
    }

    public CategoryAdapter(Context context) {
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
        HomeItemEntity entity = getItem(position);
        holder.bindData(entity);
        holder.setListener(holder.itemView, entity.link);

        return convertView;
    }


}
