package com.warehourse.app.model.db;


import android.text.TextUtils;

import com.biz.util.Lists;
import com.warehourse.app.model.UserModel;
import com.warehouse.dao.ConfigBean;
import com.warehouse.dao.ConfigBeanDao;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by wangwei on 2016/3/18.
 */
public class ConfigDaoHelper{
    public static final String TYPE_INIT = "TYPE_INIT";
    public static final String INIT_ID = "INIT_ID";
    public static final String TYPE_UPGRADE = "TYPE_UPGRADE";
    public static final String TYPE_UPGRADE_VERSION = "TYPE_UPGRADE_VERSION";
    public static final String UPGRADE_VERSION_ID = "UPGRADE_VERSION_ID";
    public static final String UPGRADE_ID = "UPGRADE_ID";
    public static final String TYPE_USER = "TYPE_USER";
    public static final String USER_ID = "USER_ID";
    public static final String TYPE_SEARCH_KEY = "TYPE_SEARCH_KEY";
    public static final String SEARCH_ID = "SEARCH_ID";

    public static final String TYPE_MAIN="TYPE_MAIN";
    public static final String MAIN_ID="MAIN_ID";

    public static final String TYPE_LOGIN_NAME="TYPE_LOGIN_NAME";

    public static final String TYPE_HOME="TYPE_HOME";
    public static final String HOME_ID="HOME_ID";



    private static ConfigDaoHelper instance;
    private ConfigBeanDao configBeanDao;

    public ConfigDaoHelper() {
        try {
            configBeanDao = DatabaseLoader.getDaoSession().getConfigBeanDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigDaoHelper getInstance() {
        if (instance == null) {
            instance = new ConfigDaoHelper();
        }

        return instance;
    }

    public <T> void addData(T bean) {
        if (configBeanDao != null && bean != null) {
            configBeanDao.insertOrReplace((ConfigBean) bean);
        }
    }

    public <T> void addList(List<T> t) {
        if (configBeanDao != null && t != null) {
            configBeanDao.insertOrReplaceInTx((List<ConfigBean>) t, true);
        }
    }

    public void deleteData(String id) {
        if (configBeanDao != null && !TextUtils.isEmpty(id)) {
            configBeanDao.deleteByKey(id);
        }
    }

    public ConfigBean getDataById(String id) {
        if (configBeanDao != null && !TextUtils.isEmpty(id)) {
            return configBeanDao.load(id);
        }
        return null;
    }

    public List getAllData() {
        if (configBeanDao != null) {
            return configBeanDao.loadAll();
        }
        return null;
    }

    public boolean hasKey(String id) {
        if (configBeanDao == null || TextUtils.isEmpty(id)) {
            return false;
        }
        QueryBuilder<ConfigBean> qb = configBeanDao.queryBuilder();
        qb.where(ConfigBeanDao.Properties.Id.eq(id));
        long count = qb.buildCount().count();
        return count > 0;
    }

    public long getTotalCount() {
        if (configBeanDao == null) {
            return 0;
        }
        QueryBuilder<ConfigBean> qb = configBeanDao.queryBuilder();
        return qb.buildCount().count();
    }

    public void deleteAll() {
        if (configBeanDao != null) {
            configBeanDao.deleteAll();
        }
    }

    public void deleteAll(String type) {
        if (configBeanDao != null) {
            List<ConfigBean> list = queryListByType(type);
            if (list != null && list.size() > 0) {
                configBeanDao.deleteInTx(list);
            }
        }
    }

    public void delete(ConfigBean configBean) {
        if (configBeanDao != null) {
            configBeanDao.deleteInTx(configBean);
        }
    }

    public List<ConfigBean> queryListByType(String type) {
        List<ConfigBean> list = configBeanDao.queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(type))
                .orderAsc(ConfigBeanDao.Properties.Id)
                .list();
        if (list == null || list.size() == 0) return Lists.newArrayList();
        return list;
    }

    public List<ConfigBean> queryListByTypeOrderAscTs(String type) {
        List<ConfigBean> list = configBeanDao.queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(type),
                        ConfigBeanDao.Properties.UserId.eq(UserModel.getInstance().getUserId()))
                .orderAsc(ConfigBeanDao.Properties.Ts)
                .list();
        if (list == null || list.size() == 0) return Lists.newArrayList();
        return list;
    }

    public List<ConfigBean> queryListByTypeOrderDescTs(String type) {
        List<ConfigBean> list = configBeanDao.queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(type),
                        ConfigBeanDao.Properties.UserId.eq(UserModel.getInstance().getUserId()))
                .orderDesc(ConfigBeanDao.Properties.Ts)
                .list();
        if (list == null || list.size() == 0) return Lists.newArrayList();
        return list;
    }

    public ConfigBean queryByType(String type) {
        List<ConfigBean> list = configBeanDao.queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(type))
                .orderAsc(ConfigBeanDao.Properties.Id)
                .list();
        if (list == null || list.size() == 0) return null;
        return list.get(0);
    }

    public ConfigBean queryByType(String id, String type) {
        List<ConfigBean> list = configBeanDao.queryBuilder()
                .where(ConfigBeanDao.Properties.Type.eq(type), ConfigBeanDao.Properties.Id.eq(id))
                .orderAsc(ConfigBeanDao.Properties.Id)
                .list();
        if (list == null || list.size() == 0) return null;
        return list.get(0);
    }
}
