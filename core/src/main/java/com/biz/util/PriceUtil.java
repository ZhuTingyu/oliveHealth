package com.biz.util;

import com.biz.http.R;
import com.biz.application.BaseApplication;
import com.biz.span.Span;
import com.biz.span.SpanUtil;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.unit.SpecialTextUnit;

public class PriceUtil {

    public static final int SYMBOL_ABSOLUTE_SIZE = 12;
    public static final int NEW_PRICE_ABSOLUTE_SIZE = 18;

    public static long toPrice(String rmb) {
        try {
            BigDecimal d = new BigDecimal(rmb);
            d = d.multiply(new BigDecimal(100));
            return Math.abs(d.longValue());
        } catch (Exception e) {
            return 0;
        }
    }

    private static BigDecimal toPriceDouble(long f) {
        BigDecimal d = new BigDecimal(f);
        d = d.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        return d;
    }

    private static BigDecimal toPriceDouble(double f) {
        BigDecimal d = new BigDecimal(f);
        d = d.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        return d;
    }

    private static String getPrice(BigDecimal price) {
        return "￥" + getPrecent(price);
    }

    public static String getPrecent(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.00");
        ds = fmt.format(price);
        return ds;
    }

    public static String getPrecent(double price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.00");
        ds = fmt.format(price);
        return ds;
    }

    private static String getPrecentInteger(BigDecimal price) {
        String ds = "";
        DecimalFormat fmt = new DecimalFormat("0.##");
        ds = fmt.format(price);
        return ds;
    }

    public static String formatInteger(long price) {
        return getPrecentInteger(toPriceDouble(price));
    }

    public static String formatRMBInteger(long price) {
        return "￥" + getPrecentInteger(toPriceDouble(price));
    }

    public static String formatRMB(long price) {
        return "￥" + getPrecent(toPriceDouble(price));
    }

    public static String formatRMB(double price) {
        return "¥" + getPrecent(toPriceDouble(price));
    }

    public static String formatRMBNoSymbol(double price) {
        return getPrecent(toPriceDouble(price));
    }

    public static String format(long price) {
        return getPrecent(toPriceDouble(price));
    }

    public static String formatRedHtml(long f) {
        return "<font color='#FF0000'> ￥" + getPrecent(toPriceDouble(f)) + "</font>";
    }

    public static String formatHtml(long f) {
        return "<font> ￥" + getPrecent(toPriceDouble(f)) + "</font>";
    }

    public static CharSequence formatRMBStyle(long price) {
        List<Span> spanList = Lists.newArrayList();
        spanList.add(
                new Span.Builder("￥")
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .absoluteSize(12)
                        .build()
        );

        spanList.add(
                new Span.Builder(format(price))
                        .absoluteSize(NEW_PRICE_ABSOLUTE_SIZE)
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .build()
        );
        return SpanUtil.getFormattedText(spanList);
    }

    public static CharSequence formatRMBStyle(String begin, long price, String end) {
        List<Span> spanList = Lists.newArrayList();
        if (!TextUtils.isEmpty(begin))
            spanList.add(
                    new Span.Builder(begin)
                            .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                            .absoluteSize(15)
                            .build()
            );
        spanList.add(
                new Span.Builder("￥")
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .absoluteSize(12)
                        .build()
        );

        spanList.add(
                new Span.Builder(format(price))
                        .absoluteSize(16)
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .build()
        );
        if (!TextUtils.isEmpty(end))
            spanList.add(
                    new Span.Builder(end)
                            .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                            .absoluteSize(NEW_PRICE_ABSOLUTE_SIZE)
                            .build()
            );
        return SpanUtil.getFormattedText(spanList);
    }

    public static CharSequence formatRMBStyle(long price, int symbolSize, int priceSize) {
        List<Span> spanList = Lists.newArrayList();
        spanList.add(
                new Span.Builder("￥")
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .absoluteSize(symbolSize)
                        .build()
        );

        spanList.add(
                new Span.Builder(format(price))
                        .absoluteSize(priceSize)
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .build()
        );
        return SpanUtil.getFormattedText(spanList);
    }

    public static CharSequence formatRMBStyle(String begin, long price, String end, int beginSize, int priceSize) {
        List<Span> spanList = Lists.newArrayList();
        if (!TextUtils.isEmpty(begin))
            spanList.add(
                    new Span.Builder(begin)
                            .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                            .absoluteSize(beginSize)
                            .build()
            );
        spanList.add(
                new Span.Builder("￥")
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .absoluteSize(12)
                        .build()
        );

        spanList.add(
                new Span.Builder(format(price))
                        .absoluteSize(priceSize)
                        .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                        .build()
        );
        if (!TextUtils.isEmpty(end))
            spanList.add(
                    new Span.Builder(end)
                            .foregroundColor(BaseApplication.getAppContext().getResources().getColor(R.color.color_money))
                            .absoluteSize(NEW_PRICE_ABSOLUTE_SIZE)
                            .build()
            );
        return SpanUtil.getFormattedText(spanList);
    }

    /**
     * @param f     金额
     * @param color 颜色
     * @return
     */
    public static String formatMarkRMBHtml(long f, String color) {
        if (f == 0) {
            return "<font color='" + color + "'> ￥" + getPrecent(toPriceDouble(Math.abs(f))) + "</font>";
        }
        if (f > 0) {
            return "<font color='" + color + "'>+ ￥" + getPrecent(toPriceDouble(Math.abs(f))) + "</font>";
        } else {
            return "<font color='" + color + "'>- ￥" + getPrecent(toPriceDouble(Math.abs(f))) + "</font>";
        }
    }


    public static void formatRMBStyleNoBeginNoAppend(TextView textView, long price, int priceSize, boolean priceUseBold) {
        formatRMBStyle(textView, null, 0, 0, SYMBOL_ABSOLUTE_SIZE, price, priceSize, priceUseBold, false);
    }

    public static void formatRMBStyleIsBeginNoAppendPriceSize(
            TextView textView, String begin, int beginSize, int beginColor, long price, int priceSize, boolean priceUseBold) {

        formatRMBStyle(textView, begin, beginSize, beginColor, SYMBOL_ABSOLUTE_SIZE,
                price, priceSize, priceUseBold, false);
    }

    public static void formatRMBStyleIsBeginNoAppend(TextView textView, String begin,
                                                     int beginSize, int beginColor, long price, boolean priceUseBold) {
        formatRMBStyle(textView, begin, beginSize, beginColor, SYMBOL_ABSOLUTE_SIZE,
                price, NEW_PRICE_ABSOLUTE_SIZE, priceUseBold, false);
    }

    public static void formatRMBStyleNoBeginIsAppend(TextView textView, long price, boolean priceUseBold) {
        formatRMBStyle(textView, null, 0, 0, SYMBOL_ABSOLUTE_SIZE,
                price, NEW_PRICE_ABSOLUTE_SIZE, priceUseBold, true);
    }

    public static void formatRMBStyle(TextView textView, String begin, int beginSize, int beginColor,
                                      int symbolSize, long price, int priceSize, boolean priceUseBold, boolean isAppend) {
        Context context = textView.getContext();

        SimplifySpanBuild simplifySpanBuild = new SimplifySpanBuild(context, textView);
        SpecialTextUnit priceUnit = new SpecialTextUnit(format(price))
                .setTextSize(priceSize)
                .setSpecialTextColor(context.getResources().getColor(R.color.color_money));

        if (priceUseBold) {
            priceUnit = priceUnit.useTextBold();
        }

        if (!TextUtils.isEmpty(begin)) {
            simplifySpanBuild
                    .appendSpecialUnit(new SpecialTextUnit(begin)
                            .setTextSize(beginSize)
                            .setSpecialTextColor(context.getResources().getColor(beginColor)));
        }
        simplifySpanBuild
                .appendSpecialUnit(
                        new SpecialTextUnit("￥")
                                .setTextSize(symbolSize)
                                .setSpecialTextColor(context.getResources().getColor(R.color.color_money)))
                .appendSpecialUnit(priceUnit);

        if (isAppend) {
            textView.append(simplifySpanBuild.build());
        } else {
            textView.setText(simplifySpanBuild.build());
        }
    }
}
