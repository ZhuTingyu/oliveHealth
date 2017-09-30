package com.olive.ui.refund;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.biz.util.IntentBuilder;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.ProductMultiChooseAdapter;
import com.olive.ui.refund.viewModel.ChooseRefundGoodViewModel;

import java.util.ArrayList;

/**
 * Created by TingYu Zhu on 2017/7/30.
 */

public class ChooseRefundGoodsFragment extends BaseErrorFragment implements ProductMultiChooseAdapter.onCheckClickListener, ProductMultiChooseAdapter.onModifyNumberListener {
    private XRecyclerView recyclerView;
    private ProductMultiChooseAdapter adapter;
    private ChooseRefundGoodViewModel viewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ChooseRefundGoodViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_with_button_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.title_apply_refund));
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductMultiChooseAdapter();
        viewModel.setAdapter(adapter);
        adapter.setOnModifyNumberListener(this);
        adapter.setOnCheckClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(() -> {
            viewModel.chooseRefundProductsList(productEntities -> {
                adapter.setNewData(productEntities);
                recyclerView.setRefreshing(false);
            });
        });
        viewModel.chooseRefundProductsList(productEntities -> {
            adapter.setNewData(productEntities);
        });


        findViewById(R.id.btn_sure).setOnClickListener(v -> {
            Intent intent = new Intent();
            getActivity().setResult(0, intent);
            intent.putParcelableArrayListExtra(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) viewModel.getSelectedProducts());
            getActivity().finish();
        });

    }

    @Override
    public void checkBoxClick(CheckBox checkBox, int position) {

    }

    @Override
    public void add(ProductEntity productEntity) {
        viewModel.countAdd(productEntity);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void min(ProductEntity productEntity) {
        viewModel.countMin(productEntity);
        adapter.notifyDataSetChanged();
    }
}
