package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.util.IntentBuilder;
import com.olive.model.entity.ProductEntity;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/8/6.
 */

public class CheckInfoViewModel extends BaseViewModel {

    private List<ProductEntity> productEntities;
    private long totalPrice;

    public CheckInfoViewModel(Object activity) {
        super(activity);
        productEntities = getActivity().getIntent().getParcelableArrayListExtra(IntentBuilder.KEY_DATA);
        totalPrice = getActivity().getIntent().getLongExtra(IntentBuilder.KEY_VALUE,0);
    }

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public int getTotalCount(){
        int count = 0;
        for(ProductEntity productEntity : productEntities){
            count += productEntity.quantity;
        }
        return count;
    }

    public long getTotalPrice() {
        return totalPrice;
    }
}
