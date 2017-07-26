package com.olive.model.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.anye.greendao.gen.DaoMaster;
import com.anye.greendao.gen.DaoSession;

/**
 * Created by TingYu Zhu on 2017/7/26.
 */

public class DatabaseLoader {
    private static final String DATABASE_NAME = "olive.db";
    private static DaoSession daoSession;

    public static void init(Application application) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application,DATABASE_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
