package com.warehourse.app.ui.search;

import com.google.gson.reflect.TypeToken;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.warehourse.app.model.ProductModel;
import com.warehourse.app.model.SearchHisModel;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.ProductSearchEntity;
import com.warehourse.app.model.entity.ProductSearchParaEntity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Title: SearchViewModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/23  11:15
 *
 * @author wangwei
 * @version 1.0
 */
public class SearchViewModel extends BaseViewModel {
    private ProductSearchParaEntity productSearchParaEntity;
    private boolean isPlaceHolder;

    public  void setSort(String sort){
        productSearchParaEntity.sort = sort;
    }

    public SearchViewModel(Object activity) {
        super(activity);
        productSearchParaEntity = new ProductSearchParaEntity();
    }



    public void search(Action1<ProductSearchEntity> onNext, Action1<List<ProductEntity>> listAction1,
                       Action1<Boolean> isMore) {
        productSearchParaEntity.lastFlag = "";
        if (!isPlaceHolder&&!TextUtils.isEmpty(productSearchParaEntity.keyword)){
            insertKey(productSearchParaEntity.keyword, b->{});
        }
        submitRequestThrowError(ProductModel.search(productSearchParaEntity).map(r -> {
            if (r.isOk()) {
                if (r.data != null) {
                    productSearchParaEntity.lastFlag = r.data.lastFlag;
                }
                return r.data;
            }
            throw new HttpErrorException(r);
        }),entity -> {
            onNext.call(entity);
            Observable.just(entity.getList()).subscribe(listAction1);
            Observable.just(entity.getList().isEmpty()).subscribe(isMore);
        });
    }

    public void loadMore(Action1<List<ProductEntity>> onNext, Action1<Boolean> isMore) {
        submitRequestThrowError(ProductModel.search(productSearchParaEntity).map(r -> {
            if (r.isOk()) {
                if (r.data != null) {
                    productSearchParaEntity.lastFlag = r.data.lastFlag;
                    return r.data.getList();
                }
                return new ArrayList<ProductEntity>();
            } else throw new HttpErrorException(r);
        }), list -> {

            onNext.call(list);
            Observable.just(list.isEmpty()).subscribe(isMore);
        });
    }

    public void setFields(String jsonString){
        if (!TextUtils.isEmpty(jsonString)){
            List<ProductSearchParaEntity.FieldEntity> list = GsonUtil.fromJson(jsonString,
                    new TypeToken<List<ProductSearchParaEntity.FieldEntity>>() {}.getType());
            productSearchParaEntity.mFieldEntityList = list;
        }
    }

    public void insertKey(String key, Action1<Boolean> onNext){
        submitRequestThrowError(SearchHisModel.addSearchHis(key), onNext);
    }

    public void clearHistory(Action1<Boolean> onNext){
        submitRequestThrowError(SearchHisModel.deleteAllSearchHis(), onNext);
    }

    public void getSearchKeys(Action1<List<String>> onNext){
        submitRequestThrowError(SearchHisModel.getSearchHisData(), onNext);
    }


    public void setCategoryId(int id){
        productSearchParaEntity.categoryId = id;
    }

    public void setKeyword(String keyword,boolean isPlaceHolder){
        this.isPlaceHolder=isPlaceHolder;
        productSearchParaEntity.keyword = keyword;
    }
    public void setKeyword(String keyword){
        this.isPlaceHolder=false;
        productSearchParaEntity.keyword = keyword;
    }
}
