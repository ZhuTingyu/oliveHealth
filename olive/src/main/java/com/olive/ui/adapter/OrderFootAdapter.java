package com.olive.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.biz.base.BaseViewHolder;
import com.biz.util.PriceUtil;
import com.olive.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;

/**
 * Created by TingYu Zhu on 2017/7/31.
 */

public class OrderFootAdapter extends BaseLineTextListAdapter{

    public OrderFootAdapter(String[] title) {
        super(title);
    }

    @Override
    protected void convert(BaseViewHolder holder, String s) {
        super.convert(holder, s);
        if(holder.getAdapterPosition() == 2 && mData.size() == 7){
            TextView number = holder.findViewById(R.id.number);
            number.setTextColor(mContext.getResources().getColor(R.color.color_red));
        }
    }

}
