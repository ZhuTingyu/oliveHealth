package com.warehourse.app.ui.product;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.warehourse.app.model.ProductModel;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.cart.BaseAddCartViewModel;

import rx.functions.Action1;


class ProductDetailViewModel extends BaseAddCartViewModel {
    private String productId;
    private ProductEntity mProductEntity;

    public ProductDetailViewModel(Object activity) {
        super(activity);
        productId=getActivity().getIntent().getStringExtra(IntentBuilder.KEY_ID);
        mProductEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }

    public void detail(Action1<ProductEntity> action0) {
        if (mProductEntity!=null) {
            action0.call(mProductEntity);
        }
        queryCartCount();
        submitRequestThrowError(ProductModel.productDetail(productId).map(r->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action0);
    }


}
