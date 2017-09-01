package com.olive.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;

import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/4.
 */

public abstract class BaseChooseAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T,K>{

    protected List<Boolean> booleanArray;
    protected List<Integer> selectedPositions;

    public BaseChooseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        booleanArray = new ArrayList<>();
    }

    public void initBooleanList(List<T> data){
        booleanArray.clear();
        for(int i = 0; i < data.size(); i++){
            booleanArray.add(false);
        }
    }

    public void setSelected(int position){
        booleanArray.set(position, true);
    }


    public void cancelSelected(int position){
        booleanArray.set(position, false);
    }

    public boolean isSelected(int position){
        return booleanArray.get(position);
    }


    public List<Integer> getSelectedPotion(){
        selectedPositions = Lists.newArrayList();
        for(int i = 0; i < booleanArray.size(); i++){
            if(booleanArray.get(i)){
                selectedPositions.add(i);
            }
        }
        return selectedPositions;
    }

    @Override
    public void replaceData(@NonNull Collection<? extends T> data) {
        if(mData.size() < data.size()){
            for(int i = mData.size(); i < data.size(); i++){
                booleanArray.add(i, false);
            }
        }else if(mData.size() > data.size()) {
            initBooleanList((List<T>) data);
        }
        super.replaceData(data);
    }

    @Override
    public void setNewData(@Nullable List<T> data) {
        initBooleanList(data);
        super.setNewData(data);
    }
}
