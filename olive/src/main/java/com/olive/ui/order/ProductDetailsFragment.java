package com.olive.ui.order;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseActivity;
import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IdsUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.ListUtil;
import com.biz.util.Lists;
import com.biz.util.LogUtil;
import com.biz.util.PriceUtil;
import com.biz.util.RxUtil;
import com.biz.util.TimeUtil;
import com.biz.util.ToastUtils;
import com.biz.util.Utils;
import com.biz.widget.CountEditText;
import com.biz.widget.CustomDraweeView;
import com.biz.widget.banner.ConvenientBanner;
import com.biz.widget.recyclerview.XRecyclerView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.holder.ImageHolderView;
import com.olive.ui.main.cart.CartFragment;
import com.olive.ui.order.viewModel.ProductDetailViewModel;
import com.olive.ui.service.CustomerServicesFragment;
import com.olive.util.LoadImageUtil;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class ProductDetailsFragment extends BaseErrorFragment {

    private XRecyclerView recyclerView;
    private ProductAdapter adapter;
    private static final int TYPE_CART = 1001;
    private static final int TYPE_BUY = 1002;

    private ProductDetailViewModel viewModel;

    private ImageView icCart;

    View headView;
    WebView webView;
    SwipeRefreshLayout refreshLayout;

    private ProductEntity productEntity;

    public static void startProductDetailsFragment(Activity activity, String productNo) {
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_VALUE, productNo)
                .startParentActivity(activity, ProductDetailsFragment.class);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ProductDetailViewModel(context);
        initViewModel(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_product_details));
        initView();
        initData();
        initBelowLayout();

        mToolbar.getMenu().clear();
        mToolbar.getMenu().add(getString(R.string.text_favorites)).setIcon(R.drawable.vector_like)
                .setOnMenuItemClickListener(item -> {
                    viewModel.addProductFavorites(s -> {
                    });
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }


    private void initData() {
        setProgressVisible(true);
        viewModel.getProductDetail(productEntity -> {
            this.productEntity = productEntity;
            initHeadViewAndDetails();
            setProgressVisible(false);
            refreshLayout.setRefreshing(false);
        });

        viewModel.getRelevanceProductList(productEntities -> {
            if (productEntities.isEmpty()) {
                findViewById(R.id.rl_relevance).setVisibility(View.GONE);
                findViewById(R.id.rl_1).setVisibility(View.GONE);
            } else {
                adapter.setNewData(productEntities);
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(R.layout.item_cart_product_layout, getContext());
        adapter.setViewModel(viewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);

        headView = findViewById(R.id.head);

        webView = findViewById(R.id.webView);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(com.biz.http.R.color.green_light, com.biz.http.R.color.orange_light,
                com.biz.http.R.color.blue_light, com.biz.http.R.color.red_light);
        refreshLayout.setOnRefreshListener(() -> {
            initData();
        });


        findViewById(R.id.left).setOnClickListener(v -> {
            recyclerView.smoothScrollBy(-recyclerView.getWidth(), 0);
        });

        findViewById(R.id.right).setOnClickListener(v -> {
            recyclerView.smoothScrollBy(recyclerView.getWidth(), 0);
        });

        findViewById(R.id.btn_one_key_join).setOnClickListener(v -> {
            viewModel.setAddCartRelevanceProductList();
            viewModel.addCart(s -> {
                ToastUtils.showLong(getContext(), s);
                icCart.setImageResource(R.drawable.vector_cart_have_goods);
            });
        });

    }

    private void initHeadViewAndDetails() {
        BaseViewHolder headHolder = new BaseViewHolder(headView);
        if (productEntity.salePrice == 0) {
            headHolder.findViewById(R.id.icon_label).setVisibility(View.GONE);
        }
        headHolder.setText(R.id.tv_product_name, productEntity.name);
        headHolder.setText(R.id.tv_product_advice, productEntity.intro);
        TextView price = headHolder.getView(R.id.tv_product_price);
        TextView priceOld = headHolder.findViewById(R.id.tv_product_price_old);
        if (productEntity.salePrice == 0) {
            price.setText(PriceUtil.formatRMB(productEntity.originalPrice) + "/" + productEntity.unit);
            priceOld.setVisibility(View.GONE);
        } else {
            price.setText(PriceUtil.formatRMB(productEntity.salePrice) + "/" + productEntity.unit);
            priceOld.setText(PriceUtil.formatRMB(productEntity.originalPrice) + "/" + productEntity.unit);
            priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        headHolder.setText(R.id.tv_product_specification, getString(R.string.text_product_specification, productEntity.standard));
        TextView saleEndTime = headHolder.findViewById(R.id.tv_product_sale_end_date);
        if (productEntity.saleEndDate == 0) {
            saleEndTime.setVisibility(View.GONE);
        } else {
            saleEndTime.setText(getString(R.string.text_product_sale_end_time, TimeUtil.format(productEntity.saleEndDate, TimeUtil.FORMAT_YYYYHHMM_CHICESEC)));
        }


        ConvenientBanner banner = headHolder.findViewById(R.id.banner);
        View indicator = banner.findViewById(com.bigkoo.convenientbanner.R.id.loPageTurningPoint);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        lp.bottomMargin = Utils.dip2px(30);
        banner.setPages(
                () -> new ImageHolderView(Utils.dip2px(getActivity(), 180), ScalingUtils.ScaleType.FIT_XY), IdsUtil.getList(productEntity.images, ",", false))
                .startTurning(3000)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focus})
                .setPointViewVisible(true)
                .setCanLoop(true);

        initWebView();


    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        // 是否使用内置缩放机制
        settings.setBuiltInZoomControls(true); // 是否使用内置缩放机制
        settings.setDisplayZoomControls(false);  // 是否显示内置缩放控件
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        webView.loadDataWithBaseURL(null, productEntity.desc, "text/html", "utf-8", null);
        webView.setLongClickable(true);

        webView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_POINTER_2_DOWN:
                    if (event.getPointerCount() == 2) {
                        webView.getParent().requestDisallowInterceptTouchEvent(true);

                    } else {
                        webView.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
            }

            return false;
        });

    }


    private void initBelowLayout() {

        icCart = findViewById(R.id.ic_cart);

        viewModel.getCartProductList(productEntities -> {
            if (!productEntities.isEmpty()) {
                icCart.setImageResource(R.drawable.vector_cart_have_goods);
            }
        });

        findViewById(R.id.btn_contact).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), CustomerServicesFragment.class);
        });

        findViewById(R.id.btn_cart).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_BOOLEAN, true)
                    .startParentActivity(getActivity(), CartFragment.class, false);
        });

        findViewById(R.id.btn_join_cart).setOnClickListener(v -> {
            showBottomSheet(TYPE_CART);
        });

        findViewById(R.id.btn_buy_now).setOnClickListener(v -> {
            showBottomSheet(TYPE_BUY);
        });
    }

    private void showBottomSheet(int type) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_product_layout);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
        dialog.show();
        initBottomSheet(dialog, type);
    }


    private void initBottomSheet(BottomSheetDialog dialog, int type) {
        AppCompatImageView ivClose = (AppCompatImageView) dialog.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(l -> dialog.dismiss());
        CustomDraweeView customDraweeView = (CustomDraweeView) dialog.findViewById(R.id.cdv_product);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvPrice = (TextView) dialog.findViewById(R.id.tv_price);
        CountEditText edCount = (CountEditText) dialog.findViewById(R.id.ed_count);
        productEntity.quantity = productEntity.orderCardinality;


        customDraweeView.setImageURI(Uri.parse(productEntity.imgLogo));

        tvTitle.setText(productEntity.name);
        tvPrice.setText(PriceUtil.formatRMB(viewModel.getPrice()));
        TextView tvTotal = (TextView) dialog.findViewById(R.id.tv_total_price);

        edCount.setText(productEntity.quantity + "");

        bindUi(RxUtil.textChanges(edCount), viewModel.setProductNumberAndCalculateTotalPrice(aLong -> {
            tvTotal.setText(getString(R.string.text_price_total, PriceUtil.formatRMB(aLong)));
        }));

        /**
         * 数量编辑后，判断数量是否合理
         */
        KeyboardVisibilityEvent.setEventListener(getActivity(), isOpen -> {
            if (!isOpen) {
                String edCountString = edCount.getText().toString();
                if (!edCountString.isEmpty()) {
                    int number = Integer.valueOf(edCountString);
                    productEntity.quantity = number;
                    viewModel.setProductNumberValid(number, s1 -> {
                        edCount.setText(s1);
                    });
                }else {
                    edCount.setText(String.valueOf(productEntity.orderCardinality));
                }
            }
        });




        //减少orderCardinality
        AppCompatImageView iconLess = (AppCompatImageView) dialog.findViewById(R.id.icon_less);
        iconLess.setOnClickListener(l -> {
            if (productEntity.quantity <= productEntity.orderCardinality) {
                productEntity.quantity = productEntity.orderCardinality;
                error(getString(R.string.message_add_cart_min_number, productEntity.orderCardinality + ""));
            } else {
                productEntity.quantity -= productEntity.orderCardinality;
            }
            edCount.setText(String.valueOf(productEntity.quantity));
        });

        //增多
        AppCompatImageView iconMore = (AppCompatImageView) dialog.findViewById(R.id.icon_more);
        iconMore.setOnClickListener(l -> {
            productEntity.quantity += productEntity.orderCardinality;
            edCount.setText(String.valueOf(productEntity.quantity));
        });

        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(l -> {
            switch (type) {
                case TYPE_CART:
                    viewModel.setAddCartCurrentProduct();
                    viewModel.addCart(s -> {
                        ToastUtils.showLong(getBaseActivity(), s);
                        dialog.dismiss();
                        icCart.setImageResource(R.drawable.vector_cart_have_goods);
                    });

                    break;
                case TYPE_BUY:
                    IntentBuilder.Builder()
                            .putParcelableArrayListExtra(IntentBuilder.KEY_DATA, (ArrayList<? extends Parcelable>) viewModel.setAddCartCurrentProduct())
                            .putExtra(IntentBuilder.KEY_VALUE, viewModel.getTotalPrice())
                            .startParentActivity(getActivity(), CheckOrderInfoFragment.class, true);
                    getActivity().finish();
                    break;
            }
        });
    }
}



