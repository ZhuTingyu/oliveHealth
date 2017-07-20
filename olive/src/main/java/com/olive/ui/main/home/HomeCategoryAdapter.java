package com.olive.ui.main.home;

import com.biz.base.BaseArrayListAdapter;
import com.biz.util.Lists;
import com.biz.util.Utils;
import com.olive.R;
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

class HomeCategoryAdapter extends BaseArrayListAdapter<String> {


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
        setList(Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_category)));
        size = context.getResources().getDisplayMetrics().widthPixels/5-Utils.dip2px(24);
        //size = size/4;
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
//        HomeItemEntity entity = getItem(position);
//        holder.bindData(entity);
//        holder.setListener(holder.itemView, entity.link);

        LoadImageUtil.Builder()
                .load(mDrawableList.get(position)).drawable().build()
                .imageOptions(mDrawableList.get(position))
                .displayImage(holder.icon);
        holder.setTitle(getItem(position));

        /*LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(holder.icon);*/
        holder.setTitle("标题");


        return convertView;
    }


}
