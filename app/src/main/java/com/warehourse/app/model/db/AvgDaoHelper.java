package com.warehourse.app.model.db;

import com.warehouse.dao.AvgBean;
import com.warehouse.dao.AvgBeanDao;

import java.util.List;

/**
 * Created by wangwei on 2016/3/18.
 */
public class AvgDaoHelper {
    private AvgBeanDao avgBeanDao;

    public AvgDaoHelper() {
        avgBeanDao = DatabaseLoader.getDaoSession().getAvgBeanDao();
    }

    public void add(List<AvgBean> list) {
        avgBeanDao.deleteAll();
        if (list != null && list.size() > 0)
            avgBeanDao.insertOrReplaceInTx(list, true);

//        List<AvgBean> listHis = avgBeanDao.queryRaw("where " + AvgBeanDao.Properties.EndTime.columnName + "<" + System.currentTimeMillis(), new String[]{});
//        avgBeanDao.deleteInTx(listHis);
    }

    public AvgBean queryAvg() {
        long time = System.currentTimeMillis();
        List<AvgBean> list = avgBeanDao.queryRaw("where " + AvgBeanDao.Properties.StartTime.columnName + "<" + time + " and " + AvgBeanDao.Properties.EndTime.columnName + ">" + time + " ORDER BY PRIORITY DESC", new String[]{});

//        List<AvgBean> list = avgBeanDao.queryBuilder()
//                .where(AvgBeanDao.Properties.StartTime.lt(time),
//                        AvgBeanDao.Properties.EndTime.gt(time))
//                .orderAsc(AvgBeanDao.Properties.Priority)
//                .list();
        if (list == null || list.size() == 0) return null;
        return list.get(0);
    }
}
