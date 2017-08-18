package com.olive.ui.order;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IdsUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.ListUtil;
import com.biz.util.Lists;
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
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.holder.ImageHolderView;
import com.olive.ui.main.cart.CartFragment;
import com.olive.ui.order.viewModel.ProductDetailViewModel;
import com.olive.util.LoadImageUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class ProductDetailsFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private ProductAdapter adapter;
    private int productCount;
    private static final int TYPE_CART = 1001;
    private static final int TYPE_BUY = 1002;

    private ProductDetailViewModel viewModel;
    private ProductEntity productEntity;

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
        initHeadData();
        initView();
        initBelowLayout();
        recyclerView.setFocusable(false);
    }


    private void initHeadData() {
        viewModel.getProductDetail(productEntity -> {
            this.productEntity = productEntity;
            initHeadView();
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(R.layout.item_cart_product_layout);
        recyclerView.setAdapter(adapter);

        viewModel.getRelevanceProductList(productEntities -> {
            adapter.setNewData(productEntities);
        });


        findViewById(R.id.left).setOnClickListener(v -> {
            recyclerView.smoothScrollBy(-recyclerView.getWidth(), 0);
        });

        findViewById(R.id.right).setOnClickListener(v -> {
            recyclerView.smoothScrollBy(recyclerView.getWidth(), 0);
        });

    }

    private void initHeadView() {
        View headView = findViewById(R.id.head);
        BaseViewHolder headHolder = new BaseViewHolder(headView);
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
        headHolder.setText(R.id.tv_product_sale_end_date, getString(R.string.text_product_sale_end_time, TimeUtil.format(productEntity.saleEndDate, TimeUtil.FORMAT_YYYYHHMM_CHICESEC)));
        headHolder.findViewById(R.id.btn_one_key_join).setOnClickListener(v -> {
            viewModel.setAddCartRelevanceProductList();
            viewModel.addCart(s -> {
                ToastUtils.showLong(getContext(), s);
            });
        });

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


        WebView webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, productEntity.desc, "text/html", "utf-8", null);

    }


    private void initBelowLayout() {
        findViewById(R.id.btn_contact).setOnClickListener(v -> {

        });

        findViewById(R.id.btn_cart).setOnClickListener(v -> {
            IntentBuilder.Builder()
                    .putExtra(IntentBuilder.KEY_BOOLEAN,true)
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
        productCount = productEntity.orderCardinality;
        edCount.setText(productCount + "");


        customDraweeView.setImageURI(Uri.parse(productEntity.imgLogo));

        tvTitle.setText(productEntity.name);
        tvPrice.setText(PriceUtil.formatRMB(viewModel.getPrice()));
        TextView tvTotal = (TextView) dialog.findViewById(R.id.tv_total_price);


        bindUi(RxUtil.textChanges(edCount), viewModel.setProductNumberAndCalculateTotalPrice(aLong -> {
            tvTotal.setText(getString(R.string.text_price_total,PriceUtil.formatRMB(aLong)));
        }));

        //减少
        AppCompatImageView iconLess = (AppCompatImageView) dialog.findViewById(R.id.icon_less);
        iconLess.setOnClickListener(l -> {
            if (productCount <= productEntity.orderCardinality) {
                productCount = productEntity.orderCardinality;
                error(getString(R.string.message_add_cart_min_number, productEntity.orderCardinality + ""));
            } else {
                productCount -= productEntity.orderCardinality;
            }
            edCount.setText(productCount + "");
        });

        //增多
        AppCompatImageView iconMore = (AppCompatImageView) dialog.findViewById(R.id.icon_more);
        iconMore.setOnClickListener(l -> {
            productCount += productEntity.orderCardinality;
            edCount.setText(productCount + "");
        });

        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(l -> {
            switch (type) {
                case TYPE_CART:
                    viewModel.setAddCartCurrentProduct();
                    viewModel.addCart(s -> {
                        ToastUtils.showLong(getBaseActivity(), s);
                        dialog.dismiss();
                    });

                    break;
                case TYPE_BUY:
                        IntentBuilder.Builder().putParcelableArrayListExtra(IntentBuilder.KEY_VALUE, (ArrayList<? extends Parcelable>) viewModel.setAddCartCurrentProduct())
                                .startParentActivity(getActivity(), CheckOrderInfoFragment.class, true);
                        break;
            }
        });
    }
}



