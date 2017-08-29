package com.olive.ui.main.my.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.AddressManageAdapter;

/**
 * Created by TingYu Zhu on 2017/7/27.
 */

public class AddressManageFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private AddressManageAdapter adapter;
    private AddressViewModel viewModel;

    public static int ADDRESS_RESULT = 0x234;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new AddressViewModel(context);
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
        mToolbar.setTitle(getString(R.string.title_manager_address));
        initView(view);
    }

    private void initView(View view) {
        recyclerView = findViewById(view, R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AddressManageAdapter(this);
        adapter.setViewModel(viewModel);
        adapter.bindToRecyclerView(recyclerView.getRecyclerView());
        adapter.setOnItemClickListener((baseQuickAdapter, view1, i) -> {
            Intent intent = new Intent();
            intent.putExtra(IntentBuilder.KEY_DATA, adapter.getItem(i));
            getBaseActivity().setResult(ADDRESS_RESULT,intent);
            getBaseActivity().finish();
        });
        viewModel.getAddressList(addressEntities -> {
            adapter.setNewData(addressEntities);
        });

        TextView btn = findViewById(view, R.id.btn_sure);
        btn.setText(getString(R.string.text_add_new_address));
        btn.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getBaseActivity(), EditAddressFragment.class, AddressManageAdapter.ADDRESS_ADD_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
