package com.warehourse.app.ui.search;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.widget.banner.PageRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.warehourse.app.R;
import com.warehourse.app.event.HotKeyClickEvent;
import com.warehourse.app.model.SystemModel;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Title: SearchOverlayFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:17/05/2017  14:50
 *
 * @author johnzheng
 * @version 1.0
 */

public class SearchOverlayFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener{


    RecyclerView mRecyclerView;
    PageRecyclerView mPageRecyclerView;
    OverlayAdapter mOverlayAdapter;
    SearchViewModel mViewModel;
    View btnView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mViewModel = new SearchViewModel(getActivity());
        initViewModel(mViewModel);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mOverlayAdapter!=null){
            mViewModel.getSearchKeys(list->{
                mOverlayAdapter.setNewData(list);
                mOverlayAdapter.removeAllFooterView();
                if (!list.isEmpty() ){
                    btnView  = clearBtn();
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_recyclerview_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView(android.R.id.list);

        mOverlayAdapter = new OverlayAdapter();
        mRecyclerView.setAdapter(mOverlayAdapter);
        mOverlayAdapter.setOnItemClickListener(this);
        addItemDecorationLine(mRecyclerView);
        mViewModel.getSearchKeys(list->{
            mOverlayAdapter.setNewData(list);
            mOverlayAdapter.removeAllFooterView();
            if (!list.isEmpty() ){
                btnView  = clearBtn();
            }
        });
        banner();

        section2();




    }

    private View banner() {
        View view = View.inflate(getContext(), R.layout.item_search_banner_layout, null);
        TextView textView = getView(view, R.id.title);
        textView.setText(R.string.text_search_promo);
        mPageRecyclerView = getView(view, R.id.list);
        mPageRecyclerView.setPageIndicator(getView(view, R.id.indicator));

        List<String> list = SystemModel.getInstance().getHotEntity();
        BannerAdapter adapter = new BannerAdapter();
        adapter.setNewData(list);
        adapter.setOnItemClickListener(this);
        mPageRecyclerView.setAdapter(adapter);

        mOverlayAdapter.addHeaderView(view);
        return view;
    }

    private View section2() {
        View view = View.inflate(getContext(), R.layout.item_section_header_layout, null);
        TextView textView = getView(view, R.id.title);
        textView.setText(R.string.text_search_history);
        mOverlayAdapter.addHeaderView(view);
        return view;
    }

    private View clearBtn() {
        View view = View.inflate(getContext(), R.layout.view_button_layout, null);
        TextView textView = getView(view, R.id.btn);
        textView.setText(R.string.text_clear_history);
        textView.setBackgroundResource(R.drawable.btn_gray_selector);

        bindUi(RxUtil.click(textView), s->{
            mViewModel.clearHistory(b->{
                if (b) {
                    mOverlayAdapter.setNewData(Lists.newArrayList());
                    mOverlayAdapter.removeFooterView(btnView);
                }
            });
        });
        mOverlayAdapter.addFooterView(view);
        return view;
    }



    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        String key = (String)baseQuickAdapter.getData().get(i);
        if (!TextUtils.isEmpty(key)) {
            EventBus.getDefault().post(new HotKeyClickEvent(key));
        }
    }


    private class BannerAdapter extends PageRecyclerView.PagingAdapter<String, BaseViewHolder> {

        public BannerAdapter() {
            super(R.layout.item_search_text_card_layout);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BaseViewHolder holder =  super.onCreateViewHolder(parent, viewType);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            lp.width =
                    holder.itemView.getResources()
                            .getDisplayMetrics().widthPixels / 4;
            holder.itemView.setLayoutParams(lp);
            return holder;
        }

        @Override
        protected void convert(BaseViewHolder holder, String s) {
            TextView view = (TextView)holder.getView(R.id.title);
            view.setText(s==null?"":s);
            view.setSingleLine();
            if (TextUtils.isEmpty(s)){
                holder.setBackgroundColor(R.id.title, Color.TRANSPARENT);
            }else {
                holder.setBackgroundColor(R.id.title, Color.WHITE);
            }
        }

        @Override
        public Object getEmpty() {
            return "";
        }
    }

    private class OverlayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        OverlayAdapter() {
            super(R.layout.item_search_text_history_layout);
        }

        @Override
        protected void convert(BaseViewHolder holder, String s) {
            holder.setText(R.id.title, s);
        }
    }

}
