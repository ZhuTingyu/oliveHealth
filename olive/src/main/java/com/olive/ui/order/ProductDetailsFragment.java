package com.olive.ui.order;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.PriceUtil;
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
import com.olive.util.LoadImageUtil;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_product_details));
        initHeadView((ViewGroup) view, new ProductEntity());
        initView(view);
        initBelowLayout();
        recyclerView.setFocusable(false);
    }

    private void initView(View view) {
        recyclerView = findViewById(view, R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(R.layout.item_cart_product_layout);
        adapter.setNewData(Lists.newArrayList(new ProductEntity(),new ProductEntity(),new ProductEntity(),new ProductEntity()));
        recyclerView.setAdapter(adapter);
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(findViewById(view, R.id.below_icon));
    }

    private void initHeadView(ViewGroup view, ProductEntity product) {
        View headView = findViewById(view, R.id.head);
        BaseViewHolder headHolder = new BaseViewHolder(headView);
        headHolder.setText(R.id.tv_product_name, "食品名称");
        headHolder.setText(R.id.tv_product_advice, "建议：加大财政投入，进一步健全和完善保健品的监管保障机制。财政投入是保健品检验、检测设备的保障");
        headHolder.setText(R.id.tv_product_price, "¥ 1130.00 /瓶");
        TextView priceOld = headHolder.findViewById(R.id.tv_product_price_old);
        priceOld.setText("¥ 1130.00 /瓶");
        priceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        headHolder.setText(R.id.tv_product_specification, "商品规格：225g/件");
        headHolder.setText(R.id.tv_product_sale_end_date, "该商品促销截止时间为2017年6月20日");
        headHolder.findViewById(R.id.btn_one_key_join).setOnClickListener(v -> {
            ToastUtils.showLong(getContext(), "一键加入");
        });

        ConvenientBanner banner = headHolder.findViewById(R.id.banner);
        View indicator = banner.findViewById(com.bigkoo.convenientbanner.R.id.loPageTurningPoint);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        lp.bottomMargin = Utils.dip2px(30);
        List list = Lists.newArrayList(
                "http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg",
                "http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg",
                "http://img.taopic.com/uploads/allimg/140326/235113-1403260G01561.jpg");
        banner.setPages(
                () -> new ImageHolderView(Utils.dip2px(getActivity(), 180), ScalingUtils.ScaleType.FIT_XY), list)
                .startTurning(3000)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focus})
                .setPointViewVisible(true)
                .setCanLoop(true);
    }



    private void initBelowLayout() {
        findViewById(R.id.btn_contact).setOnClickListener(v -> {

        });

        findViewById(R.id.btn_cart).setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(baseActivity, CartFragment.class, true);
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
        //productCount = mViewModel.getMinNumber();
        edCount.setText(productCount + "");

            /*if (mViewModel.getImageList() != null && mViewModel.getImageList().size() != 0) {
                customDraweeView.setImageURI(Uri.parse(mViewModel.getImageList().get(0)));
            }
            tvTitle.setText(mViewModel.getTitle());
            tvPrice.setText(PriceUtil.formatRMB(mViewModel.getPrice()));

            //减少
            AppCompatImageView iconLess = (AppCompatImageView) dialog.findViewById(R.id.icon_less);
            iconLess.setOnClickListener(l -> {
                if (productCount <= mViewModel.getMinNumber()) {
                    productCount = mViewModel.getMinNumber();
                    ToastUtils.showLong(getBaseActivity(), getString(R.string.text_min_buy_number, mViewModel.getMinNumber() + ""));
                } else {
                    productCount--;
                }
                edCount.setText(productCount + "");
                mViewModel.setTotalNum(productCount);
            });*/

        //增多
        AppCompatImageView iconMore = (AppCompatImageView) dialog.findViewById(R.id.icon_more);
        iconMore.setOnClickListener(l -> {
                /*if (productInfo.preSale!=8&&productCount+1>mViewModel.getShowNum()){
                    ToastUtils.showLong(getBaseActivity(),"库存不足");
                    return;
                }
                productCount++;
                edCount.setText(productCount + "");
                mViewModel.setTotalNum(productCount);*/
        });

        TextView tvTotal = (TextView) dialog.findViewById(R.id.tv_total_price);
        //tvTotal.setText(PriceUtil.formatRMB(mViewModel.getTotalPrice()));


        edCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                       /* productCount = Integer.parseInt(s.toString());
                        if (productInfo.preSale!=8&&productCount>productInfo.showQuantity){
                            productCount=productInfo.showQuantity;
                            edCount.setText(productCount+"");
                        }
                        BigDecimal num = BigDecimal.valueOf(productCount);
                        BigDecimal price = BigDecimal.valueOf(mViewModel.getPrice());
                        BigDecimal total = num.multiply(price);
                        tvTotal.setText(getString(R.string.text_total_, PriceUtil.formatRMB(total.longValue())));*/
                } catch (Exception e) {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        TextView tvConfirm = (TextView) dialog.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(l -> {
            switch (type) {
                case TYPE_CART:
                       /* if (productCount < mViewModel.getMinNumber()) {
                            ToastUtils.showLong(getBaseActivity(), getString(R.string.text_min_buy_number, mViewModel.getMinNumber() + ""));
                            edCount.setText(mViewModel.getMinNumber() + "");
                            return;
                        }
                        if (productInfo.preSale!=8&&productCount>productInfo.showQuantity){
                            return;
                        }
                        dialog.dismiss();
                        mViewModel.setCart(productId, productCount,s->{
                            //购物车不为空切换图片
                        });*/
                    //EventBus.getDefault().post(new CartUpdateEvent(productId,productCount));
                    ToastUtils.showLong(getBaseActivity(), "添加购物车成功");
                    break;
                case TYPE_BUY:
                        /*if (productCount < mViewModel.getMinNumber()) {
                            ToastUtils.showLong(getBaseActivity(), getString(R.string.text_min_buy_number, mViewModel.getMinNumber() + ""));
                            edCount.setText(mViewModel.getMinNumber() + "");
                            return;
                        }
                        if (productInfo.preSale!=8&&productCount>productInfo.showQuantity){

                            return;
                        }
                        dialog.dismiss();
                        productInfo.purchaseQuantity = productCount;
                        productInfos.add(productInfo);
                        IntentBuilder.Builder().putExtra(IntentBuilder.KEY_LIST, productInfos).startParentActivity(getActivity(), OrderConfirmFragment.class);
                        productInfos.clear();
                        break;*/
            }
        });
    }
}



