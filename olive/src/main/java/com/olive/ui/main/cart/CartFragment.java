package com.olive.ui.main.cart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biz.base.BaseLazyFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.ui.adapter.CartAdapter;
import com.olive.ui.order.CheckOrderInfoFragment;

/**
 * Title: CartFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:20/07/2017  10:57
 *
 * @author johnzheng
 * @version 1.0
 */

public class CartFragment extends BaseLazyFragment {

    private XRecyclerView recyclerView;
    private CartAdapter adapter;
    private CartViewModel viewModel;
    private TextView priceTotal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new CartViewModel(context);
    }

    @Override
    public void lazyLoad() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.action_cart));
        Boolean haveBack = getActivity().getIntent().getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
        if (!haveBack) {
            mToolbar.setNavigationOnClickListener(null);
            mToolbar.setNavigationIcon(null);
        }

        initView(view);
    }

    private void initView(View view) {

        priceTotal = findViewById(R.id.price_total);


        findViewById(view, R.id.btn_go_pay).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), CheckOrderInfoFragment.class, true);
        });



        initListView(view);


    }

    private void initListView(View view) {
        recyclerView = findViewById(view, R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.getRecyclerView();
        adapter = new CartAdapter();
        adapter.setFragment(this);
        adapter.setViewModel(viewModel);
        adapter.setTvPrice(priceTotal);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(() -> {
            viewModel.getCartProductList(productEntities -> {
                adapter.replaceData(productEntities);
                recyclerView.setRefreshing(false);
            });
        });

        viewModel.getCartProductList(productEntities -> {
            adapter.setNewData(productEntities);
        });

        viewModel.setAdapter(adapter);

        mToolbar.getMenu().add(getString(R.string.text_action_delete))
                .setOnMenuItemClickListener(item -> {
                    viewModel.removeCartProducts(s -> {
                        viewModel.getCartProductList(productEntities -> {
                            adapter.replaceData(productEntities);
                        });
                    });
                    return false;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }
}
