package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.olive.R;
import com.olive.model.CartModel;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.main.home.ProductsViewModel;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/2.
 */

public class ProductDetailViewModel extends ProductsViewModel {

    private int productNumber;
    private long totalPrice;
    public ProductEntity productEntity;
    private List<ProductEntity> relevanceProductList;

    public ProductDetailViewModel(Object activity) {
        super(activity);
        productNo = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_VALUE);
    }

    public void getProductDetail(Action1<ProductEntity> action1) {
        submitRequestThrowError(ProductsModel.productDetail(productNo).map(r -> {
            if (r.isOk()) {
                productEntity = r.data;
                return productEntity;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void getRelevanceProductList(Action1<List<ProductEntity>> action1) {
        submitRequestThrowError(ProductsModel.relevanceProductList(productNo).map(r -> {
            if (r.isOk()) {
                relevanceProductList = r.data;
                return relevanceProductList;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void getCartProductList(Action1<List<ProductEntity>> action1) {
        submitRequestThrowError(CartModel.CartProducts().map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }


    public Action1<String> setProductNumberAndCalculateTotalPrice(Action1<Long> action1) {
        return s -> {
            if(!s.isEmpty()){
                BigDecimal num = BigDecimal.valueOf(Long.parseLong(s));
                BigDecimal price = BigDecimal.valueOf(getPrice());
                BigDecimal total = num.multiply(price);
                totalPrice = total.longValue();
                Observable.just(totalPrice).subscribe(action1);
            }
        };

    }

    public List<ProductEntity> setAddCartCurrentProduct() {
        List<ProductEntity> current = Lists.newArrayList();
        current.add(productEntity);
        addProductList = current;
        return current;
    }

    public void setProductNumberValid(int number, Action1<String> action1) {
        if (number == 0) {
            error.onNext(getErrorString(getString(R.string.message_products_count_can_not_is_0)));
        } else {
            if (number % productEntity.orderCardinality != 0) {
                error.onNext(getErrorString(getString(R.string.message_product_valid_number, productEntity.orderCardinality)));
                productEntity.quantity -= number % productEntity.orderCardinality;
                Observable.just(String.valueOf(productEntity.quantity)).subscribe(action1);
            }
        }


    }

    public void setAddCartRelevanceProductList() {
        addProductList = relevanceProductList;
    }

    public long getPrice() {
        return productEntity.salePrice != 0 ? productEntity.salePrice : productEntity.originalPrice;
    }

    public long getTotalPrice() {
        return new BigDecimal(getPrice()).multiply(new BigDecimal(productEntity.quantity)).longValue();
    }
}
