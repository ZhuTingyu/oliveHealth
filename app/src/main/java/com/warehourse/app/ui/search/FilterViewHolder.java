package com.warehourse.app.ui.search;

import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.warehourse.app.R;
import com.warehourse.app.model.entity.ProductSearchParaEntity;

import android.graphics.drawable.LevelListDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

/**
 * Title: FilterLayoutHelper
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:24/05/2017  15:14
 *
 * @author johnzheng
 * @version 1.0
 */

public class FilterViewHolder extends BaseViewHolder{

    public AppCompatTextView mTextSales;
    public AppCompatTextView mTextPrice;
    public AppCompatTextView mTextFilter;
    private LevelListDrawable mDrawableSales, mDrawablePrice;
    public View mSaleView, mPriceView, mFilterView;

    public FilterViewHolder(View itemView) {
        super(itemView);
        mTextSales = findViewById(R.id.text_sales);
        mTextPrice = findViewById(R.id.text_price);
        mTextFilter = findViewById(R.id.text_filter);
        mSaleView = (View) mTextSales.getParent();
        mPriceView = (View) mTextPrice.getParent();
        mFilterView = (View) mTextFilter.getParent();
        mDrawableSales = (LevelListDrawable) DrawableHelper
                .getDrawableWithBounds(itemView.getContext(), R.drawable.ic_price_layer);
        mDrawablePrice = (LevelListDrawable) DrawableHelper
                .getDrawableWithBounds(itemView.getContext(), R.drawable.ic_price_layer);

        mDrawableSales.selectDrawable(2);
        mDrawablePrice.selectDrawable(0);

        mTextSales.setTag(2);
        mTextPrice.setTag(0);
        mTextPrice.setSelected(false);
        mTextSales.setSelected(true);

        mTextSales.setCompoundDrawables(null, null, mDrawableSales, null);
        mTextPrice.setCompoundDrawables(null, null, mDrawablePrice, null);


    }

    public View getTextSalesParent() {
        return mSaleView;
    }

    public View getTextFilterParent() {
        return mFilterView;
    }

    public View getTextPriceParent() {
        return mPriceView;
    }

    public int setTextSalesClick(){
        mTextPrice.setSelected(false);
        mTextSales.setSelected(true);
        mDrawablePrice.selectDrawable(0);
        int level = (int) mTextSales.getTag();
        mDrawableSales.selectDrawable(level = level==2?1:2);
        mTextSales.setTag(level);
        return level;
    }

    public int setTextPriceClick(){
        mTextPrice.setSelected(true);
        mTextSales.setSelected(false);
        int level = (int) mTextPrice.getTag();
        mDrawablePrice.selectDrawable(level =level==2?1:2);
        mDrawableSales.selectDrawable(0);
        mTextPrice.setTag(level);
        return level;
    }

    public void setSort(String sort) {
        if (TextUtils.isEmpty(sort)) return;
        switch (sort) {
            case ProductSearchParaEntity.SORT_SALE_PRICE_ASC:
                mTextPrice.setTag(2);
                setTextPriceClick();
                break;
            case ProductSearchParaEntity.SORT_SALE_PRICE_DESC:
                mTextPrice.setTag(1);
                setTextPriceClick();
                break;
            case ProductSearchParaEntity.SORT_SALES_VOLUME_ASC:
                mTextSales.setTag(2);
                setTextSalesClick();
                break;
            case ProductSearchParaEntity.SORT_SALES_VOLUME_DESC:
                mTextSales.setTag(1);
                setTextSalesClick();
                break;
            default:
        }
    }
}
