package com.warehourse.app.ui.home;

import com.biz.base.BaseArrayListAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.widget.ExpandGridView;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.HomeEntity;
import com.warehourse.app.model.entity.HomeEntity.HomeItemEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
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

 class ProductAdapter extends BaseArrayListAdapter<HomeItemEntity> {

    public static View createGridView(ViewGroup parent, HomeEntity entity) {
        if (HomeEntity.TYPE_PRODUCT_LIST.equals(entity.type)&&entity.items!=null&&entity.items.size()>0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_home_parent_product_layout, parent, false);
            ExpandGridView gridView = (ExpandGridView) view.findViewById(R.id.gridview_product);
            ProductAdapter adapter = new ProductAdapter(parent.getContext());
            adapter.setList(entity.items);
            gridView.setAdapter(adapter);
            //parent.addView(view);
            return view;
        }
        return null;
    }

    public ProductAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeViewHolder holder ;
        if (convertView==null){
            holder = new HomeViewHolder(inflater(R.layout.item_home_product_layout, parent));
            convertView = holder.itemView;
            convertView.setTag(holder);
        }else {
            holder = (HomeViewHolder) convertView.getTag();
        }

        HomeItemEntity entity = getItem(position);
        holder.bindData(entity);
        holder.setListener(holder.itemView, entity.link);

        return convertView;
    }


}
