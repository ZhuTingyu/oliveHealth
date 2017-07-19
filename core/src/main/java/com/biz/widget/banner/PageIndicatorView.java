package com.biz.widget.banner;

import com.biz.http.R;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import rx.Observable;


public class PageIndicatorView extends LinearLayout implements PageIndicator {

    private int resId = R.drawable.ic_page_indicator, resIdFocus = R.drawable.ic_page_indicator_focus;
    public PageIndicatorView(Context context) {
        this(context, null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void InitIndicatorItems(int itemsNumber) {
        removeAllViews();
        setVisibility(itemsNumber<2?GONE:VISIBLE);
        for (int i = 0; i < itemsNumber; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(i==0?resIdFocus:resId);
            imageView.setPadding(10, 0, 10, 0);
            addView(imageView);
        }
    }

    @Override
    public void onPageSelected(int pageIndex) {
        Observable.range(0, getChildCount())
                .filter(i->{ return i!=pageIndex;})
                .forEach(i->{
                    ImageView imageView = (ImageView) getChildAt(i);
                    imageView.setImageResource(resId);
                });
        ImageView imageView = (ImageView) getChildAt(pageIndex);
        if(imageView!=null) {
            imageView.setImageResource(resIdFocus);
        }
    }

    public void setIndicatorResId(int resId) {
        this.resId = resId;
    }

    public void setIndicatorResIdFocus(int resIdFocus) {
        this.resIdFocus = resIdFocus;
    }

    @Override
    public void onPageUnSelected(int pageIndex) {
        ImageView imageView = (ImageView) getChildAt(pageIndex);
        if(imageView!=null) {
            imageView.setImageResource(resId);
        }
    }

    public void setViewPager(ViewPager viewPager){
        InitIndicatorItems(viewPager.getOffscreenPageLimit());
        viewPager.setCurrentItem(0);
        onPageSelected(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PageIndicatorView.this.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
