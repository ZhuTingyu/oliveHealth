package com.olive.ui.holder;

import android.widget.TextView;

/**
 * Created by TingYu Zhu on 2017/9/25.
 */

public class LineTextHolder {
    public TextView title;
    public TextView content;

    public void bindData(String titleString, String contentString){
        title.setText(titleString);
        content.setText(contentString);
    }
}
