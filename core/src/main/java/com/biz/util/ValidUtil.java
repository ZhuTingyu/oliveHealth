package com.biz.util;

import android.text.TextUtils;

/**
 * Created by wangwei on 2016/3/19.
 */
public class ValidUtil {
    public static boolean phoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() == 11) {
            try {
                Long phone = Long.valueOf(phoneNumber);
                if (phone >= 0) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return isValid;
    }
    public static boolean accountValid(String pwd) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() >= 3) {
            return true;
        }
        return isValid;
    }
    public static boolean pwdValid(String pwd) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() >= 6) {
            return true;
        }
        return isValid;
    }

    public static boolean payPwdValid(String pwd) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(pwd) && pwd.length() == 6) {
            return true;
        }
        return isValid;
    }
}
