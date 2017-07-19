package com.biz.widget.banner;

//分页指示器
    public interface PageIndicator {

        void InitIndicatorItems(int itemsNumber);

        void onPageSelected(int pageIndex);

        void onPageUnSelected(int pageIndex);
    }
