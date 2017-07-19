package com.biz.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseArrayListAdapter<T> extends BaseAdapter {

    protected ArrayList<T> mList;

    protected Context mContext;
    protected LayoutInflater inflater;
    public SparseBooleanArray isSelected;
    public List<T> items;
    public List<T> filtered;
    protected Filter filter;


    private boolean isEditMode = false;

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        notifyDataSetChanged();
    }

    public BaseArrayListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    public void initSelected() {
        isSelected = new SparseBooleanArray();
        for (int i = 0; i < getCount(); i++) {
            isSelected.put(i, false);
        }
    }



    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

    public ArrayList<T> getList() {
        return mList;
    }

    public void setList(T[] list) {
        setList(Arrays.asList(list));
    }

    public void setList(List<T> list) {
        ArrayList<T> arrayList = new ArrayList<T>(list.size());
        arrayList.addAll(list);
        this.mList = arrayList;
        initSelected();
        notifyDataSetChanged();
    }

    public void addItem(T obj){
        if (this.mList==null)
            mList = new ArrayList<T>();

        this.mList.add(obj);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        this.mList.remove(position);
        this.notifyDataSetChanged();
    }

    public void removeItem(T obj) {
        this.mList.remove(obj);
        this.notifyDataSetChanged();
    }

    public Drawable getDrawable(int res) {
        Drawable drawable = getContext().getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
        return drawable;
    }

    public void setViewDrawableRight(TextView view, int resId) {
        view.setCompoundDrawables(
                null,
                null
                , getDrawable(resId), null);

    }

    public Filter getFilter() {
        return null;
    }



    public void setViewDrawableLeft(TextView view, int resId, int textId) {
        view.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        view.setText(textId);
    }

    public void setViewDrawableLeft(TextView view, int resId, String textId) {
        view.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        view.setText(textId);
    }

    public View inflater( int layoutRes,ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }
}
