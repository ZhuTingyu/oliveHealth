package com.warehourse.app.model;
import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.util.Lists;
import com.google.gson.reflect.TypeToken;
import com.warehourse.app.R;
import com.warehourse.app.model.db.MessageDaoHelper;
import com.warehourse.app.util.HttpRequest;
import com.warehouse.dao.MessageBean;

import java.util.List;

import rx.Observable;

/**
 * Created by johnzheng on 3/25/16.
 */
public class MessageModel{

    public static Observable<ResponseJson<List<MessageBean>>> messageList(Long lastNoticeId) {
        return HttpRequest.<ResponseJson<List<MessageBean>>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("lastNoticeId", lastNoticeId)
                .url(R.string.api_message_list)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<List<MessageBean>>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<Boolean> update(MessageBean messageBean) {
        return Observable.create(subscriber -> {
            MessageDaoHelper messageDaoHelper = new MessageDaoHelper();
            messageDaoHelper.update(messageBean);
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }

    public static Observable<List<MessageBean>> save(long lastFlag, List<MessageBean> listData) {
        //save db
        long idH = System.currentTimeMillis();
        for (MessageBean messageBean : listData) {
            messageBean.setUserId(UserModel.getInstance().getUserId());
            messageBean.setSysId(idH = idH + 1);
        }
        return Observable.create(subscriber -> {
            MessageDaoHelper messageDaoHelper = new MessageDaoHelper();
            if (listData != null && listData.size() > 0)
                messageDaoHelper.add(listData);
            List<MessageBean> listNew = messageDaoHelper.query(lastFlag);
            if (listNew == null) listNew = Lists.newArrayList();
            subscriber.onNext(listNew);
            subscriber.onCompleted();
        });
    }

    public static Observable<List<MessageBean>> query(long lastFlag) {
        return Observable.create(subscriber -> {
            //selected db
            MessageDaoHelper messageDaoHelper = new MessageDaoHelper();
            List<MessageBean> list = messageDaoHelper.query(lastFlag);
            if (list == null) list = Lists.newArrayList();
            subscriber.onNext(list);
            subscriber.onCompleted();
        });
    }

    public static Observable<Integer> queryNotReadCount() {
        return Observable.create(subscriber -> {
            //selected db
            MessageDaoHelper messageDaoHelper = new MessageDaoHelper();
            subscriber.onNext(messageDaoHelper.queryNotReadCount());
            subscriber.onCompleted();
        });
    }
    public static Observable<Boolean> updateReadStatus() {
        return Observable.create(subscriber -> {
            //selected db
            MessageDaoHelper messageDaoHelper = new MessageDaoHelper();
            messageDaoHelper.updateAllReadStatus();
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }
}
