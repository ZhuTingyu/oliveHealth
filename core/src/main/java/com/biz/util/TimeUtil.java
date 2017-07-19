package com.biz.util;


import com.biz.application.BaseApplication;
import com.biz.http.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangwei on 2016/3/24.
 */
public class TimeUtil {
    public static final String FORMAT_YYYYMM = "yyyy-MM";
    public static final String FORMAT_YYYYMMDD = "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD_ = "yyyy-MM-dd";

    public static final String FORMAT_YYYYMM01 = "yyyy-MM-01";
    public static final String FORMAT_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_HHMM = "HH:mm";

    public static String formatValidityDate(long dateBegin, long dateEnd) {
        return BaseApplication.getAppContext().getString(R.string.text_money_date, format(dateBegin, FORMAT_YYYYMMDD), format(dateEnd, FORMAT_YYYYMMDD));
    }

    public static String format(long date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(new Date(date));
    }

    public static long parse(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(str).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
