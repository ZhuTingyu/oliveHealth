package com.biz.image.upload;

import com.biz.http.R;
import com.biz.http.application.HttpApplication;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.drawee.backends.pipeline.Fresco;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import rx.Observable;

/**
 * Created by wangwei on 2016/3/24.
 */
public class UploadImageUtil {
    public static Observable<String> upload(String src, String bucketName) {
        return Observable.create(subscriber -> {
            Observable.combineLatest(Observable.just(src),
                    Observable.just(bucketName),
                    new ImageCompressHandle()).subscribe(
                    imageUploadEntity -> {
                        System.gc();
                        if (TextUtils.isEmpty(imageUploadEntity.src)) {
                            subscriber.onError(new TextErrorException(HttpApplication.getAppContext().getString(R.string.text_upload_image_handle_error)));
                            return;
                        }
                        ExifInterfaceUtil.SavePictureDegree(imageUploadEntity.src, imageUploadEntity.degree);
                        uploadImage(bucketName, imageUploadEntity.degree, imageUploadEntity.src, new UploadObserver() {
                            @Override
                            public void onCompleted(String name) {
                                deleteFile(imageUploadEntity, name, bucketName);
                                subscriber.onNext(name);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(String throwable) {
                                subscriber.onError(new TextErrorException(throwable));
                            }

                            @Override
                            public void onNext(int t) {

                            }
                        });
                    }, throwable -> {
                        subscriber.onError(throwable);
                    }
            );
        });
    }

    private static void deleteFile(ImageCompressEntity imageUploadEntity, String uploadImage, String type) {
        if (imageUploadEntity != null && !TextUtils.isEmpty(imageUploadEntity.src)) {
            if (!TextUtils.isEmpty(uploadImage)) {
                //上传成功 加入缓存
                //保存旋转角度
                if (imageUploadEntity.isDisCache) {
                    if (imageUploadEntity.degree != 0) {
                        ExifInterfaceUtil.SavePictureDegree(imageUploadEntity.src, imageUploadEntity.degree);
                    }
                    try {
                        final FileInputStream fileInputStream = new FileInputStream(imageUploadEntity.src);
                        ;
                        SimpleCacheKey simpleCacheKey = new SimpleCacheKey(getUriImage(uploadImage, type));
                        Fresco.getImagePipelineFactory().getMainDiskStorageCache().insert(simpleCacheKey, new WriterCallback() {
                            @Override
                            public void write(OutputStream outputStream) throws IOException {
                                byte[] buffer = new byte[1024];
                                int byteread = 0;
                                while ((byteread = fileInputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, byteread);
                                }
                                fileInputStream.close();
                            }
                        });
                    } catch (IOException e) {
                    }
                }
            }
            if (imageUploadEntity.DeleteFile) {
                //删除生成的图片
                File file = new File(imageUploadEntity.src);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public static String getUriImage(String url, String bucketName) {
        if (url != null && url.indexOf("http://") > -1) {
            return url;
        } else {
            OssManager ossManager = new OssManager(HttpApplication.getAppContext(),bucketName);
            return ossManager.getPublicURL(url);
        }
    }

    private static void uploadImage(String type, int rotate, String filename, UploadObserver observable) {
        File file = new File(filename);
        if (!file.exists()) {
            if (observable != null) {
                observable.onError(HttpApplication.getAppContext().getString(R.string.text_upload_image_file_not_exists));
            }
            return;
        }
        OssManager ossManager = new OssManager(HttpApplication.getAppContext(),type);
        ossManager.uploadImage(type, rotate, filename, observable);
    }
}
