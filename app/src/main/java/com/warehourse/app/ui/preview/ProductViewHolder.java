package com.warehourse.app.ui.preview;

import com.biz.base.BaseViewHolder;
import com.biz.util.PriceUtil;
import com.biz.widget.CustomDraweeView;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductViewHolder extends BaseViewHolder {
    CustomDraweeView icon;
    TextView title;
    TextView textCount;
    TextView textPrice;
    TextView textPromo;
    TextView textFree;

    public ProductViewHolder(View view) {
        super(view);
        icon = findViewById(R.id.icon);
        textFree = findViewById(R.id.text_free);
        title = findViewById(R.id.title);
        textCount = findViewById(R.id.title_right);
        View viewLine2=findViewById(R.id.text_line2);
        if(viewLine2!=null) {
            viewLine2.setVisibility(View.INVISIBLE);
        }
        textPrice = findViewById(R.id.text_line3);
        View viewLine3=findViewById(R.id.text_line3_right);
        if(viewLine3!=null) {
            viewLine3.setVisibility(View.GONE);
        }
        textPromo = findViewById(R.id.text_promo);
    }

    public static ProductViewHolder createViewHolder(ViewGroup parent) {
        return new ProductViewHolder(inflater(R.layout.item_product_order_layout, parent));
    }


    public void bindData(ProductEntity entity){
        setOrderView(entity.getLogo(), entity.name, entity.salePrice, entity.quantity, entity.isGift());
    }

    private void setOrderView(String url, String name, long price, int count, boolean isFree){
        title.setText(name);
        textPrice.setText(PriceUtil.formatRMB(price));
        textCount.setText("x "+count);
        textCount.setTextColor(getColors(R.color.color_333333));
        textFree.setVisibility(isFree?View.VISIBLE:View.GONE);
        textFree.setText(R.string.text_word_gift);
        LoadImageUtil.Builder().load(url).build()
                .imageOptions(R.drawable.ic_product_default)
                .displayImage(icon);
    }
}
