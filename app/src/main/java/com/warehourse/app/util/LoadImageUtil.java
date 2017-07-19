package com.warehourse.app.util;

import com.biz.image.BaseLoadImageUtil;
import com.biz.image.OssLoadImageTemplateImpl;
import com.biz.image.OssPrivateLoadImageTemplateImpl;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.model.SystemModel;

/**
 * Title: ImageLoadUtil
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/24  13:03
 *
 * @author wangwei
 * @version 1.0
 */
public class LoadImageUtil {
    public static Builder Builder() {
        return new Builder();
    }

    public static class Builder extends BaseLoadImageUtil.Builder {
        public Builder() {
        }

        @Override
        public BaseLoadImageUtil.Builder isPrivate(boolean isPrivate) {
            super.isPrivate(isPrivate);
            if (mLoadImageTemplate != null) return this;
            if (isPrivate) {
                this.mLoadImageTemplate = new OssPrivateLoadImageTemplateImpl(SystemModel.getInstance().getOssPrivateBucketName(), WareApplication.getApplication().getOssTokenEntity());
            } else {
                this.mLoadImageTemplate = new OssLoadImageTemplateImpl(SystemModel.getInstance().getOssPublicBucketName());
            }
            return this;
        }

        @Override
        public BaseLoadImageUtil build() {
            if (url != null && url.indexOf("://") > -1) {
                this.imageLoadUrl = url;
            } else {
                if (headDefault.indexOf("http") > -1) {
                    if (mLoadImageTemplate == null) {
                        mLoadImageTemplate = new OssLoadImageTemplateImpl(SystemModel.getInstance().getOssPublicBucketName());
                    }
                    this.imageLoadUrl = this.mLoadImageTemplate.getImageUrl(url);
                } else {
                    this.imageLoadUrl = headDefault + url;
                }
            }
            return new BaseLoadImageUtil(this);
        }
    }
}
