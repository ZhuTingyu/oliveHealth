package com.olive.ui.main.cart;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.biz.util.Lists;
import com.olive.model.CartModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.CartAdapter;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/4.
 */

public class CartViewModel extends BaseViewModel {

    private List<ProductEntity> productEntities;
    private BigDecimal totalPrice;
    private String productNo;
    private int quantity;
    private List<Integer> selectedPosition;
    private CartAdapter adapter;

    public CartViewModel(Object activity) {
        super(activity);
    }

    public void getCartProductList(Action1<List<ProductEntity>> action1) {
        submitRequestThrowError(CartModel.CartProducts().map(r -> {
            if (r.isOk()) {
                productEntities = r.data;
                return productEntities;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void updateProductNumber(Action1<String> action1) {
        submitRequestThrowError(CartModel.CartProductNumberUpdate(productNo, quantity).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void removeCartProducts(Action1<String> action1){
        submitRequestThrowError(CartModel.CartProductRemove(getSelectedProductsNo()).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }


    public void getTotalPrice(Action1<Long> action1) {
        selectedPosition = adapter.getSelectedPotion();
        totalPrice = BigDecimal.valueOf(0);
        for(Integer position : selectedPosition){
            ProductEntity productEntity = productEntities.get(position);
            BigDecimal num = BigDecimal.valueOf(productEntity.quantity);
            BigDecimal price = BigDecimal.valueOf(productEntity.price);
            BigDecimal oneProductTotal = num.multiply(price);
            totalPrice = totalPrice.add(oneProductTotal);
        }
        Observable.just(totalPrice.longValue()).subscribe(action1);
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAdapter(CartAdapter adapter) {
        this.adapter = adapter;
    }

    private List<String> getSelectedProductsNo(){
        selectedPosition = adapter.getSelectedPotion();
        List<String> nos = Lists.newArrayList();
        for(Integer position : selectedPosition){
           nos.add(productEntities.get(position).productNo);
        }
        return nos;
    }

    public List<ProductEntity> getSelectedProducts(){
        selectedPosition = adapter.getSelectedPotion();
        List<ProductEntity> productEntities = Lists.newArrayList();
        for(Integer position : selectedPosition){
            productEntities.add(productEntities.get(position));
        }
        return productEntities;
    }
}
