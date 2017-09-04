package com.olive.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.biz.widget.recyclerview.XRecyclerView;

/**
 * Created by TingYu Zhu on 2017/9/4.
 */

public class RecyclerViewForScrollView extends XRecyclerView {

    public RecyclerViewForScrollView(Context context) {
        super(context);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
