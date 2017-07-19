package com.biz.image.upload;

/**
 * Created by ThinkPad on 2015/3/11.
 */
public interface UploadObserver {
    void onCompleted(String name);
    void onError(String throwable);
    void onNext(int t);
}
