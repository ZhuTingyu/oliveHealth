package com.warehourse.app.ui.cart;

import com.biz.base.BaseLazyFragment;
import com.biz.util.DialogUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.PriceUtil;
import com.biz.util.Utils;
import com.biz.widget.recyclerview.XRecyclerView;
import com.warehourse.app.R;
import com.warehourse.app.event.CartStatusChangeEvent;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.CartEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.base.EmptyViewHolder;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.ui.preview.OrderPreviewFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Title: CartFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:11/05/2017  10:34
 *
 * @author johnzheng
 * @version 1.0
 */

public class CartFragment extends BaseLazyFragment {

    private View mLayout,mParentView;
    private TextView mTitleTotal;
    private TextView mTitlePromo;
    private TextView mBtnPay;
    private View mLayoutDelete;
    private AppCompatCheckBox mCheckboxAll;
    private TextView mBtnDel;
    private XRecyclerView mRecyclerView;
    private CartAdapter mAdapter;
    private CharSequence done;
    private CartViewModel mViewModel;
    private View mProgressView, mLoadingView;
    private ViewGroup rootView;
    private boolean isEdit=false;


    public void onEventMainThread(LoginEvent event) {
        setLoginState(UserModel.getInstance().isLogin());
    }

    public void onEventMainThread(CartStatusChangeEvent event) {
        setHasLoaded(false);
        setLoginState(UserModel.getInstance().isLogin());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        mViewModel = new CartViewModel(this);
        initViewModel(mViewModel);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void lazyLoad() {
        setLoginState(UserModel.getInstance().isLogin());
    }


    private void setTitleTotal(long price) {
        if (mTitleTotal != null) {
            mTitleTotal.setText(getString(R.string.text_total) + "" + PriceUtil.formatRMB(price));
        }
    }

    private void setTitlePromo(long totalPrice, long freePrice) {
        if (mTitlePromo != null) {
            String str = getString(R.string.text_price_total) + "" + PriceUtil.formatRMB(totalPrice);
            if (freePrice > 0) {
                str = str + "    " + getString(R.string.text_price_free) + PriceUtil.formatRMB(freePrice);
            }
            mTitlePromo.setText(str);
        }
    }

    private void loginCart() {
        View view = View.inflate(getContext(), R.layout.view_nologin_layout, null);
        EmptyViewHolder holder = new EmptyViewHolder(view);
        holder.btnEmpty.setOnClickListener(LoginActivity::goLogin);
        mAdapter.setEmptyView(view);
    }

    private void emptyCart() {
        addMenu(true);
        mParentView.setVisibility(View.GONE);
        mLayout.setVisibility(View.GONE);
        mLayoutDelete.setVisibility(View.GONE);
        EmptyViewHolder holder = EmptyViewHolder.createHolder(getContext())
                .setIcon(R.drawable.vector_empty_cart)
                .setTitle(R.string.text_no_product_in_cart);
        mAdapter.setEmptyView(holder.itemView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    void initView() {
        rootView = getView(R.id.frame_layout);
        initProgressLayout();
        mToolbar.setNavigationIcon(null);
        mToolbar.getLayoutParams().height = Utils.dip2px(48);
        mToolbar.setMinimumHeight(Utils.dip2px(48));
        done = Html.fromHtml("<font color='#FA3946'>" + getString(R.string.action_done) + "</font>");
        mAppBarLayout.setPadding(0, Utils.getStatusBarHeight(getActivity()), 0, 0);

        setTitle(R.string.title_cart);
        addMenu(true);

        mLayout = findViewById(R.id.layout);
        mTitleTotal = findViewById(R.id.title_total);
        mTitlePromo = findViewById(R.id.title_promo);
        mBtnPay = findViewById(R.id.btn_pay);
        mLayoutDelete = findViewById(R.id.layout_delete);
        mParentView= (View) mLayoutDelete.getParent();
        mCheckboxAll = findViewById(R.id.checkbox_all);
        mBtnDel = findViewById(R.id.btn_del);
        mRecyclerView = findViewById(R.id.list);
        mAdapter = new CartAdapter();
        mParentView.setVisibility(View.GONE);
        mBtnPay.setOnClickListener(v -> {
            ArrayList<ProductEntity> ids = mAdapter.getPayList();
            if (ids == null || ids.size() == 0) {
                DialogUtil.createDialogView(getBaseActivity(), R.string.text_dialog_pay_cart_item);
                return;
            }
            IntentBuilder.Builder().putExtra(IntentBuilder.KEY_DATA, ids)
                    .startParentActivity(getActivity(), OrderPreviewFragment.class);
        });

        mBtnDel.setOnClickListener(v -> {
            List<String> ids = mAdapter.getCheckedIds();
            if (ids == null || ids.size() == 0) {
                DialogUtil.createDialogView(getBaseActivity(), R.string.text_dialog_checked_cart_item);
                return;
            }
            setProgressVisible(true);
            mViewModel.deleteCart(ids, this::setCartData);
        });
        mAdapter.setCheckedAllListener(isAll -> {
            mCheckboxAll.setChecked(isAll);
        });

        mAdapter.setRemoveOnClickListener(v -> {
            ProductEntity productEntity = (ProductEntity) v.getTag();
            setProgressVisible(true);
            mViewModel.deleteCart(productEntity.getProductId(), this::setCartData);
        });
        mAdapter.setAddOnClickListener(v -> {
            ProductEntity productEntity = (ProductEntity) v.getTag();
            if (mAdapter.checkAdd(v, productEntity, productEntity.quantity)) {
                setProgressVisible(true);
                mViewModel.updateCart(productEntity.getProductId(), productEntity.quantity + 1, this::setCartData);
            }
        });

        mAdapter.setMinOnClickListener(v -> {
            ProductEntity productEntity = (ProductEntity) v.getTag();
            if (mAdapter.checkMin(v, productEntity, productEntity.quantity)) {
                setProgressVisible(true);
                mViewModel.updateCart(productEntity.getProductId(), productEntity.quantity - 1, this::setCartData);
            }
        });
        mAdapter.setEditCountListener((productId, count) -> {
            setProgressVisible(true);
            mViewModel.updateCart(productId, count, this::setCartData);
        });

        mRecyclerView.setAdapter(mAdapter);
        emptyCart();
        mAdapter.setEnableLoadMore(false);
        addItemDecorationLine(mRecyclerView.getRecyclerView());

        mCheckboxAll.setOnClickListener(v -> {
            if (mAdapter != null)
                mAdapter.setCheckedAll(mCheckboxAll.isChecked());
        });
        mAdapter.loadMoreEnd(false);

        mRecyclerView.setRefreshListener(this::refresh);

    }

    private void addMenu(boolean isEmpty) {
        mToolbar.getMenu().clear();
        if (isEmpty) {
            return;
        }
        mToolbar.getMenu().add(0, 5, 0,  isEdit?done:getString(R.string.action_edit))
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 5) {
                mParentView.setVisibility(View.VISIBLE);
                if(isEdit){
                    isEdit=false;
                    mLayoutDelete.setVisibility(View.GONE);
                    mLayout.setVisibility(View.VISIBLE);
                    item.setTitle(Html.fromHtml(getString(R.string.action_edit)));
                    mAdapter.setShowChecked(isEdit);
                }else {
                    isEdit=true;
                    item.setTitle(done);
                    mLayoutDelete.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.GONE);
                    mAdapter.setShowChecked(isEdit);
                }
            }
            return false;
        });
    }

    private void setLoginState(boolean isLogin) {
        mParentView.setVisibility(!isLogin ? View.GONE : View.VISIBLE);
        mLayout.setVisibility(!isLogin ? View.GONE : View.VISIBLE);
        mLayoutDelete.setVisibility(View.GONE);
        if (mToolbar!=null && mToolbar.getMenu().findItem(5)!=null) {
            mToolbar.getMenu().findItem(5).setVisible(isLogin);
        }
        if (!isLogin) {
            mAdapter.setNewData(Lists.newArrayList());
            loginCart();
            mRecyclerView.setEnabled(false);
        } else {
            mLayout.postDelayed(()->{
                setProgressVisible(true);
                refresh();
            },500);
        }
    }

    private void refresh() {
        mViewModel.getCartList(this::setCartData);
    }

    private void setCartData(CartEntity cartEntity) {
        mRecyclerView.setEnabled(true);
        setProgressVisible(false);
        if (cartEntity != null) {
            setTitleTotal(cartEntity.payAmount);
            setTitlePromo(cartEntity.orderAmount, cartEntity.freeAmount);
        }
        if (mAdapter != null && cartEntity != null) {
            if (cartEntity.items == null || cartEntity.items.size() == 0) {
                isEdit=false;
                mAdapter.setShowChecked(isEdit);
                mAdapter.setNewData(Lists.newArrayList());
                emptyCart();
            } else {
                addMenu(false);
                mParentView.setVisibility(View.VISIBLE);
                if(isEdit){
                    mLayout.setVisibility(View.GONE);
                    mLayoutDelete.setVisibility(View.VISIBLE);
                }else {
                    mLayout.setVisibility(View.VISIBLE);
                    mLayoutDelete.setVisibility(View.GONE);
                }
                mAdapter.setNewData(cartEntity.items);
            }
            mAdapter.isCheckedAll();
        }
        setHasLoaded(true);
    }

    @Override
    public void error(String error) {
        setProgressVisible(false);
        mRecyclerView.setEnabled(true);

        emptyCart();
        if (mAdapter.getData().size() > 0) {
            super.error(error);
        }
    }


    protected void initProgressLayout() {
        if (mProgressView == null) {
            mProgressView = LayoutInflater.from(getContext())
                    .inflate(R.layout.loading_layout, rootView
                            , false);
            mProgressView.setOnClickListener(v -> {
            });
            mLoadingView = mProgressView.findViewById(R.id.loading_view);
            setProgressVisible(false);
            rootView.addView(mProgressView);
        }
    }

    public void setProgressVisible(boolean show) {
        if (mProgressView != null) {

               mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
               mLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }
}
