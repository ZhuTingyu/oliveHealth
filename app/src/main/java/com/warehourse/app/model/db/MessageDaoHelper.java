package com.warehourse.app.model.db;

import com.warehourse.app.model.UserModel;
import com.warehouse.dao.MessageBean;
import com.warehouse.dao.MessageBeanDao;

import java.util.List;

/**
 * Created by wangwei on 2016/3/18.
 */
public class MessageDaoHelper {
    public static final int PAGE_SIZE = 30;
    private MessageBeanDao messageBeanDao;

    public MessageDaoHelper() {
        messageBeanDao = DatabaseLoader.getDaoSession().getMessageBeanDao();
    }

    public void add(List<MessageBean> list) {
        if (list != null && list.size() > 0) {
            for (MessageBean messageBean : list) {
                List<MessageBean> listHis = messageBeanDao.queryBuilder()
                        .where(MessageBeanDao.Properties.Id.eq(messageBean.getId()),
                                MessageBeanDao.Properties.UserId.eq(messageBean.getUserId()))
                        .orderAsc(MessageBeanDao.Properties.Id)
                        .list();
                messageBeanDao.deleteInTx(listHis);
            }
            messageBeanDao.insertOrReplaceInTx(list, true);
        }
    }

    public void update(MessageBean messageBean) {
        messageBeanDao.update(messageBean);
    }

    public List<MessageBean> query(long id) {
        if (id > 0) {
            List<MessageBean> list = messageBeanDao.queryBuilder()
                    .where(MessageBeanDao.Properties.Id.lt(id),
                            MessageBeanDao.Properties.UserId.eq(UserModel.getInstance().getUserId()))
                    .orderDesc(MessageBeanDao.Properties.Id)
                    .limit(PAGE_SIZE)
                    .list();
            return list;
        } else {
            List<MessageBean> list = messageBeanDao.queryBuilder()
                    .where(MessageBeanDao.Properties.UserId.eq(UserModel.getInstance().getUserId()))
                    .orderDesc(MessageBeanDao.Properties.Id)
                    .limit(PAGE_SIZE)
                    .list();
            return list;
        }
    }

    public int queryNotReadCount() {
        List<MessageBean> list = messageBeanDao.queryBuilder()
                .where(MessageBeanDao.Properties.UserId.eq(UserModel.getInstance().getUserId()))
                .orderDesc(MessageBeanDao.Properties.Id)
                .list();
        int count = 0;
        for (MessageBean messageBean : list) {
            if (messageBean.getIsRead() == null || messageBean.getIsRead() == false)
                count = count + 1;
        }
        return count;
    }

    public void updateAllReadStatus() {
        List<MessageBean> list = messageBeanDao.queryBuilder()
                .where(MessageBeanDao.Properties.UserId.eq(UserModel.getInstance().getUserId()))
                .orderDesc(MessageBeanDao.Properties.Id)
                .list();
        if (list != null && list.size() > 0) {
            for(MessageBean messageBean:list){
                messageBean.setIsRead(true);
            }
            messageBeanDao.updateInTx(list);
        }
    }
}
