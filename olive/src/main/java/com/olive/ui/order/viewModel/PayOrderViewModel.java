package com.olive.ui.order.viewModel;

import com.biz.base.BaseViewModel;
import com.biz.base.RestErrorInfo;
import com.biz.http.HttpErrorException;
import com.olive.R;
import com.olive.model.AccountModel;
import com.olive.model.OrderModel;
import com.olive.model.entity.BankEntity;
import com.olive.model.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by TingYu Zhu on 2017/8/7.
 */


public class PayOrderViewModel extends BaseViewModel {

    public static final int PAY_TYPE_BANK = 0;
    public static final int PAY_TYPE_WEI = 1;
    public static final int PAY_TYPE_ALI = 2;

    private OrderEntity orderEntity;
    private String orderInfo = "app_id=2016082000292686&biz_content=%7B%22out_trade_no%22%3A%22tradepay14780883537524762206%22%2C%22total_amount%22%3A%221.00%22%2C%22subject%22%3A%22test%22%2C%22body%22%3A%22284885403956064810%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA&timestamp=2017-08-16+15%3A04%3A54&version=1.0&sign=vmuht2Z1eGJRFzu0GYXgOomBK0y2wR98dcyQdDZaVzvXxDbbtx%2BtOENLQaHnZExRuiItJqhAkZvleLiW0HLd2UYE9Xr%2BKdXa30H04tSoQCk2r1R3G6UDu9FIthnnwpcbXT9N7cz0EDvY98Y6fECU4qesDMHbb3wHM%2BaPbRI3Ue0%3D";

    private String payPassword;
    private int payType;

    private int balancePayAmount;

    public PayOrderViewModel(Object activity) {
        super(activity);
    }

    public void getBankCards(Action1<List<BankEntity>> action1) {
        submitRequestThrowError(AccountModel.bankCards().map(r -> {
            if (r.isOk()) {
                List<BankEntity> entityList = r.data;
                return entityList;
            } else throw new HttpErrorException(r);
        }), action1);
    }

    public void getAliPayOrderInfoAndPay(Action1<String> action1){
        submitRequestThrowError(OrderModel.AlipayOrderInfo(orderEntity.orderNo, balancePayAmount).map(r -> {
            if(r.isOk()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),s -> {
            payOrderByAlliPay(s,action1);
        });
    }

    private void payOrderByAlliPay(String orderInfo, Action1<String> action1) {
        submitRequest(OrderModel.payAlipay(orderInfo, getActivity()).map(a -> {
            String result = "";
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
        }), action1, throwable -> {
            error.onNext(new RestErrorInfo(getString(R.string.resultcode_alipay_ERROR_4000)));
        });
    }


    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Action1<String> setBalancePayAmount(){
        return s -> {
            if(!s.isEmpty()){
                balancePayAmount = Integer.valueOf(s) * 100;
            }else {
                balancePayAmount = 0;
            }
        };
    }

}
