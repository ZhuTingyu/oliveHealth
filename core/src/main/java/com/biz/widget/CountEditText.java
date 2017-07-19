package com.biz.widget;


import com.biz.util.Lists;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by wangwei on 2016/3/23.
 */
public class CountEditText extends AppCompatEditText {
    private List<TextWatcher> mListeners = null;
    public CountEditText(Context context) {
        super(context);
    }

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher)
    {
        if (mListeners == null)
        {
            mListeners = Lists.newArrayList();
        }
        mListeners.add(watcher);

        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher)
    {
        if (mListeners != null)
        {
            int i = mListeners.indexOf(watcher);
            if (i >= 0)
            {
                mListeners.remove(i);
            }
        }

        super.removeTextChangedListener(watcher);
    }

    public void clearTextChangedListeners()
    {
        if(mListeners != null)
        {
            for(TextWatcher watcher : mListeners)
            {
                super.removeTextChangedListener(watcher);
            }

            mListeners.clear();
            mListeners = null;
        }
    }
}
