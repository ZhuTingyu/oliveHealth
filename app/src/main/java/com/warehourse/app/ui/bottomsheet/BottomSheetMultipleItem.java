package com.warehourse.app.ui.bottomsheet;

import com.biz.util.Lists;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;

import java.util.List;

import rx.Observable;

public class BottomSheetMultipleItem implements MultiItemEntity {

    public static final int HEADER = 10;
    public static final int GALLERY = 12;
    public static final int CAMERA = 11;
    public static final int CANCEL = 13;

    private int itemType;

    public String item;

    public BottomSheetMultipleItem(String item){
        this.item = item;
    }


    public BottomSheetMultipleItem(int itemType, String item){
        this.itemType = itemType;
        this.item = item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static List<BottomSheetMultipleItem> getList(){
        String[] settings = WareApplication.getAppContext()
                .getResources().getStringArray(R.array.array_photo);
        List<BottomSheetMultipleItem> list = Lists.newArrayList();
        Observable.range(0, settings.length)
                .map(i->{return  new BottomSheetMultipleItem(CAMERA+i, settings[i]);})
                .toList().subscribe(list::addAll);
        return list;
    }

    public static List<BottomSheetMultipleItem> getListWithHeader(){
        String[] settings = WareApplication.getAppContext()
                .getResources().getStringArray(R.array.array_photo_1);
        List<BottomSheetMultipleItem> list = Lists.newArrayList();
        Observable.range(0, settings.length).map(i->{return  new BottomSheetMultipleItem(HEADER+i,settings[i]);})
                .toList().subscribe(list::addAll);
        return list;
    }
}