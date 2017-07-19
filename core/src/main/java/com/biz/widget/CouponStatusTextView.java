package com.biz.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class CouponStatusTextView extends TextView {
    private Paint paint1 = new Paint();
    private int color= Color.WHITE;
    private int bgColor=Color.RED;
    private String text="";
    public CouponStatusTextView(Context context) {
        super(context);
    }

    public CouponStatusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CouponStatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CouponStatusTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setColor(int color,int bgColor)
    {
        this.color=color;
        this.bgColor=bgColor;
        this.postInvalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        this.text=getText().toString();
        if(TextUtils.isEmpty(text)) return;
        //文字大小
        float textSize=(getWidth()/3.5f)/(float)Math.sqrt(2);
        //获取结束点
        float xx1=textSize/(float)Math.sqrt(2);
        float end_x=getWidth()-xx1;
        float end_y=getHeight()-xx1;
        //画圆角矩形
        paint1.setColor(bgColor);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        path.moveTo(0, 0);// 此点为多边形的起点
        path.lineTo(xx1+textSize+xx1, 0);
        path.lineTo(getWidth(),getHeight()-(xx1+textSize+xx1));
        path.lineTo(getWidth(),getHeight());
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, paint1);
        //总长度
        int z=(int)(Math.abs(Math.sqrt((end_x*end_x+end_y*end_y))));
        //文字总长度
        float textWidth=textSize*text.length();
        paint1.reset();//重置
        paint1.setColor(color);
        paint1.setTextSize(textSize);
        //开始坐标
        float dd=z/2f-textWidth/2f;
        float x=dd/(float)Math.sqrt(2);
        canvas.rotate(45, xx1+x, x);
        canvas.drawText(text, xx1+x, x, paint1);
        canvas.rotate(-45, xx1+x, x);
    }
}
