package com.olive.model.entity;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class AliPayResult {
    private String resultStatus;
    private String result;
    private String memo;

    public AliPayResult(String rawResult) {

        if (TextUtils.isEmpty(rawResult))
            return;

        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                resultStatus = gatValue(resultParam, "resultStatus");
            }
            if (resultParam.startsWith("result")) {
                result = gatValue(resultParam, "result");
            }
            if (resultParam.startsWith("memo")) {
                memo = gatValue(resultParam, "memo");
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    public String getTradeNumber(){
        JSONObject res = null;
        String tradeNumber = "";
        try {
            res = new JSONObject(getResult());
            res = res.getJSONObject("alipay_trade_app_pay_response");
            tradeNumber = res.getString("trade_no");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tradeNumber;
    }
}