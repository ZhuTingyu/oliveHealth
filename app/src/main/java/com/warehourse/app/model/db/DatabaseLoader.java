package com.warehourse.app.model.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.warehouse.dao.DaoMaster;
import com.warehouse.dao.DaoSession;


public class DatabaseLoader {
    private static final String DATABASE_NAME = "appv1.db";
    private static DaoSession daoSession;
    // 虚方法，可以无实体内容
    public static void init(Application application) {
        DbHelper helper = new DbHelper(application, DATABASE_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}