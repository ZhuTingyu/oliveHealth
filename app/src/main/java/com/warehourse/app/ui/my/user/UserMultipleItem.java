package com.warehourse.app.ui.my.user;

import com.biz.util.Lists;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.warehourse.app.R;
import com.warehourse.app.application.WareApplication;
import com.warehourse.app.model.UserModel;

import java.util.List;

import rx.Observable;

class UserMultipleItem implements MultiItemEntity {

    public static final int AVATAR = 10;
    public static final int USERNAME = 12;
    public static final int PHONE = 14;
    public static final int DELIVERY_NAME =16;
    public static final int DELIVERY_ADDRESS = 18;
    public static final int PASSWORD = 20;

    private int itemType;

    public String item;
    public String text;

    UserMultipleItem(int itemType, String item){
        this.itemType = itemType;
        this.item = item;
    }

    UserMultipleItem(int itemType, String item, String text){
        this.itemType = itemType;
        this.item = item;
        this.text = text;
    }


    UserMultipleItem(String item){
        this.item = item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public static List<UserMultipleItem> getList(){
        String[] settings = WareApplication.getAppContext()
                .getResources().getStringArray(R.array.array_personal_settings);
        List<UserMultipleItem> list = Lists.newArrayList();
        list.add(new UserMultipleItem(AVATAR, settings[0]));
        list.add(new UserMultipleItem(USERNAME, settings[1], UserModel.getInstance().getName() ));
        list.add(new UserMultipleItem(PHONE, settings[2],UserModel.getInstance().getUserEntity().mobile));
        list.add(new UserMultipleItem(DELIVERY_NAME, settings[3],UserModel.getInstance().getUserEntity().deliveryName));
        list.add(new UserMultipleItem(DELIVERY_ADDRESS, settings[4],WareApplication.getAppContext().getString(R.string.text_click)));
        list.add(new UserMultipleItem(PASSWORD, settings[5],""));

//        Observable.range(0, settings.length)
//                .map(i->{return  new UserMultipleItem(settings[i]);})
//                .toList().subscribe(list::addAll);
        return list;
    }
}