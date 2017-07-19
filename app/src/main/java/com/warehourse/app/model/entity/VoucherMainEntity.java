package com.warehourse.app.model.entity;

import com.biz.util.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwei on 2016/3/23.
 */
public class VoucherMainEntity {
    public ArrayList<VoucherEntity> unused;
    public ArrayList<VoucherEntity> used;
    public ArrayList<VoucherEntity> expired;

    public ArrayList<VoucherEntity> getUnused() {
        return unused == null ? Lists.newArrayList() : unused;
    }

    public ArrayList<VoucherEntity> getUsed() {
        return used == null ? Lists.newArrayList() : used;
    }

    public ArrayList<VoucherEntity> getExpired() {
        return expired == null ? Lists.newArrayList() : expired;
    }
}
