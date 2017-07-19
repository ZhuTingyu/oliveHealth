package com.biz.base;

import com.biz.util.PhotoUtil;
import com.biz.util.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import rx.functions.Action1;

/**
 * Title: UploadImageViewModel
 * Description:支持图片上传ViewModel
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2016/9/7  18:31
 *
 * @author wangwei
 * @version 1.0
 * @review dengqinsheng 16/9/7
 */
public  class UploadImageViewModel extends BaseViewModel {
    protected Uri mUri;

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public UploadImageViewModel(Object activity) {
        super(activity);
    }
//    protected void uploadImage(String image, Action1<String> action0) {
//        submitRequest(UploadImageUtil.upload(image, InitModel.getInstance().getOssBucketInfo()), url -> {
//            Observable.just(url).subscribe(action0);
//        }, throwable -> {
//            error.onNext(getError(throwable));
//        });
//    }
    public boolean onActivityResult(int requestCode, int resultCode, Intent data,Action1<String> action0) {
        if (resultCode == Activity.RESULT_OK)
        if (requestCode == PhotoUtil.PHOTO_SUCCESS) {
                //List<String> selectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                if (selectPath!=null&&selectPath.size()>0&&!TextUtils.isEmpty(selectPath.get(0))) {
//                    File file = new File(selectPath.get(0));
//                    getActivity().setProgressVisible(true);
//                    uploadImage(file.getAbsolutePath(),action0);
//                } else {
//                    ToastUtils.showShort(getActivity(), getString(R.string.text_toast_retry_choose_image));
//                }

        }else if (requestCode == PhotoUtil.CAMERA_SUCCESS){
                     if (mUri!=null) {
                    getActivity().setProgressVisible(true);
                    //uploadImage(file.getAbsolutePath(),action0);
                } else {
                    //ToastUtils.showShort(getActivity(), getString(R.string.text_toast_retry_choose_image));
                }
        }
        return false;
    }
}
