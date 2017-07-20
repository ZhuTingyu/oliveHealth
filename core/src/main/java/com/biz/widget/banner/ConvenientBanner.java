package com.biz.widget.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;


public class ConvenientBanner extends com.bigkoo.convenientbanner.ConvenientBanner {

    public ConvenientBanner(Context context) {
        super(context);

    }

    public ConvenientBanner(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ConvenientBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConvenientBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

}
