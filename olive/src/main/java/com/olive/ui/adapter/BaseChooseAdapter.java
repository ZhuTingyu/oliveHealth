package com.olive.ui.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/4.
 */

public abstract class BaseChooseAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T,K>{

    protected SparseBooleanArray sparseBooleanArray;
    protected List<Integer> selectedPositions;

    public BaseChooseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        sparseBooleanArray = new SparseBooleanArray();
    }

    protected void initBooleanList(List<T> data){
        sparseBooleanArray.clear();
        for(int i = 0; i < data.size(); i++){
            sparseBooleanArray.put(i, false);
        }
    }

    public void setSelected(int position){
        sparseBooleanArray.put(position, true);
    }

    public  void setSingleSelected(int position){
        sparseBooleanArray.clear();
        sparseBooleanArray.put(position, true);
        notifyDataSetChanged();
    }

    public void cancelSelected(int position){
        sparseBooleanArray.put(position, false);
    }

    public boolean isSelected(int position){
        return sparseBooleanArray.get(position);
    }


    public List<Integer> getSelectedPotion(){
        selectedPositions = Lists.newArrayList();
        for(int i = 0; i < sparseBooleanArray.size(); i++){
            if(sparseBooleanArray.get(i)){
                selectedPositions.add(i);
            }
        }
        return selectedPositions;
    }

    @Override
    public void replaceData(@NonNull Collection<? extends T> data) {
        if(mData.size() < data.size()){
            for(int i = mData.size()+1; i <= data.size(); i++){
                sparseBooleanArray.put(i, false);
            }
        }else if(mData.size() > data.size()) {
            sparseBooleanArray.delete(sparseBooleanArray.indexOfValue(true));
        }
        super.replaceData(data);
    }

}
