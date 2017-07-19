package com.warehourse.app.ui.category;

import com.google.gson.reflect.TypeToken;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.http.ResponseJson;
import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.warehourse.app.R;
import com.warehourse.app.model.ProductModel;
import com.warehourse.app.model.entity.ProductBrandEntity;
import com.warehourse.app.model.entity.ProductBrandEntity.BrandItemEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.ProductFilterEntity;
import com.warehourse.app.model.entity.ProductFilterItemEntity;
import com.warehourse.app.model.entity.ProductSearchEntity;
import com.warehourse.app.model.entity.ProductSearchParaEntity;
import com.warehourse.app.model.entity.ProductSearchParaEntity.FieldEntity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class CategoryChildViewModel extends BaseViewModel {
    private ProductSearchParaEntity productSearchParaEntity;
    private String postUrl = "";
    private List<ProductFilterEntity> fields;
    private ProductFilterEntity mBrandEntity;
    private ProductFilterItemEntity mBrandItemEntity;

    public CategoryChildViewModel(Object activity) {
        super(activity);
        postUrl = getString(R.string.api_product_search);
        productSearchParaEntity = new ProductSearchParaEntity();
        fields = Lists.newArrayList();
        mBrandEntity = new ProductFilterEntity();
        mBrandEntity.filterItems = Lists.newArrayList();
        mBrandItemEntity = new ProductFilterItemEntity();
        mBrandItemEntity.value="";
        mBrandEntity.filterItems.add(mBrandItemEntity);
        fields.add(mBrandEntity);
        productSearchParaEntity.fields = fields;
    }

    public void setSort(String sort){
        productSearchParaEntity.sort= sort;
    }

    public  String getSort(){
        return productSearchParaEntity.sort;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public void search(Action1<ProductSearchEntity> onNext, Action1<List<ProductEntity>> listAction1, Action1<Boolean> isMore) {
        subscription.clear();
        productSearchParaEntity.lastFlag = "";
        submitRequestThrowError(ProductModel.search(postUrl, productSearchParaEntity).map(r -> {
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
            mBrandEntity.field = entity.getBrandsField();
        });
    }

    public void loadMore(Action1<List<ProductEntity>> onNext, Action1<Boolean> isMore) {
        subscription.clear();
        submitRequestThrowError(ProductModel.search(postUrl, productSearchParaEntity).map(r -> {
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

    public void clearFilter(){
        fields.clear();
    }

    public int getCategoryId(){
        return productSearchParaEntity.categoryId;
    }
    public void setCategoryId(int id) {
        productSearchParaEntity.categoryId = id;
    }

    public void setFilterItems(List<ProductFilterEntity> list){
        fields .clear();
        fields.add(mBrandEntity);
        fields.addAll(list);
    }

    public void setBrandEntity(BrandItemEntity entity) {
        fields.clear();
        mBrandItemEntity.label = entity.label;
        mBrandItemEntity.value = entity.value;
        mBrandItemEntity.isSelected = true;
        mBrandEntity.filterItems = Lists.newArrayList();
        mBrandEntity.field = "brandId";
        mBrandEntity.filterItems.add(mBrandItemEntity);
        fields.add(mBrandEntity);
    }



    public void setKeyword(String keyword) {
        productSearchParaEntity.keyword = keyword;
    }
}
