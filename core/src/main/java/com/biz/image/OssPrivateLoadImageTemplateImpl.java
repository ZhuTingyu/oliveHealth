package com.biz.image;

import com.biz.application.BaseApplication;
import com.biz.image.upload.OssManager;
import com.biz.image.upload.OssTokenEntity;
import com.biz.widget.CustomDraweeView;

/**
 * Title: OssPrivateLoadImageTemplateImpl
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/24  11:09
 *
 * @author wangwei
 * @version 1.0
 */
public class OssPrivateLoadImageTemplateImpl extends OssLoadImageTemplateImpl {
    private OssTokenEntity mOssTokenEntity;
    public OssPrivateLoadImageTemplateImpl(String bucketName,OssTokenEntity ossTokenEntity) {
        super(bucketName);
        this.mOssTokenEntity=ossTokenEntity;
        if(mOssTokenEntity==null){
            mOssTokenEntity=new OssTokenEntity();
        }
    }

    @Override
    public String getImageUrl(String url) {
        OssManager ossManager = new OssManager(BaseApplication.getAppContext(),bucketName,mOssTokenEntity.accessKeyId,mOssTokenEntity.accessKeySecret,mOssTokenEntity.securityToken);
        return ossManager.getPrivateURL(url);
    }

    @Override
    public String addImageParaUrl(CustomDraweeView imageView, String url) {
        return url;
    }
}
