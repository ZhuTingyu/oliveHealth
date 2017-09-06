package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.olive.R;
import com.olive.model.RefundModel;
import com.olive.model.entity.ProductEntity;
import com.olive.model.entity.RefundReasonEntity;
import com.olive.model.UploadImageModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class ApplyRefundViewModel extends BaseViewModel {

    private String fileUri;

    public List<String> imgUrls;

    private List<RefundReasonEntity> refundReasonEntities;

    private List<String> refundNameList;

    private int refundReasonId;

    private List<ProductEntity>  chooseRefundProducts;

    private String description;

    public ApplyRefundViewModel(Object activity) {
        super(activity);
        imgUrls = Lists.newArrayList();
    }

    public void uploadImg(Action1<String> action1){
        submitRequestThrowError(UploadImageModel.uploadImg(getActivity().getBaseContext() ,getString(R.string.api_upload_image),fileUri).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void getRefundReason(Action1<List<RefundReasonEntity>> action1){
        submitRequestThrowError(RefundModel.applyRefundReasons().map(r -> {
            if(r.isOk()){
                refundReasonEntities = r.data;
                return refundReasonEntities;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    public void applyRefund(Action1<String> action1){

        if(chooseRefundProducts.isEmpty()){
            error.onNext(getErrorString(R.string.message_choose_refund_goods));
            return;
        }

        if(refundReasonId == 0){
            error.onNext(getErrorString(R.string.message_choose_refund_goods_reason));
            return;
        }

        if(imgUrls.isEmpty()){
            error.onNext(getErrorString(R.string.message_choose_refund_goods_photo));
            return;
        }



        submitRequestThrowError(RefundModel.applyRefund(chooseRefundProducts, refundReasonId, getImgString(), description).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    private String getImgString() {
        StringBuffer sb = new StringBuffer();
        for(String url : imgUrls){
            sb.append(url + ",");
        }
        return sb.toString();
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setImgUrl(int position, String url){
    }

    public List<String> getRefundNameList(){
        refundNameList = Lists.newArrayList();
        for(RefundReasonEntity refundReasonEntity : refundReasonEntities){
            refundNameList.add(refundReasonEntity.reason);
        }
        return refundNameList;
    }

    public void setRefundReasonId(int refundReasonId) {
        this.refundReasonId = refundReasonId;
    }

    public void setChooseRefundProduct(List<ProductEntity> chooseRefundProducts) {
        this.chooseRefundProducts = chooseRefundProducts;
    }

    public Action1<String> setDescription() {
        return s -> {
            this.description = s;
        };
    }
}
