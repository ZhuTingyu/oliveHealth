package com.warehourse.app.ui.category;


import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.Lists;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductFilterItemEntity;

import android.widget.TextView;
import android.widget.Toast;

public class FilterKey2Adapter extends BaseSectionQuickAdapter<ProductFilterItemSection, BaseViewHolder> {
    protected FilterKey2Adapter() {
        super(R.layout.item_settings_layout, R.layout.item_section_header_layout, Lists.newArrayList());
    }


    @Override
    protected void convert(BaseViewHolder holder, ProductFilterItemSection section) {
        ProductFilterItemEntity entity = section.t;
        TextView titleText = (TextView) holder.itemView.findViewById(R.id.title);
        TextView textView = (TextView) holder.itemView.findViewById(R.id.text);

        if(entity.isSelected)
        {
            titleText.setTextColor(holder.getColors(R.color.base_color));
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    DrawableHelper.getDrawableWithBounds(titleText.getContext(), R.drawable.ic_done_black_24dp), null);
        }else{
            titleText.setTextColor(holder.getColors(R.color.color_515151));
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        titleText.setText(entity.label==null?"":entity.label);
        holder.itemView.setOnClickListener(v->{
            if(!entity.isSelected&&getSelectItemCount()>=3)
            {
                Toast.makeText(v.getContext(), R.string.toast_three_more, Toast.LENGTH_SHORT).show();
                return;
            }
            entity.isSelected=!entity.isSelected;
            titleText.setSelected(entity.isSelected);
            notifyDataSetChanged();
        });
    }

    @Override
    protected void convertHead(BaseViewHolder viewHolder, ProductFilterItemSection entity) {
        viewHolder.itemView.setBackgroundResource(R.color.color_divider);
        viewHolder.setText(R.id.title, entity.t.getPrefix());
    }



    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    private  int getSelectItemCount(){
        int count = 0;
        for (int i = 0; i < getData().size(); i++) {
            if (getItem(i).t.isSelected){
                count++;
            }
        }
        return count;
    }

}