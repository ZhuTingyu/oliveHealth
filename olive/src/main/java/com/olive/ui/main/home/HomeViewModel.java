package com.olive.ui.main.home;

import com.biz.base.BaseViewModel;
import com.biz.http.HttpErrorException;
import com.biz.util.Lists;
import com.olive.model.CartModel;
import com.olive.model.CategoryModel;
import com.olive.model.NoticeModel;
import com.olive.model.ProductsModel;
import com.olive.model.entity.AdvertEntity;
import com.olive.model.entity.CategoryEntity;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.holder.ProductViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/1.
 */

public class HomeViewModel extends ProductsViewModel {

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_NOTICE = 1;

    public static final int TYPE_CATEGORY_HOME = 0;


    private ArrayList<String> imageList;

    public List<AdvertEntity> advertEntityList;

    public HomeViewModel(Object activity) {
        super(activity);
    }

    public void getAvertList(Action1<List<AdvertEntity>> action1){
        submitRequestThrowError(NoticeModel.advertList().map(r -> {
            if(r.isOk()){
                advertEntityList= r.data;
                setImageList(advertEntityList);
                return advertEntityList;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public void getRecommendProductList(Action1<List<ProductEntity>> action1){
        submitRequestThrowError(ProductsModel.recommendProductList().map(r ->{
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }


    public void getCategoryList(Action1<List<CategoryEntity>> action1){
        submitRequestThrowError(CategoryModel.categroyList(TYPE_CATEGORY_HOME).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw  new HttpErrorException(r);
        }),action1);
    }

    private void setImageList(List<AdvertEntity> list) {
        imageList = Lists.newArrayList();
        for(AdvertEntity entity : list){
           imageList.add(entity.image);
        }
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public boolean isGoodsAdvert(int position){
        return advertEntityList.get(position).type == HomeViewModel.TYPE_GOODS;
    }
}
