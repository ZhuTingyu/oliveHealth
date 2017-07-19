package com.biz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.biz.http.R;


/**
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:16/9/1  15:22
 *
 * @author 郑炯
 * @version 1.0
 */

public class StatusTextView extends View {
    private Paint mTagPaint;
    private Paint mTextPaint;

    private String text;
    private int tagWidth;
    private float cornerDistance;
    private float tagTextSize;
    private int tagTextColor;
    private int tagColor;
    private int tagPosition;

    private PointF pointStart = new PointF();
    private PointF pointEnd = new PointF();
    private Path path = new Path();


    public StatusTextView(Context context) {
        this(context, null);
    }

    public StatusTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CouponStatusTextView, defStyleAttr, 0);
        text = typedArray.getString(R.styleable.CouponStatusTextView_tagText);
        tagWidth = typedArray.getDimensionPixelSize(R.styleable.CouponStatusTextView_tagWidth, dip2px(30));
        cornerDistance = typedArray.getDimensionPixelSize(R.styleable.CouponStatusTextView_cornerDistance, dip2px(50));
        tagTextSize = typedArray.getDimensionPixelSize(R.styleable.CouponStatusTextView_tagTextSize, dip2px(14));
        tagTextColor = typedArray.getColor(R.styleable.CouponStatusTextView_tagTextColor, Color.WHITE);
        tagColor = typedArray.getColor(R.styleable.CouponStatusTextView_tagColor, getResources().getColor(R.color.base_color));
        tagPosition = typedArray.getInt(R.styleable.CouponStatusTextView_tagPosition, 0);
        typedArray.recycle();

        mTagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTagPaint.setDither(true);
        mTagPaint.setColor(tagColor);
        mTagPaint.setStyle(Paint.Style.STROKE);
        mTagPaint.setStrokeJoin(Paint.Join.ROUND);
        mTagPaint.setStrokeCap(Paint.Cap.SQUARE);
        mTagPaint.setStrokeWidth(tagWidth);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(tagTextSize);
        mTextPaint.setColor(tagTextColor);
        mTextPaint.setAntiAlias(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (cornerDistance + tagWidth * Math.sqrt(2));
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float tagDistance = (float) (cornerDistance + tagWidth / 2 * Math.sqrt(2));
        float startX = getMeasuredWidth() - tagDistance;
        float endY = getMeasuredHeight() - tagDistance;
        switch (tagPosition) {
            case 0:
                pointStart.x = 0;
                pointStart.y = tagDistance;
                pointEnd.x = tagDistance;
                pointEnd.y = 0;
                break;
            case 1:
                pointStart.x = startX;
                pointStart.y = 0;
                pointEnd.x = getMeasuredWidth();
                pointEnd.y = tagDistance;
                break;
            case 2:
                pointStart.x = startX;
                pointStart.y = getMeasuredHeight();
                pointEnd.x = getMeasuredWidth();
                pointEnd.y = endY;
                break;
            case 3:
                pointStart.x = 0;
                pointStart.y = endY;
                pointEnd.x = tagDistance;
                pointEnd.y = getMeasuredHeight();
                break;
            default:
                pointStart.x = 0;
                pointStart.y = tagDistance;
                pointEnd.x = tagDistance;
                pointEnd.y = 0;
        }
        path.reset();
        path.moveTo(pointStart.x, pointStart.y);
        path.lineTo(pointEnd.x, pointEnd.y);
        canvas.drawPath(path, mTagPaint);

        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length() - 1, textBounds);

        canvas.drawTextOnPath(text, path,
                (float) (tagDistance * Math.sqrt(2)) / 2 - (float) Math.sqrt(2) * textBounds.width() / 2,
                (float) (Math.sqrt((textBounds.height() * textBounds.height() / 2))) / 2,
                mTextPaint);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) params;
        switch (tagPosition) {
            case 0:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case 1:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                break;
            case 2:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
            case 3:
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                break;
        }
    }

    public void setText(int resId) {
        String text = getContext().getString(resId);
        this.text = text;
        invalidate();
    }

    public void setTagColor(int color){
        tagColor = color;
        mTagPaint.setColor(tagColor);
        invalidate();
    }

    private int dip2px(int dip) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dip + 0.5f);
    }


}
