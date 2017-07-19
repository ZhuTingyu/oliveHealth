package com.warehourse.app.ui.order.detail;

import com.biz.base.UploadImageViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IdsUtil;
import com.biz.util.IntentBuilder;
import com.warehourse.app.R;
import com.warehourse.app.model.OrderModel;
import com.warehourse.app.model.SystemModel;
import com.warehourse.app.model.entity.OrderApplyReturnEntity;
import com.warehourse.app.ui.base.BaseUploadImageViewModel;

import android.text.TextUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by wangwei on 2016/5/27.
 */
public class OrderApplyRefundViewModel extends BaseUploadImageViewModel {
    public static final int TYPE_PRODUCT_RETURN = 1;
    public static final int TYPE_PRODUCT_CHANGE = 2;
    public static final int REASON_ADVENT = 1;
    public static final int REASON_DAMAGED = 2;
    public static final int REASON_PROBLEM = 3;
    private OrderApplyReturnEntity mOrderApplyReturnEntity;

    public OrderApplyRefundViewModel(Object activity) {
        super(activity);
        mOrderApplyReturnEntity = new OrderApplyReturnEntity();
        mOrderApplyReturnEntity.orderId = getActivity().getActivity().getIntent().getStringExtra(IntentBuilder.KEY_ID);
    }

    public Action1<Integer> setType() {
        return t -> {
            mOrderApplyReturnEntity.type = t;
        };
    }

    public Action1<Integer> setReason() {
        return t -> {
            mOrderApplyReturnEntity.cause = t;
        };
    }

    public Action1<String> setDescription() {
        return t -> {
            mOrderApplyReturnEntity.description = t;
        };
    }

    public Action1<List<String>> setImage() {
        return t -> {
            mOrderApplyReturnEntity.images = t;
        };
    }

    public void submitRequest(Action1<Boolean> onNext) {
        if (mOrderApplyReturnEntity.type <= 0) {
            error.onNext(getErrorString(R.string.error_not_selected_type));
            return;
        }
        if (mOrderApplyReturnEntity.cause <=0) {
            error.onNext(getErrorString(R.string.error_not_selected_reason));
            return;
        }
        if (mOrderApplyReturnEntity.images==null||mOrderApplyReturnEntity.images.size()==0) {
            error.onNext(getErrorString(R.string.error_not_upload_image));
            return;
        }
        submitRequest(OrderModel.applyReturn(mOrderApplyReturnEntity), r -> {
            if (r.isOk()) {
                Observable.just(true).subscribe(onNext);
            } else throw new HttpErrorException(r);
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    public void upload(String src, Action1<String> onNext) {
        super.upload(SystemModel.getInstance().getOssPublicBucketName(), src, onNext);
    }
}
