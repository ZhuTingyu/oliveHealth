package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.olive.R;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;
import com.olive.util.UploadImageUtil;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class ApplyRefundViewModel extends BaseViewModel {

    private String fileUri;

    public ApplyRefundViewModel(Object activity) {
        super(activity);
    }

    public void uploadImg(Action1<String> action1){
        submitRequestThrowError(UploadImageUtil.uploadImg(getString(R.string.api_upload_image),fileUri).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }
}
