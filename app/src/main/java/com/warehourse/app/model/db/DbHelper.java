package com.warehourse.app.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.warehouse.dao.AvgBeanDao;
import com.warehouse.dao.ConfigBeanDao;
import com.warehouse.dao.DaoMaster;


/**
 * Created by wangwei on 2016/3/18.
 */
public class DbHelper extends DaoMaster.OpenHelper {
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                AvgBeanDao.createTable(db,true);
                ConfigBeanDao.createTable(db,true);
                break;
        }
    }
}
