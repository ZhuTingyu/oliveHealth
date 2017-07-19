package com.warehourse.app.ui.order.detail;

import com.biz.base.BaseViewHolder;
import com.biz.util.PriceUtil;
import com.biz.widget.CustomDraweeView;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.view.View;
import android.widget.TextView;

/**
 * Title: ProductViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  16:00
 *
 * @author johnzheng
 * @version 1.0
 */

public class ProductViewHolder extends BaseViewHolder{

    private CustomDraweeView icon;
    private TextView textFree;
    private TextView title;
    private TextView textPrice;
    private TextView textCount;
    private TextView textPromo;


    public ProductViewHolder(View itemView) {
        super(itemView);
        icon =  findViewById(R.id.icon);
        textFree =  findViewById(R.id.text_free);
        title =  findViewById(R.id.title);
        findViewById(R.id.title_right).setVisibility(View.GONE);
        findViewById(R.id.text_line2).setVisibility(View.INVISIBLE);
        textPrice =  findViewById(R.id.text_line3);
        textCount = findViewById(R.id.text_line3_right);
        textPromo =  findViewById(R.id.text_promo);
    }
    public void bindData(ProductEntity entity){
        setOrderView(entity.getLogo(), entity.name, entity.salePrice, entity.quantity, entity.isGift());
    }

    private void setOrderView(String url, String name, long price, int count, boolean isFree){
        title.setText(name);
        textPrice.setText(PriceUtil.formatRMB(price));
        textCount.setText("x "+count);
        textPrice.setTextColor(getColors(R.color.color_333333));
        textFree.setVisibility(isFree?View.VISIBLE:View.GONE);
        textFree.setText(R.string.text_word_gift);
        LoadImageUtil.Builder().load(url).build()
                .imageOptions(R.drawable.ic_product_default)
                .displayImage(icon);
    }

}

