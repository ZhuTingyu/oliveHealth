package com.warehourse.app.ui.base;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.util.Lists;
import com.biz.widget.picker.ProvinceEntity;
import com.warehourse.app.model.ShopModel;
import com.warehourse.app.model.entity.AreaInfo;
import com.warehourse.app.ui.my.address.AddressCache;
import com.warehourse.app.ui.my.address.AreaModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class BaseProvinceViewModel extends BaseViewModel {
    private final AddressCache mAddressCache;

    protected List<ProvinceEntity> provinces;
    protected int provinceId;
    protected int cityId;
    protected int districtId;

    protected String province="";
    protected String city="";
    protected String district="";

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public BaseProvinceViewModel(Object activity) {
        super(activity);
        mAddressCache = new AddressCache();

        province(list->{});
    }


    public void province(Action1<List<ProvinceEntity>> onNext) {
        if (provinces != null && provinces.size() > 0) {
            Observable.just(provinces).subscribe(onNext);
            return;
        }
        submitRequest(ShopModel.getProvince(), list -> {
            provinces = list;
            Observable.just(provinces).subscribe(onNext);
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }


    public void setSelectedProvince(int i, int j, int p) {
        try {
            provinceId = provinces.get(i).getId();
            cityId = provinces.get(i).getCities().get(j).getId();
            districtId = provinces.get(i).getCities().get(j).getDistricts().get(p).getId();
            province = provinces.get(i).getName();
            city = provinces.get(i).getCities().get(j).getName();
            district = provinces.get(i).getCities().get(j).getDistricts().get(p).getName();
        } catch (Exception e) {
        }
    }


    public void requestProvince(Action1<List<AreaInfo>> onNext, Action1<Throwable> onError) {
        List<AreaInfo> provinceList = mAddressCache.getProvince("province");
        if (provinceList != null && provinceList.size() > 0) {
            Observable.just(provinceList).subscribe(onNext);
            return;
        }
        submitRequest(AreaModel.getProvince(), r -> {
            if (r.isOk()) {
                List<AreaInfo> list = r.data;

                if (list != null && !list.isEmpty()) {
                    mAddressCache.addProvince("province", list);
                } else {
                    list = Lists.newArrayList();
                }
                Observable.just(list).subscribe(onNext);
            } else {
                error.onNext(new RestErrorInfo(r.code,r.msg));
            }
        }, onError);

    }

    public void requestCity(Action1<List<AreaInfo>> onNext, Action1<Throwable> onError, long id, String name) {
        if (mAddressCache.getCity(splitKey(id, name)) != null
                && mAddressCache.getCity(splitKey(id, name)).size() > 0) {
            Observable.just(mAddressCache.getCity(splitKey(id, name))).subscribe(onNext);
            return;
        }
        submitRequest(AreaModel.getCity(id), r -> {
            if (r.isOk()) {
                List<AreaInfo> list = r.data;

                if (list != null && !list.isEmpty()) {
                    mAddressCache.addCity(splitKey(id, name), list);
                } else {
                    list = Lists.newArrayList();
                }
                Observable.just(list).subscribe(onNext);
            } else {
                error.onNext(new RestErrorInfo(r.code,r.msg));
            }
        }, onError);
    }

    public void requestDistrict(Action1<List<AreaInfo>> onNext, Action1<Throwable> onError, long id, String name) {
        if (mAddressCache.getDistrict(splitKey(id, name)) != null
                && mAddressCache.getDistrict(splitKey(id, name)).size() > 0) {
            Observable.just(mAddressCache.getDistrict(splitKey(id, name))).subscribe(onNext);
            return;
        }
        submitRequest(AreaModel.getDistrict(id), r -> {
            if (r.isOk()) {
                List<AreaInfo> list = r.data;

                if (list != null && !list.isEmpty()) {
                    mAddressCache.addDistrict(splitKey(id, name), list);
                } else {
                    list = Lists.newArrayList();
                }
                Observable.just(list).subscribe(onNext);
            } else {
                error.onNext(new RestErrorInfo(r.code,r.msg));
            }
        }, onError);
    }

    private String splitKey(long id, String name) {
        return id + "_" + name;
    }

    public Action1<Integer> setProvince() {
        return s -> {
            provinceId = s;
        };
    }

    public Action1<String> setProvinceText() {
        return s -> {
            province = s;
        };
    }

    public Action1<Integer> setCity() {
        return s -> {
            cityId = s;

        };
    }

    public Action1<String> setCityText() {
        return s -> {
            city = s;
        };
    }

    public Action1<Integer> setDistrict() {
        return s -> {
            districtId = s;
        };
    }

    public Action1<String> setDistrictText() {
        return s -> {
            district = s;
        };
    }

}