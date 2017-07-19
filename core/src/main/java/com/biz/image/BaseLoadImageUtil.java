package com.biz.image;

import com.biz.application.BaseApplication;
import com.biz.util.UrlUtils;
import com.biz.widget.CustomDraweeView;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;

import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.net.URLEncoder;

/**
 * Title: ImageUtil
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/24  11:12
 *
 * @author wangwei
 * @version 1.0
 */
public class BaseLoadImageUtil {
    private int loadingId;
    private int failId;
    private int roundedSize;
    private Builder builder;

    public BaseLoadImageUtil(Builder builder) {
        this.builder = builder;
    }
    public static Builder Builder() {
        return new Builder();
    }

    public BaseLoadImageUtil imageOptionsWithDisplayer(int loadingId, int failID, int size) {
        //显示图片的配置
        this.loadingId = loadingId;
        this.failId = failID;
        this.roundedSize = size;
        return this;
    }

    public BaseLoadImageUtil imageOptions(int loadingId, int failID) {
        this.loadingId = loadingId;
        this.failId = failID;
        return this;
    }

    public BaseLoadImageUtil imageOptions(int loadingId) {
        this.loadingId = loadingId;
        return this;
    }


    public void displayImage(CustomDraweeView imageView) {
        if (imageView == null || builder == null) return;
        if (loadingId > 0) {
            imageView.setPlaceholderId(loadingId);
            imageView.setFailureImageId(loadingId);
        }
        if (failId > 0) {
            imageView.setFailureImageId(failId);
        }
        if (roundedSize > 0) {
            imageView.setRoundedCornerRadius(roundedSize);
        }
        if (loadingId > 0 && !TextUtils.isEmpty(builder.headDefault)
                && builder.headDefault.indexOf("http") > -1
                && TextUtils.isEmpty(builder.url)) {
            BaseLoadImageUtil.Builder().load(loadingId).drawable().build().imageOptions(loadingId).displayImage(imageView);
            return;
        }
        String _imageLoadUri = builder.getImageLoadUrl();
        if (TextUtils.isEmpty(_imageLoadUri)) {
            return;
        }
        //查看是否有原图
        SimpleCacheKey simpleCacheKey = new SimpleCacheKey(_imageLoadUri);
        if (!Fresco.getImagePipelineFactory().getMainFileCache().probe(simpleCacheKey)) {
            //拼接切图大小
            if (_imageLoadUri != null && _imageLoadUri.indexOf(builder.HEAD_HTTP) > -1 && imageView != null) {
                if (imageView != null) {
                    if (builder.getLoadImageTemplate() != null) {
                        if(!builder.isOrigin) {
                            _imageLoadUri = builder.getLoadImageTemplate().addImageParaUrl(imageView, _imageLoadUri);
                        }else {
                            _imageLoadUri = builder.getLoadImageTemplate().addImageParaUrl(_imageLoadUri);
                        }
                    }
                }
            }
        }
        imageView.setImageURI(builder.getEncodeUri(_imageLoadUri));
    }
    public Uri displayImageUri(CustomDraweeView imageView) {
        if (imageView == null || builder == null) return null;
        String _imageLoadUri = builder.getImageLoadUrl();
        if (TextUtils.isEmpty(_imageLoadUri)) {
            return null;
        }
        //查看是否有原图
        SimpleCacheKey simpleCacheKey = new SimpleCacheKey(_imageLoadUri);
        if (!Fresco.getImagePipelineFactory().getMainFileCache().probe(simpleCacheKey)) {
            //拼接切图大小
            if (_imageLoadUri != null && _imageLoadUri.indexOf(builder.HEAD_HTTP) > -1 && imageView != null) {
                if (imageView != null) {
                    if (builder.getLoadImageTemplate() != null) {
                        if(!builder.isOrigin) {
                            _imageLoadUri = builder.getLoadImageTemplate().addImageParaUrl(imageView, _imageLoadUri);
                        }else {
                            _imageLoadUri = builder.getLoadImageTemplate().addImageParaUrl(_imageLoadUri);
                        }
                    }
                }
            }
        }
        return builder.getEncodeUri(_imageLoadUri);
    }

    public static class Builder {
        public final static String HEAD_FILE = "file://";
        public String HEAD_HTTP = "http://";
        public final static String HEAD_CONTENT = "content://";
        public final static String HEAD_ASSETS = "asset://" + BaseApplication.getAppContext().getPackageName() + "/";
        public final static String HEAD_DRAWABLE = "res://" + BaseApplication.getAppContext().getPackageName() + "/";
        protected String headDefault = HEAD_HTTP;
        protected String url = "";
        private boolean isDefault = true;
        public String imageLoadUrl = "";
        protected LoadImageTemplate mLoadImageTemplate;
        private boolean isPrivate;
        private boolean isOrigin=false;
        public Builder() {
        }

        public LoadImageTemplate getLoadImageTemplate() {
            return mLoadImageTemplate;
        }

        public void setLoadImageTemplate(LoadImageTemplate loadImageTemplate) {
            mLoadImageTemplate = loadImageTemplate;
        }

        public Builder setHead(String head) {
            this.HEAD_HTTP = UrlUtils.addEndSeparator(head);
            return this;
        }

        public Builder loadFile(String fileName) {
            if (isDefault) {
                headDefault = HEAD_FILE;
            }
            if (fileName != null) {
                url = fileName;
            }
            return this;
        }

        public Builder loadAssets(String fileName) {
            if (isDefault) {
                headDefault = HEAD_ASSETS;
            }
            url = fileName;
            return this;
        }

        public Builder load(File file) {
            if (isDefault) {
                headDefault = HEAD_FILE;
            }
            if (file != null) {
                url = file.getAbsolutePath();
            }
            return this;
        }

        public Builder load(String url) {
            if (isDefault) {
                headDefault = HEAD_HTTP;
            }
            this.url = url;
            return http();
        }

        public Builder load(Integer drawable) {
            if (isDefault) {
                headDefault = HEAD_DRAWABLE;
            }
            this.url = "" + drawable;
            return this;
        }

        public Builder isPrivate(boolean isPrivate) {
            this.isPrivate=isPrivate;
            return this;
        }

        public Builder origin(boolean origin) {
            this.isOrigin=origin;
            return this;
        }

        public Builder load(Uri uri) {

            if (uri != null) {
                if (uri.getScheme().toString().compareTo("content") == 0) {
                    if (isDefault) {
                        headDefault = HEAD_CONTENT;
                    }
                    this.url = uri.getPath();
                } else if (uri.getScheme().compareTo("file") == 0) {
                    if (isDefault) {
                        headDefault = HEAD_FILE;
                    }
                    this.url = uri.getPath();
                }
            }
            return this;
        }

        public Builder assets() {
            headDefault = HEAD_ASSETS;
            isDefault = false;
            return this;
        }

        public Builder content() {
            headDefault = HEAD_CONTENT;
            isDefault = false;
            return this;
        }

        public Builder file() {
            headDefault = HEAD_FILE;
            isDefault = false;
            return this;
        }

        public Builder http() {
            headDefault = HEAD_HTTP;
            isDefault = false;
            return this;
        }

        public Builder drawable() {
            headDefault = HEAD_DRAWABLE;
            isDefault = false;
            return this;
        }

        public String getImageLoadUrl() {
            if (TextUtils.isEmpty(imageLoadUrl)) {
                build();
            }
            return this.imageLoadUrl;
        }

        public Uri getImageLoadUri() {
            return getEncodeUri(getImageLoadUrl());
        }

        private Uri getEncodeUri(String string) {
            Uri uri = null;
            if (string.indexOf(HEAD_FILE) <= -1) {
                uri = Uri.parse(string);
            } else {
                try {
                    uri = Uri.parse(string.substring(0, string.indexOf("://") + 3)
                            + URLEncoder.encode(
                            string.substring(string.indexOf("://") + 3), "utf-8").replace("%2F", "/"));
                } catch (Exception e) {
                    uri = Uri.parse(string);
                }
            }
            return uri;
        }

        public BaseLoadImageUtil build() {
            if (url != null && url.indexOf("://") > -1) {
                this.imageLoadUrl = url;
            } else {
                if (mLoadImageTemplate != null && headDefault.indexOf("http") > -1) {
                    this.imageLoadUrl=this.mLoadImageTemplate.getImageUrl(url);
                } else {
                    this.imageLoadUrl = headDefault + url;
                }
            }
            return new BaseLoadImageUtil(this);
        }
    }
}
