package com.warehourse.app.ui.preview;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.warehourse.app.R;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.entity.AlipayPayEntity;
import com.warehourse.app.model.entity.OrderPreviewEntity;
import com.warehourse.app.model.entity.OrderPreviewParaEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.VoucherEntity;
import com.warehourse.app.model.entity.WeiXinPayEntity;
import com.warehourse.app.ui.base.BasePayViewModel;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Title: OrderPreviewViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/31  10:42
 *
 * @author wangwei
 * @version 1.0
 */
public class OrderPreviewViewModel extends BasePayViewModel {
    private OrderPreviewParaEntity mOrderPreviewParaEntity;
    private long payAmount=0;
    private VoucherEntity mVoucherEntity;
    private BehaviorSubject<Integer> voucherCount=BehaviorSubject.create();

    public OrderPreviewViewModel(Object activity) {
        super(activity);
        mOrderPreviewParaEntity = new OrderPreviewParaEntity();
        mOrderPreviewParaEntity.items = getActivity().getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
    }

    @Override
    protected Observable<ResponseJson<AlipayPayEntity>> getAlipayPay() {
        return OrderModel.createAlipay(mOrderPreviewParaEntity);
    }

    @Override
    protected Observable<ResponseJson<WeiXinPayEntity>> getWeiXinPay() {
        return OrderModel.createWeiXin(mOrderPreviewParaEntity);
    }

    public Action1<Integer> setPayType() {
        return payType -> {
            mOrderPreviewParaEntity.paymentType = payType;
        };
    }

    public void setInvoice(boolean isCheck, String title) {
        if (mOrderPreviewParaEntity != null) {
            mOrderPreviewParaEntity.invoiceType = isCheck ? OrderPreviewParaEntity.INVOICE_TYPE_COMPANY : OrderPreviewParaEntity.INVOICE_TYPE_NO;
            mOrderPreviewParaEntity.invoiceTitle = title;
        }
    }

    public void setInvoice(boolean isCheck, String title, String number) {
        if (mOrderPreviewParaEntity != null) {
            mOrderPreviewParaEntity.invoiceType = isCheck ?
                    OrderPreviewParaEntity.INVOICE_TYPE_COMPANY : OrderPreviewParaEntity.INVOICE_TYPE_NO;
            mOrderPreviewParaEntity.invoiceTitle = title;
            mOrderPreviewParaEntity.invoiceNumber = number;
        }
    }

//    public void setUsedCoupons(List<String> usedCoupons) {
//        mOrderPreviewParaEntity.usedCoupons = usedCoupons;
//    }

    public void requestPreview(Action1<OrderPreviewEntity> onNext) {
        if(mVoucherEntity!=null){
            List<String> list= Lists.newArrayList();
            for(int i=0;i<mVoucherEntity.quantity;i++){
                list.add(""+mVoucherEntity.voucherTypeId);
            }
            mOrderPreviewParaEntity.usedCoupons=list;
        }else {
            mOrderPreviewParaEntity.usedCoupons=null;
        }
        submitRequestThrowError(OrderModel.preview(mOrderPreviewParaEntity).map(r -> {
            if (r.isOk()) {
                if(r.data!=null) {
                    payAmount = r.data.payAmount;
                }else {
                    payAmount=0l;
                }
                sendVoucherCount();
                return r.data;
            }
            throw new HttpErrorException(r);
        }), onNext);

    }

    public VoucherEntity getVoucherEntity() {
        return mVoucherEntity;
    }
    public ArrayList<ProductEntity> getProductList(){
        return mOrderPreviewParaEntity.items;
    }

    public void submitOrder(Activity activity) {
        if (mOrderPreviewParaEntity == null) return;
        if (mOrderPreviewParaEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_DELIVER
                ||((mOrderPreviewParaEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_ALIPAY
                ||mOrderPreviewParaEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_WEIXIN)&&payAmount<=0)) {
            submitRequestThrowError(OrderModel.createDeliver(mOrderPreviewParaEntity), r -> {
                if (r.isOk()) {
                    payComplete(r.data.getOrderId());
                } else throw new HttpErrorException(r);
            });
        } else if (mOrderPreviewParaEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_ALIPAY) {
            alipayPay(activity);
        } else if (mOrderPreviewParaEntity.paymentType == OrderPreviewParaEntity.TYPE_PAY_WEIXIN) {
            weixinPay();
        } else {
            error.onNext(getErrorString(R.string.text_error_selected_pay));
        }
    }

    public void setVoucherData(Intent intent) {
        if (intent != null) {
            mVoucherEntity = intent.getParcelableExtra(IntentBuilder.KEY_DATA);
        }else {
            mVoucherEntity=null;
        }
        sendVoucherCount();
    }
    private void sendVoucherCount()
    {
        if(mVoucherEntity!=null) {
            voucherCount.onNext(mVoucherEntity.quantity);
        }else {
            voucherCount.onNext(0);
        }
    }

    public BehaviorSubject<Integer> getVoucherCount() {
        return voucherCount;
    }
}
