package com.anye.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.olive.model.entity.ConfigBean;

import com.anye.greendao.gen.ConfigBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig configBeanDaoConfig;

    private final ConfigBeanDao configBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        configBeanDaoConfig = daoConfigMap.get(ConfigBeanDao.class).clone();
        configBeanDaoConfig.initIdentityScope(type);

        configBeanDao = new ConfigBeanDao(configBeanDaoConfig, this);

        registerDao(ConfigBean.class, configBeanDao);
    }
    
    public void clear() {
        configBeanDaoConfig.clearIdentityScope();
    }

    public ConfigBeanDao getConfigBeanDao() {
        return configBeanDao;
    }

}
