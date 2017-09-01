package com.olive.ui.order.viewModel;

import android.support.annotation.Nullable;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.http.HttpErrorException;
import com.biz.share.weixin.SendWX;
import com.biz.util.IntentBuilder;
import com.olive.R;
import com.olive.event.WeiPayResultEvent;
import com.olive.model.AccountModel;
import com.olive.model.OrderModel;
import com.olive.model.entity.AccountEntity;
import com.olive.model.entity.BankEntity;
import com.olive.model.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */


public class PayOrderViewModel extends BaseViewModel {

    public static final int PAY_TYPE_BANK = 0;
    public static final int PAY_TYPE_WEI = 1;
    public static final int PAY_TYPE_ALI = 2;
    public static final int PAY_TYPE_BALANCE = -1;

    public static final int PAY_PASSWORD_CORRECT = 1;

    private static final int PAY_NOT_IS_USE_BALANCE = 0;
    private static final int PAY_IS_USE_BALANCE = 1;


    public OrderEntity orderEntity;
    public AccountEntity accountEntity;

    private String payPassword;
    public int payType = PAY_TYPE_BALANCE;
    private int balancePayAmount;
    private Integer bankCardId;
    private String outTradeNo;

    private HashMap payOrderParameterMap;

    public PayOrderViewModel(Object activity) {
        super(activity);
        payOrderParameterMap = new HashMap();
        orderEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);
        accountEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_VALUE);
        EventBus.getDefault().register(this);
    }

    public void getBankCards(Action1<List<BankEntity>> action1) {
        submitRequestThrowError(AccountModel.bankCards().map(r -> {
            if (r.isOk()) {
                List<BankEntity> entityList = r.data;
                return entityList;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void payOrder(Action1<String> action1, Action1<Throwable> error) {
        payOrderParameterMap.clear();
        initMap();
        submitRequest(OrderModel.payOrder(payOrderParameterMap).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1, error);
    }

    public void getAliPayOrderInfoAndPay(Action1<String> action1) {
        submitRequestThrowError(OrderModel.aliPayOrderInfo(orderEntity.orderNo, balancePayAmount).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), s -> {
            payOrderByAlliPay(s, action1);
        });
    }

    private void payOrderByAlliPay(String orderInfo, Action1<String> action1) {
        submitRequest(OrderModel.payAliPay(orderInfo, getActivity()).map(a -> {
            String result = "";
            outTradeNo = a.getTradeNumber();
            if ("9000".equals(a.getResultStatus())) {
                result = getString(R.string.message_pay_success);
            } else if ("8000".equals(a.getResultStatus())) {
                result = getString(R.string.resultcode_alipay_ERROR_4000);
            } else if ("4000".equals(a.getResultStatus())) {
                result = getString(R.string.resultcode_alipay_ERROR_4000);
            } else if ("6001".equals(a.getResultStatus())) {
                result = getString(R.string.resultcode_alipay_ERROR_6001);
            } else if ("6002".equals(a.getResultStatus())) {
                result = getString(R.string.resultcode_alipay_ERROR_6002);
            } else {
                result = getString(R.string.resultcode_alipay_ERROR_4000);
            }
            return result;
        }), action1);
    }

    public void getWeiXinOrderInfoAndPay() {
        submitRequestThrowError(OrderModel.weiXinOrderInfo(orderEntity.orderNo, balancePayAmount).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), weiXinPayEntity -> {
            SendWX sendWX = new SendWX(getActivity());
            sendWX.payWeiXin(weiXinPayEntity.getPayReq());
        });
    }

    public void checkPayPassword(Action1<Integer> action1) {
        submitRequestThrowError(OrderModel.checkPayPassword(payPassword).map(r -> {
            if (r.isOk()) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public boolean isPayHasBalance() {
        return balancePayAmount != 0;
    }

    public boolean isBalanceEnough() {
        return orderEntity.amount <= accountEntity.balance && orderEntity.amount == balancePayAmount;
    }

    public boolean isPayWithAli() {
        return payType == PAY_TYPE_ALI;
    }

    public boolean isPayWithWei() {
        return payType == PAY_TYPE_WEI;
    }


    public void setPayType(int payType, @Nullable int bankCardId) {
        if (payType == PAY_TYPE_BANK) {
            this.bankCardId = bankCardId;
        }

        this.payType = payType;

    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword + getString(R.string.string_password_suffix);
    }

    public Action1<String> setBalancePayAmount() {
        return s -> {
            if (!s.isEmpty()) {
                balancePayAmount = (int) (Float.valueOf(s) * 100);
            } else {
                balancePayAmount = 0;
            }
        };
    }

    public void setBalancePayAmount(int price) {
        balancePayAmount = price;
    }


    private void initMap() {
        payOrderParameterMap.put("orderNo", orderEntity.orderNo);

        payOrderParameterMap.put("payType", payType);
        if (payType == PAY_TYPE_BANK) {
            payOrderParameterMap.put("bankCardId", bankCardId);
        }
        if (balancePayAmount == 0) {
            payOrderParameterMap.put("useBalancePay", PAY_NOT_IS_USE_BALANCE);
        } else {
            payOrderParameterMap.put("useBalancePay", PAY_IS_USE_BALANCE);
            payOrderParameterMap.put("balancePayAmount", balancePayAmount);
        }
        if (payType == PAY_TYPE_ALI) {
            payOrderParameterMap.put("outTradeNo", outTradeNo);
        } else if (payType == PAY_TYPE_WEI) {

        } else if (payType == PAY_TYPE_BANK) {

        }

        payOrderParameterMap.put("payPassword", payPassword);
    }

    public void onEvent(WeiPayResultEvent event) {
        if (event.code == WeiPayResultEvent.SUCCESS) {

        } else if (event.code == WeiPayResultEvent.CANCEL) {

        } else if (event.code == WeiPayResultEvent.ERROR) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

