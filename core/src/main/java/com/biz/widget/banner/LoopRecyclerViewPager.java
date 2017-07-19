package com.biz.widget.banner;


import com.lsjwzh.widget.recyclerviewpager.LoopRecyclerViewPagerAdapter;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPagerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Title: LoopRecyclerviewPager
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:18/05/2017  11:38
 *
 * @author johnzheng
 * @version 1.0
 */

public class LoopRecyclerViewPager extends RecyclerViewPager {


    private PageIndicator pageIndicator;

    private boolean pageIndicatorNeedInit = false;


    public LoopRecyclerViewPager(Context context) {
        super(context);
    }

    public LoopRecyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private int getActualItemCountFromAdapter() {
        if (this.getAdapter()!=null)
        return ((LoopRecyclerViewPagerAdapter) this.getWrapperAdapter()).getActualItemCount();
        return 0;
    }

    private LoopRecyclerViewPagerAdapter getActualAdapter() {
        return (LoopRecyclerViewPagerAdapter) this.getWrapperAdapter();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        super.scrollToPosition(this.getMiddlePosition());

        if (pageIndicator != null && pageIndicatorNeedInit) {
            pageIndicator.InitIndicatorItems(getActualItemCountFromAdapter());
            pageIndicator.onPageSelected(0);
            pageIndicatorNeedInit = true;
        }

    }

    public void setPageIndicator(PageIndicator pageIndicator) {
        this.pageIndicator = pageIndicator;
        pageIndicatorNeedInit = true;
        if (getAdapter() != null) {
            pageIndicator.InitIndicatorItems(getActualItemCountFromAdapter());
            pageIndicator.onPageSelected(0);
            pageIndicatorNeedInit = false;
        }
        addOnPageChangedListener((int last, int index) -> {
            pageIndicator.onPageSelected(getActualAdapter().getActualPosition(index));
            pageIndicator.onPageUnSelected(getActualAdapter().getActualPosition(last));

        });

    }


    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        super.scrollToPosition(this.getMiddlePosition());
    }

    @NonNull
    protected RecyclerViewPagerAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {
        return adapter instanceof LoopRecyclerViewPagerAdapter?(LoopRecyclerViewPagerAdapter)adapter:new LoopRecyclerViewPagerAdapter(this, adapter);
    }

    public void smoothScrollToPosition(int position) {
        if (position<0)return;
        int transformedPosition = this.transformInnerPositionIfNeed(position);
        super.smoothScrollToPosition(transformedPosition);
    }

    public void scrollToPosition(int position) {
        int i = this.transformInnerPositionIfNeed(position);
        if (i<0)return;
        super.scrollToPosition(i);
    }

    public int getActualCurrentPosition() {
        int position = this.getCurrentPosition();
        return this.transformToActualPosition(position);
    }

    public int transformToActualPosition(int position) {
        return position % this.getActualItemCountFromAdapter();
    }



    private int transformInnerPositionIfNeed(int position) {
        try {
            int actualItemCount = this.getActualItemCountFromAdapter();
            int actualCurrentPosition = this.getCurrentPosition() % actualItemCount;
            int bakPosition1 = this.getCurrentPosition() - actualCurrentPosition + position % actualItemCount;
            int bakPosition2 = this.getCurrentPosition() - actualCurrentPosition - actualItemCount + position % actualItemCount;
            int bakPosition3 = this.getCurrentPosition() - actualCurrentPosition + actualItemCount + position % actualItemCount;
            return Math.abs(bakPosition1 - this.getCurrentPosition()) > Math.abs(bakPosition2 - this.getCurrentPosition())?(Math.abs(bakPosition2 - this.getCurrentPosition()) > Math.abs(bakPosition3 - this.getCurrentPosition())?bakPosition3:bakPosition2):(Math.abs(bakPosition1 - this.getCurrentPosition()) > Math.abs(bakPosition3 - this.getCurrentPosition())?bakPosition3:bakPosition1);

        }catch (Exception e){
            return -1;
        }
    }

    private int getMiddlePosition() {
        int middlePosition = 1073741823;
        int actualItemCount = this.getActualItemCountFromAdapter();
        if(actualItemCount > 0 && middlePosition % actualItemCount != 0) {
            middlePosition -= middlePosition % actualItemCount;
        }

        return middlePosition;
    }


}
