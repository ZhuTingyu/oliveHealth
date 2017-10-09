package com.olive.ui.main.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.biz.base.BaseLazyFragment;
import com.biz.util.IntentBuilder;
import com.biz.widget.recyclerview.XRecyclerView;
import com.olive.R;
import com.olive.event.UpdateCartEvent;
import com.olive.model.UserModel;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.ProductMultiChooseAdapter;
import com.olive.ui.login.LoginActivity;
import com.olive.ui.order.CheckOrderInfoFragment;
import com.olive.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

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

public class CartFragment extends BaseLazyFragment implements ProductMultiChooseAdapter.onCheckClickListener, ProductMultiChooseAdapter.onModifyNumberListener {

    private XRecyclerView recyclerView;
    private ProductMultiChooseAdapter adapter;
    private CartViewModel viewModel;
    private TextView priceTotal;
    private int productNumber;
    private boolean haveBack;

    private boolean isBuyAgain;
    private int buyAgainProductsNumber;

    private AppCompatImageView chooseAll;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new CartViewModel(context);
        initViewModel(viewModel);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            isBuyAgain = intent.getBooleanExtra(IntentBuilder.KEY_BOOLEAN_KUAIHE, false);
            buyAgainProductsNumber = intent.getIntExtra(IntentBuilder.KEY_VALUE, 0);
            haveBack = intent.getBooleanExtra(IntentBuilder.KEY_BOOLEAN, false);
        }
        EventBus.getDefault().register(this);
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
        initView();
        if (!haveBack) {
            mToolbar.setNavigationOnClickListener(null);
            mToolbar.setNavigationIcon(null);
        }else {
            getBaseActivity().setToolbarBackDrawable(mToolbar);
        }

    }

    @Override
    public void lazyLoad() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(UpdateCartEvent event) {
        updateCart();
        chooseAll.setSelected(false);
    }

    private void initView() {

        priceTotal = findViewById(R.id.price_total);

        findViewById(R.id.btn_go_pay).setOnClickListener(v -> {
            if (viewModel.isCanGoPay()) {
                IntentBuilder.Builder()
                        .putExtra(IntentBuilder.KEY_VALUE, viewModel.getTotalPrice())
                        .putParcelableArrayListExtra(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) viewModel.getSelectedProducts())
                        .startParentActivity(getActivity(), CheckOrderInfoFragment.class, true);
                if(isBuyAgain){
                    getActivity().finish();
                }
            } else {
                error(getString(R.string.message_not_choose_product));
            }

        });

        chooseAll = findViewById(R.id.choose_all);

        chooseAll.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                adapter.isChooseAll(true);
            } else {
                adapter.isChooseAll(false);
            }
        });

        mToolbar.getMenu().add(getString(R.string.text_action_delete))
                .setOnMenuItemClickListener(item -> {
                    setProgressVisible(true);
                    viewModel.removeCartProducts(s -> {
                        setProgressVisible(false);
                        adapter.deleteChoose();
                        adapter.setPrice(0);
                        chooseAll.setSelected(false);
                        if(adapter.getData().isEmpty()){
                            Utils.setEmptyView(adapter, getString(R.string.message_empty_cart));
                        }
                    });
                    return false;
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        initListView();

    }

    private void initListView() {
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.getRecyclerView();
        adapter = new ProductMultiChooseAdapter();
        adapter.setViewModel(viewModel);
        adapter.setTvPrice(priceTotal);
        adapter.setBuyAgainProductsNumber(buyAgainProductsNumber);
        adapter.setOnCheckClickListener(this);
        adapter.setOnModifyNumberListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshListener(() -> {
            viewModel.getCartProductList(productEntities -> {
                updateCart();
                recyclerView.setRefreshing(false);
            });
        });

        viewModel.setAdapter(adapter);

        updateCart();

    }

    private void setBuyAgain(List<ProductEntity> productEntityList) {
        Collections.reverse(productEntityList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        updateCart();
    }

    private void updateCart() {
        setProgressVisible(true);
        viewModel.getCartProductList(productEntities -> {
            setProgressVisible(false);
            if (isBuyAgain) {
                setBuyAgain(productEntities);
            }
            adapter.setNewData(productEntities);
            viewModel.getTotalPrice(aLong -> {
                adapter.setPrice(aLong);
            });
        });
    }

    @Override
    public void add(ProductEntity productEntity) {
        setProgressVisible(true);
        productNumber = productEntity.quantity + productEntity.orderCardinality;
        viewModel.setProductNo(productEntity.productNo);
        viewModel.setQuantity(productNumber);
        viewModel.updateProductNumber(s -> {
            updateCart();
        });
    }

    @Override
    public void min(ProductEntity productEntity) {
        if (productEntity.quantity <= productEntity.orderCardinality) {
            error(getString(R.string.message_add_cart_min_number, productEntity.orderCardinality + ""));
        } else {
            setProgressVisible(true);
            productNumber = productEntity.quantity - productEntity.orderCardinality;
            viewModel.setProductNo(productEntity.productNo);
            viewModel.setQuantity(productNumber);
            viewModel.updateProductNumber(s -> {
                updateCart();
            });
        }
    }

    @Override
    public void checkBoxClick(CheckBox checkBox, int position) {
        viewModel.getTotalPrice(aLong -> {
            adapter.setPrice(aLong);
        });
    }

    @Override
    public void error(int code, String error) {
        if(code == 1004){
            UserModel.getInstance().loginOut();
            IntentBuilder.Builder(getActivity(), LoginActivity.class)
                    .putExtra(IntentBuilder.KEY_TYPE, LoginActivity.TYPE_LOGIN_INVALID)
                    .startActivity();
            getActivity().finish();
            return;
        }
        super.error(code, error);
    }
}
