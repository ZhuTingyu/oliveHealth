package com.biz.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Title: ScoreUtil
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/8/16  18:00
 *
 * @author 郑炯
 * @version 1.0
 */
public class ScoreUtil {

    public static String formatScore(int score) {
        return getTencentInteger(toScoreFloat(score));
    }

    private static String getTencentInteger(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.0");
        ds = fmt.format(price);
        return ds;
    }

    private static BigDecimal toScoreFloat(int f) {
        BigDecimal d = new BigDecimal(f);
        d = d.divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP);
        return d;
    }
}
