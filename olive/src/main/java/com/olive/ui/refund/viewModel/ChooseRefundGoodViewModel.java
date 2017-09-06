package com.olive.ui.refund.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.olive.R;
import com.olive.model.ProductsModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.CartAdapter;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/10.
 */

public class ChooseRefundGoodViewModel extends BaseViewModel {

    private CartAdapter adapter;

    private List<ProductEntity> productEntities;

    public ChooseRefundGoodViewModel(Object activity) {
        super(activity);
    }

    public void chooseRefundProductsList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.chooseRefundProductsList().map(r -> {
            if(r.isOk()){
                productEntities = r.data;
                return productEntities;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public List<ProductEntity> getSelectedProducts(){
        List<Integer> selectedPosition = adapter.getSelectedPotion();
        List<ProductEntity> chooseProductEntities = Lists.newArrayList();
        for(Integer position : selectedPosition){
            chooseProductEntities.add(productEntities.get(position));
        }
        return chooseProductEntities;
    }

    public boolean isChangeCount(int position){
        int count = productEntities.get(position).quantity;
        if(count < 0 ){
           return false ;
        }else return true;
    }

    public void countMin(ProductEntity productEntity){
        productEntity.quantity -- ;
        if(productEntity.quantity -- < 1){
            productEntity.quantity = 1;
            getActivity().error(getActivity().getString(R.string.message_products_count_can_not_is_1));
        }
    }

    public void countAdd(ProductEntity productEntity){
        productEntity.quantity ++ ;
    }

    public void setAdapter(CartAdapter adapter) {
        this.adapter = adapter;
    }
}
