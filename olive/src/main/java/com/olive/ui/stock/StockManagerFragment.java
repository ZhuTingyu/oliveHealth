package com.olive.ui.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.biz.base.BaseFragment;
import com.biz.util.Lists;
import com.biz.util.RxUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.StockManagerAdapter;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class StockManagerFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private StockManagerAdapter adapter;
    private StockViewModel viewModel;
    private EditText searchView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new StockViewModel(context);
        searchView = getView(getActivity(), R.id.edit_search);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StockManagerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(() -> {
            viewModel.getStockList(productEntities -> {
                adapter.setNewData(productEntities);
                recyclerView.setRefreshing(false);
            });
        });
        viewModel.getStockList(productEntities -> {
            adapter.setNewData(productEntities);
        });


        bindUi(RxUtil.textChanges(searchView), viewModel.setKey());
        searchView.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER)
                    && event.getAction() == KeyEvent.ACTION_UP) {
                v.clearFocus();
                String key = getSearchText();
                if (!TextUtils.isEmpty(key)) {
                    setProgressVisible(true);
                    viewModel.getStockList(o -> {
                        setProgressVisible(false);
                        adapter.setNewData(o);
                    });
                    dismissKeyboard();
                }else {
                    error(getString(R.string.message_input_search_key_word));
                }
            }
            return false;
        });

    }

    public String getSearchText() {
        return searchView.getText() == null ? "" : searchView.getText().toString();
    }
}
