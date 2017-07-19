package com.biz.image.upload;

import android.os.Environment;
import android.text.TextUtils;

import com.biz.application.BaseApplication;
import com.biz.http.R;

import java.io.File;

import rx.functions.Func2;
import rx.functions.Func5;

public class ImageCompressHandle implements Func2<String, String, ImageCompressEntity> {
    @Override
    public ImageCompressEntity call(String src, String imageModule) {
        try {
            ImageCompressEntity entity = new ImageCompressEntity();
            entity.src = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + "tempImage.jpg";
            entity.isDisCache = true;
            entity.isDeleteFile = false;
//        entity.degree = ExifInterfaceUtil.readPictureDegree(src);
            entity.degree = 0;
            if (TextUtils.isEmpty(src)) {
                entity.src = null;
                return entity;
            }
            OpenCVUtil.init();
            if (OpenCVUtil.isOriginal(src)) {
                entity.src = src;
                return entity;
            }
            if (OpenCVUtil.disposeImage(src, OpenCVUtil.MAX_LENGTH, entity.src)) {
                entity.isDeleteFile = true;
                return entity;
            }
            entity.isDisCache = true;
            return entity;
        }catch (Exception e){
            throw new TextErrorException(BaseApplication.getAppContext().getString(R.string.text_error_photo_image));
        }
    }
}