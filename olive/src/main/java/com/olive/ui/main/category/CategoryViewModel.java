package com.olive.ui.main.category;

import com.biz.http.HttpErrorException;
import com.olive.model.CategoryModel;
import com.olive.model.ProductsModel;
import com.olive.model.entity.CategoryEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseLoadMoreViewModel;
import com.olive.ui.main.home.ProductsViewModel;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/3.
 */

public class CategoryViewModel extends ProductsViewModel {

    private static final int TYPE_ALL_CATEGORY = 0;
    private String keyWord;
    private String categoryCode;

    public CategoryViewModel(Object activity) {
        super(activity);
    }

    @Override
    public void setLoadMore(Action1<Object> action1) {
        page++;
        getProductList(productEntities -> {
            loadMore(productEntities,action1);
        });
    }

    public void getCategory(Action1<List<CategoryEntity>> action1){
        submitRequestThrowError(CategoryModel.categroyList(TYPE_ALL_CATEGORY).map(r -> {
            if(r.isOk()){
                categoryCode = r.data.get(0).code;
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void getProductList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.productList(keyWord, categoryCode, page, 0, 0).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public Action1<String> setKeyWord() {
        return s -> {
            keyWord = s;
        };
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
