package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.biz.util.Lists;
import com.biz.util.StringUtils;
import com.olive.R;
import com.olive.model.ProductsModel;
import com.olive.model.RefundModel;
import com.olive.model.entity.ProductEntity;
import com.olive.model.entity.RefundReasonEntity;
import com.olive.util.UploadImageUtil;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class ApplyRefundViewModel extends BaseViewModel {

    private String fileUri;

    private String[] imgUrls;

    private List<RefundReasonEntity> refundReasonEntities;

    private List<String> refundNameList;

    private int refundReasonId;

    private List<ProductEntity>  chooseRefundProducts;

    private String description;

    public ApplyRefundViewModel(Object activity) {
        super(activity);
        imgUrls = new String[2];
    }

    public void uploadImg(Action1<String> action1){
        submitRequestThrowError(UploadImageUtil.uploadImg(getString(R.string.api_upload_image),fileUri).map(r -> {
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
        submitRequestThrowError(RefundModel.applyRefund(chooseRefundProducts, refundReasonId, getImgUrlsString(), description).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }), action1);
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setImgUrl(int position, String url){
        imgUrls[position] = url;
    }

    private List<String> getImgUrlsString(){
        return Lists.newArrayList(imgUrls);
    }

    public List<String> getRefundNameList(){
        refundNameList = Lists.newArrayList();
        for(RefundReasonEntity refundReasonEntity : refundReasonEntities){
            refundNameList.add(refundReasonEntity.desc);
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
