package com.olive.ui.main.my.address;

import android.text.TextUtils;

import com.biz.http.HttpErrorException;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.ValidUtil;
import com.olive.R;
import com.olive.model.AddressModel;
import com.olive.model.CityModel;
import com.olive.model.entity.AddressEntity;
import com.olive.model.entity.CityEntity;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */

public class EditAddressViewModel extends AddressViewModel {

    public static final int TYPE_PROVINCE = 0;
    public static final int TYPE_CITY = 1;
    public static final int TYPE_COUNTY = 2;

    public int id;//地址ID
    public String consignee;   //收货人
    public String mobile;    //手机号
    public String area;
    public String detailAddress;   //详情地址

    private String code;

    public int positionProvince = 0;
    public int positionCity = 0;
    public int positionCounty = 0;

    public List<CityEntity> provinceList;
    public List<CityEntity> cityList;
    public List<CityEntity> countyList;

    private AddressEntity editAddressEntity;

    private final BehaviorSubject<Boolean> isValid = BehaviorSubject.create();

    public EditAddressViewModel(Object activity) {
        super(activity);
        addressEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
    }


    public void getCityList(int type, Action1<List<String>> action1) {
        submitRequestThrowError(CityModel.cityList(code).map(r -> {
            if (r.isOk()) {
                List<CityEntity> entities = r.data;
                if (TYPE_PROVINCE == type) {
                    provinceList = entities;
                } else if (TYPE_CITY == type) {
                    cityList = entities;
                } else if (TYPE_COUNTY == type) {
                    countyList = entities;
                }
                return getCitiesName(entities);
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void addAddress(Action1<AddressEntity> action1) {
        checkAddressInfo();
        submitRequestThrowError(AddressModel.AddressAdd(getNewAddress()).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void updateAddress(Action1<AddressEntity> action1){
        checkAddressInfo();
        submitRequestThrowError(AddressModel.AddressUpdate(getUpdateAddress()).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),action1);
    }

    public AddressEntity getNewAddress() {
        AddressEntity entity = new AddressEntity();
        entity.consignee = consignee;
        entity.mobile = mobile;
        entity.province = provinceList != null ? getCurrentProvince().code : editAddressEntity.province;
        entity.city = cityList != null ? getCurrentCity().code : editAddressEntity.city;
        entity.district = countyList != null ? getCurrentCounty().code : editAddressEntity.district;
        entity.detailAddress = detailAddress;
        return entity;
    }

    private AddressEntity getUpdateAddress(){
        AddressEntity entity = getNewAddress();
        entity.id = editAddressEntity.id;
        entity.isDefault = editAddressEntity.isDefault;
        return entity;
    }

    private List<String> getCitiesName(List<CityEntity> cityEntities) {
        List<String> nameList = Lists.newArrayList();
        for (CityEntity cityEntity : cityEntities) {
            nameList.add(cityEntity.name);
        }
        return nameList;
    }

    public String getAreaString() {
        return getCurrentProvince().name + " "
                + getCurrentCity().name + " "
                + getCurrentCounty().name;
    }

    public void checkAddressInfo(){
        if(!ValidUtil.phoneNumberValid(mobile)){
            error.onNext(getErrorString(R.string.message_input_valid_mobile));
            return;
        }
    }

    private CityEntity getCurrentProvince() {
        return provinceList.get(positionProvince);
    }

    private CityEntity getCurrentCity() {
        return cityList.get(positionCity);
    }

    private CityEntity getCurrentCounty() {
        return countyList.get(positionCounty);
    }

    public void cleanAreaData() {
        if (provinceList != null) {
            provinceList.clear();
        }
        if (cityList != null) {
            cityList.clear();
        }
        if (countyList != null) {
            countyList.clear();
        }

        code = null;

    }


    public Action1<String> setConsignee() {
        return s -> {
            this.consignee = s;
            setInfoValid();
        };
    }

    public Action1<String> setMobile() {
        return s -> {
            this.mobile = s;
            setInfoValid();
        };
    }

    public Action1<String> setArea() {
        return s -> {
            area = s;
            setInfoValid();
        };
    }

    public Action1<String> setDetailAddress() {
        return s -> {
            this.detailAddress = s;
            setInfoValid();
        };
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEditAddressEntity(AddressEntity editAddressEntity) {
        this.editAddressEntity = editAddressEntity;
    }

    protected void setInfoValid() {
        boolean isV = !TextUtils.isEmpty(consignee) && !TextUtils.isEmpty(mobile)
                && !TextUtils.isEmpty(area) && !TextUtils.isEmpty(detailAddress);
        isValid.onNext(isV);
    }

    public BehaviorSubject<Boolean> getIsValid() {
        return isValid;
    }
}
