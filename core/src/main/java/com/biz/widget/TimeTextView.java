package com.biz.widget;


import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimeTextView extends android.support.v7.widget.AppCompatTextView implements Runnable {
    Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息
    private boolean run = false; //是否启动了
    private TimeEntity timeBeginEntity;
    private TimeEntity timeEndEntity;
    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
    }

    public TimeTextView(Context context) {
        super(context);
    }
    public void setEndTimes(long startTime, long endTime,RefreshViewListener refreshViewListener) {
        timeEndEntity= TimeEntity.build(startTime,endTime,false,refreshViewListener);
    }
    public void setBeginTimes(long startTime, long endTime,RefreshViewListener refreshViewListener) {
        timeBeginEntity= TimeEntity.build(startTime,endTime,true,refreshViewListener);
    }
    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        //标示已经启动
        run = true;
        if(timeBeginEntity!=null)
            timeBeginEntity.ComputeTime();
        if(timeEndEntity!=null)
            timeEndEntity.ComputeTime();

        if(timeBeginEntity!=null&&timeEndEntity==null)
        {
            this.setText(timeBeginEntity.formatText());
        }else if(timeBeginEntity!=null&&timeEndEntity!=null)
        {
            if(timeBeginEntity.isEnd()) this.setText(timeEndEntity.formatText());
            else this.setText(timeBeginEntity.formatText());
        }else if(timeEndEntity!=null)
        {
            this.setText(timeEndEntity.formatText());
        }else{
            this.setText("");
        }
        postDelayed(this, 1000);
    }
    public static class TimeEntity{
        public RefreshViewListener refreshViewListener;
        public long day, hour, min, second;//天，小时，分钟，秒
        public boolean isBegin;
        public static TimeEntity build(long startTime, long endTime,boolean isBegin,RefreshViewListener refreshViewListener)
        {
            TimeEntity entity=new TimeEntity();
            entity.initTime(startTime,endTime);
            entity.isBegin=isBegin;
            entity.refreshViewListener=refreshViewListener;
            return entity;
        }
        public boolean isEnd()
        {
            return day<=0l&&hour<=0l&&min<=0l&&second<=0l;
        }
        public Spanned formatText()
        {
            if(day<0l||hour<0l||min<0l||second<0l) return Html.fromHtml("");
            String strTime = "<font color='blue'>" + day
                    + "</font>" + "天<font color='blue'>" + hour
                    + "</font >小时<font color='blue'>" + min
                    + "</font>分钟<font color='blue'>" + second
                    + "</font >秒";
            if(day==0){
                strTime = "<font color='blue'>" + hour
                        + "</font >小时<font color='blue'>" + min
                        + "</font>分钟<font color='blue'>" + second
                        + "</font>秒";
            }
            if (isBegin) {
                strTime = "<font color='blue'> " + day
                        + "</font>" + "天<font color='blue'>" + hour
                        + "</font>小时<font color='blue'>" + min
                        + "</font>分钟<font color='blue'>" + second
                        + "</font>秒 开始";
                if(day==0)
                {
                    strTime = "<font color='blue'>" + hour
                            + "</font>小时<font color='blue'>" + min
                            + "</font>分钟<font color='blue'>" + second
                            + "</font>秒 开始";
                }
            }
            if(day==0&&hour==0&&min==0&&second==0){
                if(refreshViewListener!=null)
                {
                    refreshViewListener.call();
                }
            }
            return Html.fromHtml(strTime);
        }
        public void initTime(long startTime, long endTime) {
            //按照传入的格式生成一个simpledateformate对象
            long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
            long nh = 1000 * 60 * 60;//一小时的毫秒数
            long nm = 1000 * 60;//一分钟的毫秒数
            long ns = 1000;//一秒钟的毫秒数long diff;try {
            //获得两个时间的毫秒时间差异
            long diff = endTime - startTime;
            day = diff / nd;//计算差多少天
            if(day<0)
            {
                day=0;
                diff=diff+nd;
            }
            hour = diff % nd / nh;//计算差多少小时
            if(hour<0)
            {
                hour=0;
                diff=diff+nh;
            }
            min = diff % nd % nh / nm;//计算差多少分钟
            if(min<0)
            {
                min=0;
                diff=diff+nm;
            }
            second = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
            if(second<0)
            {
                second=0;
            }
            //LogUtil.print("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + second + "秒。");
        }
        /**
         * 倒计时计算
         */
        private void ComputeTime() {
            second--;
            if (second < 0) {
                min--;
                second = 59;
                if (min < 0) {
                    min = 59;
                    hour--;
                    if (hour < 0) {
                        // 倒计时结束
                        hour = 59;
                        day--;
                    }
                }
            }
        }
    }
    public interface RefreshViewListener{
        void call();
    }
}