package com.biz.widget;


import com.biz.http.R;
import com.biz.util.DrawableHelper;
import com.biz.util.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;



@SuppressWarnings("deprecation")
@SuppressLint("ClickableViewAccessibility")
public class ClearEditText extends AppCompatEditText implements OnFocusChangeListener,
        TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;

    private Drawable mLeftDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    private OnFocusChangeListener mOnFocusChangeListener;

    public ClearEditText(Context context) {
        this(context, null);

    }

    public ClearEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        TypedArray a = context.obtainStyledAttributes(attributeSet,
                R.styleable.ClearEditText);
        mClearDrawable = a.getDrawable(R.styleable.ClearEditText_drawableClear);
        mLeftDrawable = a.getDrawable(R.styleable.ClearEditText_drawableStart);
        if (mClearDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.met_ic_clear);
            mClearDrawable.setColorFilter(getResources().getColor(R.color.color_divider), PorterDuff.Mode.SRC_ATOP);
        }
        if (mLeftDrawable==null){
            mLeftDrawable = DrawableHelper.getDrawable(context, R.drawable.ic_search_gray);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        mLeftDrawable.setBounds(0, 0, mLeftDrawable.getIntrinsicWidth(),
                mLeftDrawable.getIntrinsicHeight());
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        super.setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                setClearIconVisible(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        a.recycle();
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
     * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (mOnFocusChangeListener!=null)
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        //else setBackgroundDrawable(hasFocus);

        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
        super.setOnFocusChangeListener(l);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(mLeftDrawable, getCompoundDrawables()[1], right,
                getCompoundDrawables()[3]);
        int padding = Utils.dip2px(8);
        setCompoundDrawablePadding(padding);
        setPadding(padding , 0, padding, 0);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置晃动动画
     */
    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}
