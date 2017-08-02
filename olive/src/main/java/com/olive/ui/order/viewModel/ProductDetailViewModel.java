package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/2.
 */

public class ProductDetailViewModel extends BaseViewModel {

    private String productNo;

    public ProductDetailViewModel(Object activity) {
        super(activity);
        productNo = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE);
    }

    public void getProductDetail(Action1<ProductEntity> action1){
        submitRequestThrowError(ProductsModel.productDetail(productNo).map(r ->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

}
