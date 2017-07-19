package com.biz.image;

import com.biz.application.BaseApplication;
import com.biz.image.upload.OssManager;
import com.biz.util.LogUtil;
import com.biz.widget.CustomDraweeView;

/**
 * Title: OssLoadImageTemplateImpl
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/24  11:02
 *
 * @author wangwei
 * @version 1.0
 */
public class OssLoadImageTemplateImpl implements LoadImageTemplate {
    protected String bucketName;
    public OssLoadImageTemplateImpl(String bucketName) {
        this.bucketName=bucketName;
    }

    @Override
    public String getImageUrl(String url) {
        OssManager ossManager=new OssManager(BaseApplication.getAppContext(),bucketName);
        return ossManager.getPublicURL(url);
    }

    @Override
    public String addImageParaUrl(CustomDraweeView imageView, String url) {
        if (imageView == null) {
            return url;
        }
        String imageUrl=url;
        if (ViewScaleType.fromImageView(imageView) == ViewScaleType.FIT_INSIDE) {
            int w = imageView.getLayoutHeight() > imageView.getLayoutWidth() ?
                    imageView.getLayoutHeight() : imageView.getLayoutWidth();
            String ch = imageView.getLayoutHeight() > imageView.getLayoutWidth() ?
                    "h" : "w";
            if (w > 0) {
                imageUrl += "@2o_0l_" + w + ch + "_80q";
            }
        } else if (ViewScaleType.fromImageView(imageView) == ViewScaleType.CROP) {
            //等比
            if (imageView.getLayoutWidth() > 0 && imageView.getLayoutHeight() > 0) {
                //@1e_0o_0l_400h_150w_90q
                imageUrl += "@1e_2o_0l_" + imageView.getLayoutHeight() + "h_"
                        + imageView.getLayoutWidth() + "w_80q";
            }
        }
        LogUtil.print(imageUrl);
        return imageUrl;
    }

    @Override
    public String addImageParaUrl(String url) {
        return url;
    }
}
