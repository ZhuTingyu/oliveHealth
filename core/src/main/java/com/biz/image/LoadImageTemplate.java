package com.biz.image;

import com.biz.widget.CustomDraweeView;

/**
 * Title: LoadImageTemplate
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/24  10:57
 *
 * @author wangwei
 * @version 1.0
 */
public interface LoadImageTemplate {
    String  getImageUrl(String url);
    String addImageParaUrl(CustomDraweeView imageView,String url);
    String addImageParaUrl(String url);
    enum ViewScaleType {
        FIT_INSIDE,
        CROP;

        public static ViewScaleType fromImageView(CustomDraweeView imageView) {
            switch (imageView.getScaleType()) {
                case FIT_CENTER:
                case FIT_XY:
                case FIT_START:
                case FIT_END:
                case CENTER_INSIDE:
                    return ViewScaleType.FIT_INSIDE;
                case MATRIX:
                case CENTER:
                case CENTER_CROP:
                default:
                    return ViewScaleType.CROP;
            }
        }
    }
}
