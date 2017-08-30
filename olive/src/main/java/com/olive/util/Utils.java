package com.olive.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.olive.R;

import org.w3c.dom.Text;

/**
 * Created by TingYu Zhu on 2017/8/3.
 */

public class Utils {

    public static void setRecyclerViewNestedSlide(XRecyclerView recyclerView){
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
                    case MotionEvent.ACTION_DOWN:
                        recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // 当手指松开时，让父控件重新获取onTouch权限
                        recyclerView.getParent().requestDisallowInterceptTouchEvent(false);
                        break;

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });
    }

    public static void setListHeight(RecyclerView recyclerView){
        // 获取ListView对应的Adapter
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int totalHeight = 0;
        for (int i = 0, len = adapter.getItemCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = recyclerView.getLayoutManager().findViewByPosition(0);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
    }

    public static void setEmptyView(Context context, BaseQuickAdapter adapter, String message){
        View view = View.inflate(context, R.layout.list_empty_layout,null);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(message);
        adapter.setEmptyView(view);
    }

}
