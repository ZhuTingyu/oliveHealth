package com.olive.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class BaseLineTextListAdapter extends BaseAdapter{

    protected String[] title;
    List<String> mData;
    LayoutInflater inflater;

    public BaseLineTextListAdapter(Context context, String[] title, List<String> mData){
        this.mData = mData;
        this.title = title;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_line_text_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.name);
            viewHolder.content = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bindData(title[position], mData.get(position));
        return convertView;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    class ViewHolder{
        TextView title;
        TextView content;

        private void bindData(String titleString, String contentString){
            title.setText(titleString);
            content.setText(contentString);
        }
    }

}
