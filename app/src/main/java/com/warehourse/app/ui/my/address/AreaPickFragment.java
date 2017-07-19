package com.warehourse.app.ui.my.address;


import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.AreaInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.warehourse.app.model.entity.AreaInfo.*;

/**
 * Title: AreaPickFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:9/19/16  5:44 PM
 *
 * @author johnzheng
 * @version 1.0
 */

public class AreaPickFragment extends BaseFragment {



    public class AreaPickEvent{
        public AreaInfoType index;
        public AreaInfo mAreaInfo;
        public AreaPickEvent(AreaInfoType index, AreaInfo info){
            this.index = index; mAreaInfo = info;
        }
    }

    AreaInfoType index;

    AreaPickAdapter mAdapter;

    List<AreaInfo> mList;

    AreaInfo mAreaInfo;
    RecyclerView mRecyclerView;

    public AreaPickFragment setAreaInfo(AreaInfo areaInfo) {
        mAreaInfo = areaInfo; return this;
    }

    public AreaPickFragment setIndex(AreaInfoType index) {
        this.index = index;
        return this;
    }

    public AreaPickFragment setList(List<AreaInfo> list) {
        mList = list;
        if (isAdded()){
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setNewData(list);
        }
        return this;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()){
            mRecyclerView.setAdapter(mAdapter);
            if (mList!=null && mAdapter.getItemCount()>0&&
                    mList.get(0).id!=mAdapter.getItem(0).id)
                mAdapter.setNewData(mList);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new AreaPickAdapter(getContext());
        if (mList!=null) mAdapter.setNewData(mList);
        mRecyclerView.setAdapter(mAdapter);
        emptyProgress(mAdapter);
        return mRecyclerView;
    }


    private void emptyProgress(BaseQuickAdapter adapter) {
        View view = LayoutInflater.from(getContext()).inflate( R.layout.loading_layout, null);
        view.setBackgroundResource(R.color.color_transparent);
        adapter.setEmptyView(view);
    }

    class AreaPickAdapter extends BaseQuickAdapter<AreaInfo, BaseViewHolder> {

        public AreaPickAdapter(Context context) {
            super(R.layout.item_area_layout);
        }


        @Override
        protected void convert(BaseViewHolder holder, AreaInfo areaInfo) {
            if (mAreaInfo!=null && mAreaInfo.id == areaInfo.id){
                holder.setTextColor(R.id.title, getColors(R.color.base_color));
                holder.setViewVisible(R.id.icon, View.VISIBLE);
            }else {
                holder.setTextColor(R.id.title, getColors(R.color.color_2c2c2c));
                holder.setViewVisible(R.id.icon, View.GONE);
            }
            holder.setText(R.id.title, areaInfo.name);
            holder.itemView.setOnClickListener(v -> {
                mAreaInfo = areaInfo;
                notifyDataSetChanged();
                EventBus.getDefault().post(new AreaPickEvent(index, mAreaInfo));
            });
        }


    }




}
