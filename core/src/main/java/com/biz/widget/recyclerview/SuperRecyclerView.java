package com.biz.widget.recyclerview;


import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.biz.http.R;

import java.util.List;


public class SuperRecyclerView extends BaseRecyclerView {


    private TextView emptyTitleText;
    private ImageView emptyImageView;

    protected OnMoreListener mOnLoadMoreListener;

    public SuperRecyclerView(Context context) {
        super(context);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ITEM_LEFT_TO_LOAD_MORE = 30;

        setRefreshingColorResources(R.color.green_light, R.color.orange_light,
                R.color.blue_light, R.color.red_light);
        if (getEmptyView() != null) {
            emptyTitleText = (TextView) getEmptyView().findViewById(R.id.title);
            emptyImageView = (ImageView) getEmptyView().findViewById(R.id.icon);
        }

        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView == null) return;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(adapter);
//        if (adapter != null)
//            adapter.registerAdapterDataObserver(
//                    new RecyclerView.AdapterDataObserver() {
//                        @Override
//                        public void onItemRangeInserted(int positionStart, int itemCount) {
//                            super.onItemRangeInserted(positionStart, itemCount);
//                            setLoadCount(itemCount);
//                        }
//
//                        @Override
//                        public void onChanged() {
//                            super.onChanged();
////                            if(SuperRecyclerView.this.mRecycler.getAdapter().getItemCount() == 0 && SuperRecyclerView.this.mEmptyId != 0) {
////                                getRecyclerView().setVisibility(INVISIBLE);
////                            } else if(SuperRecyclerView.this.mEmptyId != 0) {
////                                getRecyclerView().setVisibility(VISIBLE);
////                            }else  getRecyclerView().setVisibility(VISIBLE);
//                        }
//                    });
    }

    @Override
    public void setScrollBarStyle(int style) {
        super.setScrollBarStyle(style);
    }


    public void refreshComplete() {
        getSwipeToRefresh().setRefreshing(false);
    }

    @Override
    public void setupMoreListener(OnMoreListener onMoreListener, int max) {
        mOnLoadMoreListener = onMoreListener;
        super.setupMoreListener(onMoreListener, max);
    }

    public void setEmptyTitleText(@StringRes int stringRes) {
        this.emptyTitleText.setText(stringRes);
    }

    public void setEmptyImageView(@DrawableRes int resId) {
        this.emptyImageView.setImageResource(resId);
    }

    public void setLoadCount(int count) {
        if (count != ITEM_LEFT_TO_LOAD_MORE) {
            refreshComplete();
            removeMoreListener();
            hideMoreProgress();
        } else setOnMoreListener(mOnLoadMoreListener);
    }

    public void setLoadCount(boolean disable) {
        if (!disable) {
            refreshComplete();
            removeMoreListener();
            hideMoreProgress();
        } else setOnMoreListener(mOnLoadMoreListener);
    }

    public void setLoadCount(List<?> list) {
        if (list == null) removeMoreListener();
        else if (list.size() != ITEM_LEFT_TO_LOAD_MORE) {
            refreshComplete();
            removeMoreListener();
            hideMoreProgress();
        } else setOnMoreListener(mOnLoadMoreListener);
    }

    public void stopLoadMore() {
        refreshComplete();
        removeMoreListener();
        hideMoreProgress();
    }

    public void setLoadCountHideMore(List<?> list) {
        if (list == null || list.size() == 0) {
            refreshComplete();
            removeMoreListener();
            hideMoreProgress();
        } else setOnMoreListener(mOnLoadMoreListener);
    }

    public void setLoadNearbyMaxCount(List<?> list) {
        if (list == null || list.size() < ITEM_LEFT_TO_LOAD_MORE - 2) {
            refreshComplete();
            removeMoreListener();
            hideMoreProgress();
        } else setOnMoreListener(mOnLoadMoreListener);
    }
}
