package com.warehourse.app.ui.message;


import com.biz.base.BaseViewModel;
import com.biz.util.Lists;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.MessageModel;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.UserEntity;
import com.warehouse.dao.MessageBean;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by wangwei on 2016/3/23.
 */
class MessageListViewModel extends BaseViewModel {
    private final BehaviorSubject<Integer> notReadCount = BehaviorSubject.create();
    private long lastFlag;

    public MessageListViewModel(Object activity) {
        super(activity);
        submitRequest(MessageModel.updateReadStatus(), b -> {
            submitRequest(UserModel.mainPoint(), mainPointEvent -> {
                EventBus.getDefault().post(mainPointEvent);
            }, throwable -> {
            });
        });
    }


    /**
     * 刷新 流程
     * 1.获取本地最大的ID
     * 2.去服务器上获取最新数据
     * 3.保存数据到本地 去掉本地有的数据
     * 4.重新查询数据库中的数据
     *
     * @param isMore
     */
    public void refresh(Action1<Boolean> isMore,Action1<List<MessageBean>> listAction1) {
        lastFlag = 0l;
        submitRequest(MessageModel.query(lastFlag), list -> {
            if (list.size() > 0) {
                lastFlag = list.get(0).getId();
            }
            submitRequest(MessageModel.messageList(lastFlag), i -> {
                if (i.isOk()) {
                    UserEntity userInfo = UserModel.getInstance().getUserEntity();
                    userInfo.msgCount = 0;
                    UserModel.getInstance().setUserInfo(userInfo);
                    EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_MESSAGE));
                    queryDb(i.data, isMore,listAction1);
                } else {
                    queryDb(i.data, isMore,listAction1);
                }
            }, throwable -> {
                queryDb(null, isMore,listAction1);
                error.onNext(getError(throwable));
            });
        }, throwable1 -> {
            error.onNext(getError(throwable1));
        });
    }

    private void queryDb(List<MessageBean> listData, Action1<Boolean> isMore,Action1<List<MessageBean>> listAction1) {
        if (listData == null) listData = Lists.newArrayList();
        submitRequest(MessageModel.save(0, listData), listOut -> {
            if (listOut.size() > 0) {
                lastFlag = listOut.get(0).getId();
            }
            if(listAction1!=null){
                Observable.just(listOut).subscribe(listAction1);
            }
            queryNotReadCount();
            Observable.just(listOut.size() > 0).subscribe(isMore);
            submitRequest(MessageModel.updateReadStatus(), b -> {
                submitRequest(UserModel.mainPoint(), mainPointEvent -> {
                    EventBus.getDefault().post(mainPointEvent);
                }, throwable -> {
                });
            });
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    public void updateRead(MessageBean messageBean, Action1<Boolean> onNext) {
        if (messageBean == null) {
            Observable.just(false).subscribe(onNext);
            return;
        }
        messageBean.setIsRead(true);
        submitRequest(MessageModel.update(messageBean), b -> {
            queryNotReadCount();
            Observable.just(b).subscribe();
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }


    public void loadMore(Action1<Boolean> isMore,Action1<List<MessageBean>> listAction1) {
        if (lastFlag == 0l) {
            Observable.just(false).subscribe(isMore);
            return;
        }
        submitRequest(MessageModel.query(lastFlag), r -> {
            if (r == null || r.size() == 0) {
                lastFlag = 0;
                Observable.just(false).subscribe(isMore);
                return;
            } else {
                lastFlag = r.get(r.size() - 1).getId();
                Observable.just(r).subscribe(listAction1);
                Observable.just(true).subscribe(isMore);
            }
        }, throwable -> {
            error.onNext(getError(throwable));
        });
    }

    private void queryNotReadCount() {
        submitRequest(MessageModel.queryNotReadCount(), count -> {
            notReadCount.onNext(count);
        }, throwable -> {
        });
    }

    public BehaviorSubject<Integer> getNotReadCount() {
        return notReadCount;
    }
}
