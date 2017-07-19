package com.biz.imageselector.view;

import com.facebook.drawee.view.SimpleDraweeView;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Title: SquaredImageView
 * Description: An image view which always remains square with respect to its width.
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/7/27  下午3:07
 *
 * @author dengqinsheng
 * @version 1.0
 */

class SquaredImageView extends SimpleDraweeView {
    public SquaredImageView(Context context) {
        super(context);
    }

    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
