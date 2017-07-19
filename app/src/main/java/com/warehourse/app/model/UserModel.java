package com.warehourse.app.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.biz.http.ResponseJson;
import com.biz.http.RestMethodEnum;
import com.biz.push.PushManager;
import com.biz.util.DialogUtil;
import com.biz.util.GsonUtil;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.MD5;

import com.google.gson.reflect.TypeToken;

import com.warehourse.app.R;
import com.warehourse.app.event.CartCountEvent;
import com.warehourse.app.event.MainPointEvent;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.db.ConfigDaoHelper;
import com.warehourse.app.model.entity.UserEntity;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.util.HttpRequest;
import com.warehouse.dao.ConfigBean;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Title: UserModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:2017/5/15  17:58
 *
 * @author wangwei
 * @version 1.0
 */
public class UserModel {
    private static UserModel userModel;
    private UserEntity userEntity;
    private final CompositeSubscription subscription = new CompositeSubscription();

    public static UserModel getInstance() {
        if (userModel == null) {
            synchronized (UserModel.class) {
                userModel = new UserModel();
            }
        }
        return userModel;
    }

    public UserModel() {
        List<ConfigBean> list = ConfigDaoHelper.getInstance().queryListByType(ConfigDaoHelper.TYPE_USER);
        if (list != null && list.size() > 0) {
            for (ConfigBean configBean : list) {
                if (configBean.getTs() != null && configBean.getTs() > 0) {
                    try {
                        this.userEntity = GsonUtil.fromJson(configBean.getCache(), new TypeToken<UserEntity>() {
                        }.getType());
                    } catch (Exception e) {
                    }
                    break;
                }
            }
        }
    }


    public String getUserId() {
        if (userEntity == null) return "";
        return userEntity.getUserId();
    }

    public String getShopId() {
        if (userEntity == null) return "";
        return userEntity.getShopId();
    }

    public int getEditPhoto() {
        if (userEntity == null) return -1;
        return userEntity.qualificationAuditStatus;
    }

    public int getEditAddress() {
        if (userEntity == null) return -1;
        return userEntity.detailAuditStatus;
    }

    public boolean isReview() {
        if (userEntity == null) return false;
        return (userEntity.detailAuditStatus == 20 || userEntity.detailAuditStatus == 25) ||
                (userEntity.qualificationAuditStatus == 20 || userEntity.qualificationAuditStatus == 25);
    }

    public boolean isEditDetail() {
        if (userEntity == null) return false;
        return userEntity.detailAuditStatus != 30 || userEntity.qualificationAuditStatus != 30;
    }

    public void createContactDialog(Context context) {
        String tell = userEntity == null ? "" : userEntity.csh;
        if (TextUtils.isEmpty(tell)) {
            tell = SystemModel.getInstance().getTell400();
        }
        final String tell1 = tell;
        DialogUtil.createDialogViewWithCancel(context,
                R.string.title_contact,
                context.getString(R.string.text_contact_customer) + tell1,
                (dialog, i) -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(context.getString(R.string.text_tel) + tell1.replace(" ", "")));
                        context.startActivity(intent);
                    } catch (Exception e) {
                    }
                },
                R.string.btn_dial);
    }

    public void createLoginDialog(Context context, Action0 loginOnNext) {
        if (context == null) return;
        if (!isLogin()) {
            DialogUtil.createDialogView(context,
                    R.string.dialog_title_notice,
                    R.string.dialog_login_tip,
                    (DialogInterface dialog, int which) -> {
                        dialog.dismiss();
                    }
                    , R.string.btn_cancel_login,
                    (DialogInterface dialog, int which) -> {
                        LoginActivity.goLogin(context);
                    }
                    , R.string.btn_im_login);
        } else {
            if (loginOnNext != null)
                loginOnNext.call();
        }
    }

    public boolean isLogin() {

        return this.userEntity != null && !TextUtils.isEmpty(userEntity.userId)
                && userEntity.detailAuditStatus == 30 && userEntity.qualificationAuditStatus == 30;
    }

    public UserEntity getUserEntity() {
        if (userEntity == null) return new UserEntity();
        return userEntity;
    }

    public String getMobile() {
        return userEntity == null || userEntity.mobile == null ? "" : userEntity.mobile;
    }

    public String getName() {
        return userEntity == null || userEntity.name == null ? "" : userEntity.name;
    }

    public String getAvatar() {
        return userEntity == null || userEntity.avatar == null ? "" : userEntity.avatar;
    }

    public static Observable<ResponseJson<UserEntity>> login(String account, String password) {
        return HttpRequest.<ResponseJson<UserEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .setToJsonType(new TypeToken<ResponseJson<UserEntity>>() {
                }.getType())
                .url(R.string.api_user_login)
                .addBody("account", account)
                .addBody("password", MD5.toMD5(password))
                .addBody("pushToken", PushManager.getInstance().getPushTag())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserModel.getInstance().setUserInfo(r.data);
                        EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_LOGIN));
                        EventBus.getDefault().post(new CartCountEvent(r.data.purchaseCount));
                        OrderModel.getInstance().updateOrderCount();
                        mainPoint().subscribe(mainPointEvent -> {
                            EventBus.getDefault().post(mainPointEvent);
                        });
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<UserEntity>> autoLogin() {
        return HttpRequest.<ResponseJson<UserEntity>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .setToJsonType(new TypeToken<ResponseJson<UserEntity>>() {
                }.getType())
                .url(R.string.api_user_auto_login)
                .userId(UserModel.getInstance().getUserId())
                .addBody("pushToken", PushManager.getInstance().getPushTag())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserModel.getInstance().setUserInfo(r.data);
                        EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_LOGIN));
                        EventBus.getDefault().post(new CartCountEvent(r.data.purchaseCount));
                        OrderModel.getInstance().updateOrderCount();
                        mainPoint().subscribe(mainPointEvent -> {
                            EventBus.getDefault().post(mainPointEvent);
                        });
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> loginOutUser() {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_user_login_out)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<UserEntity>> register(String mobile, String password, String smsCode, String inviterCode) {
        return HttpRequest.<ResponseJson<UserEntity>>builder()
                .addBody("mobile", mobile)
                .addBody("password", MD5.toMD5(password))
                .addBody("smsCode", smsCode)
                .addBody("inviterCode", inviterCode)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_user_register)
                .setToJsonType(new TypeToken<ResponseJson<UserEntity>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserModel.getInstance().setUserInfo(r.data);
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> changePassword(String password,
                                                                  String newPassword) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .addBody("originPassword", MD5.toMD5(password))
                .addBody("newPassword", MD5.toMD5(newPassword))
                .addBody("confirmPassword", MD5.toMD5(newPassword))
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_change_password)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<Object>> forgotPassword(String mobile, String password, String smsCode) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .addBody("mobile", mobile)
                .addBody("password", MD5.toMD5(password))
                .addBody("smsCode", smsCode)
                .restMethod(RestMethodEnum.REST_POST)
                .url(R.string.api_forgot_password)
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI();
    }

    public static Observable<ResponseJson<Object>> changeMobile(String mobile, String smsCode) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("mobile", mobile)
                .addBody("smsCode", smsCode)
                .url(R.string.api_change_mobile)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserEntity userInfo = UserModel.getInstance().userEntity;
                        if (userInfo != null) {
                            userInfo.mobile = mobile;
                            UserModel.getInstance().setUserInfo(userInfo);
                            EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_CHANGE_MOBILE));
                        }
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> changeAvatar(String avatar) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("avatar", avatar)
                .url(R.string.api_change_avatar)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserEntity userInfo = UserModel.getInstance().getUserEntity();
                        if (userInfo != null) {
                            userInfo.avatar = avatar;
                            UserModel.getInstance().setUserInfo(userInfo);
                            EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_CHANGE_AVATAR));
                        }
                    }
                    return r;
                });
    }

    public static Observable<ResponseJson<Object>> changeDeliveryName(String deliveryName) {
        return HttpRequest.<ResponseJson<Object>>builder()
                .restMethod(RestMethodEnum.REST_POST)
                .addBody("deliveryName", deliveryName)
                .url(R.string.api_shop_change_delivery_name)
                .userId(UserModel.getInstance().getUserId())
                .setToJsonType(new TypeToken<ResponseJson<Object>>() {
                }.getType())
                .requestPI().map(r -> {
                    if (r.isOk()) {
                        UserEntity userInfo = UserModel.getInstance().getUserEntity();
                        if (userInfo != null) {
                            userInfo.deliveryName = deliveryName;
                            UserModel.getInstance().setUserInfo(userInfo);
                            EventBus.getDefault().post(new UserEvent(UserEvent.TYPE_CHANGE_DELIVERY_NAME));
                        }
                    }
                    return r;
                });
    }

    public synchronized void loginOut() {
        loginOutUser().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(b -> {
        }, throwable -> {
        });
        List<ConfigBean> list = ConfigDaoHelper.getInstance().queryListByType(ConfigDaoHelper.TYPE_USER);
        if (list != null && list.size() > 0) {
            for (ConfigBean configBean : list) {
                configBean.setTs(-1l);
            }
        } else if (list == null) {
            list = Lists.newArrayList();
        }
        ConfigDaoHelper.getInstance().addList(list);
        this.userEntity = null;
    }

    public synchronized void setUserInfo(UserEntity userInfo) {
        List<ConfigBean> list = ConfigDaoHelper.getInstance().queryListByType(ConfigDaoHelper.TYPE_USER);
        boolean isExists = false;
        if (list != null && list.size() > 0) {
            for (ConfigBean configBean : list) {
                if (userInfo != null && userInfo.getUserId().equals(configBean.getUserId())) {
                    configBean.setCache(GsonUtil.toJson(userInfo));
                    configBean.setKey(userInfo.mobile);
                    configBean.setTs(System.currentTimeMillis());
                    configBean.setType(ConfigDaoHelper.TYPE_USER);
                    isExists = true;
                } else {
                    configBean.setTs(-1l);
                }
            }
        } else if (list == null) {
            list = Lists.newArrayList();
        }
        if (!isExists && userInfo != null) {
            ConfigBean configBean = new ConfigBean();
            configBean.setCache(GsonUtil.toJson(userInfo));
            configBean.setKey(userInfo.mobile);
            configBean.setTs(System.currentTimeMillis());
            configBean.setType(ConfigDaoHelper.TYPE_USER);
            configBean.setId(ConfigDaoHelper.USER_ID + userInfo.userId);
            configBean.setUserId(userInfo.userId);
            list.add(configBean);
        }
        ConfigDaoHelper.getInstance().addList(list);
        this.userEntity = userInfo;
    }

    public static Observable<String> loginMobile() {
        return Observable.create(subscriber -> {
            ConfigBean configBean = ConfigDaoHelper.getInstance().queryByType(ConfigDaoHelper.TYPE_LOGIN_NAME);
            if (configBean != null) {
                subscriber.onNext(configBean.getCache() == null ? "" : configBean.getCache());
            } else {
                subscriber.onNext("");
            }
            subscriber.onCompleted();
        });
    }

    public static Observable<Boolean> saveLoginMobile(String mobile) {
        return Observable.create(subscriber -> {
            ConfigBean configBean = new ConfigBean();
            configBean.setCache(mobile);
            configBean.setId("0");
            configBean.setTs(System.currentTimeMillis());
            configBean.setType(ConfigDaoHelper.TYPE_LOGIN_NAME);
            ConfigDaoHelper.getInstance().addData(configBean);
            subscriber.onNext(true);
            subscriber.onCompleted();
        });
    }

    public static Observable<MainPointEvent> mainPoint() {
        return MessageModel.queryNotReadCount().map(count -> {
            if (UserModel.getInstance().isLogin()) {
                MainPointEvent event = new MainPointEvent();
                LogUtil.print("queryNotReadCount" + count + "|" + UserModel.getInstance().getUserEntity().msgCount);
                event.messageCount = UserModel.getInstance().getUserEntity().msgCount + count;
                event.showPaymentButton = UserModel.getInstance().getUserEntity().showPaymentButton;
                event.luckMoneyCount = UserModel.getInstance().getUserEntity().luckMoneyCount;
                event.showActivityRedPoint = UserModel.getInstance().getUserEntity().showActivityRedPoint;
                return event;
            } else {
                return new MainPointEvent();
            }
        });
    }

    public void updateUserStatus() {
        subscription.clear();
        subscription.add(autoLogin()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfoResponseJson -> {
                }, throwable -> {
                }));
    }
}
