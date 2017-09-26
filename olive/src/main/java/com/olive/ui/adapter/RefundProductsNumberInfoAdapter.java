package com.olive.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.holder.LineTextHolder;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/9/25.
 */

public class RefundProductsNumberInfoAdapter extends BaseAdapter {

    List<ProductEntity> data;
    LayoutInflater inflater;


    public RefundProductsNumberInfoAdapter(Context context, List<ProductEntity> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ProductEntity getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LineTextHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_line_text_layout,null);
            viewHolder = new LineTextHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.name);
            viewHolder.content = (TextView) convertView.findViewById(R.id.number);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (LineTextHolder) convertView.getTag();
        }
        ProductEntity productEntity = data.get(position);
        viewHolder.bindData(productEntity.name == null || productEntity.name.isEmpty() ? productEntity.productName : productEntity.name,
                "x" + String.valueOf(data.get(position).quantity));
        return convertView;
    }

    public void setData(List<ProductEntity> data) {
        this.data = data;
    }
}
