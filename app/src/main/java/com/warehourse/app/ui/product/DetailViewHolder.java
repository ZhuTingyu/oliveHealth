package com.warehourse.app.ui.product;


import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.PriceUtil;
import com.biz.widget.BadgeView;
import com.biz.widget.CustomDraweeView;
import com.biz.widget.banner.LoopRecyclerViewPager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.OrderEntity;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.util.LoadImageUtil;

import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import rx.Observable;


 class DetailViewHolder extends BaseViewHolder {

    public AppBarLayout appBarLayout;
    public LoopRecyclerViewPager viewpager;
    public TextView title;
    public TextView textDesc;
    public TextView textMin;
    public TextView textMax;
    public TextView textPrice;
    public TextView textPriceSuggest;
    public TextView textPriceMarket;
    public LinearLayout layoutInfo;
    public LinearLayout layoutPic;
    public LinearLayout btnContact;
    public LinearLayout btnCart;
    public TextView btn2;
    public LinearLayout layout;
    public LinearLayout layoutPromote;
    public BadgeView cartText;
    public TextView benefitText;

    public DetailViewHolder(View view) {
        super(view);
        appBarLayout = findViewById(R.id.appbar);
        viewpager = findViewById(R.id.viewpager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL,
                false);
        viewpager.setLayoutManager(layoutManager);
        viewpager.setSinglePageFling(true);
        viewpager.setPageIndicator(getView(R.id.indicator));
        title = findViewById(R.id.title);
        textDesc = findViewById(R.id.text_desc);
        textPrice = findViewById(R.id.text_price);
        benefitText = findViewById(R.id.text_benefit);
        textPriceMarket = findViewById(R.id.text_price_market);
        textPriceSuggest = findViewById(R.id.text_price_suggest);
        layoutPromote = findViewById(R.id.layout_promote);
        layoutInfo = findViewById(R.id.layout_info);
        layoutPic = findViewById(R.id.layout_pic);

        layout = findViewById(R.id.layout);
        btnContact = findViewById(R.id.btn_contact);
        btnCart = findViewById(R.id.btn_cart);
        cartText = findViewById(R.id.badge_view);
        btn2 = findViewById(R.id.btn_2);

        textMax = findViewById(R.id.text_max_number);
        textMin = findViewById(R.id.text_min_number);

        textPriceSuggest.setCompoundDrawables(
                DrawableHelper.getDrawableWithBounds(textPriceSuggest.getContext(), R.drawable.ic_suggest_price)
                , null, null, null);
//        benefitText.setCompoundDrawables(
//                DrawableHelper.getDrawableWithBounds(benefitText.getContext(), R.drawable.ic_benefit_24),
//                null, null, null
//        );
    }

    public void onDestroyView() {
        //convenientBanner.stopTurning();
    }

    protected void createPictureView(ViewGroup parent, String url) {
        if (TextUtils.isEmpty(url)) return;
        DisplayMetrics dm = parent.getResources().getDisplayMetrics();

        CustomDraweeView draweeView = new CustomDraweeView(parent.getContext());
        draweeView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        draweeView.setSize(dm.widthPixels, dm.widthPixels);
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = dm.widthPixels * imageInfo.getHeight() / imageInfo.getWidth();
                draweeView.setLayoutParams(new LinearLayout.LayoutParams(
                        dm.widthPixels,
                        height));
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
            }
        };
        DraweeController controller = Fresco.getDraweeControllerBuilderSupplier().get()
                .setCallerContext(draweeView.getContext())
                .setControllerListener(controllerListener)
                .setOldController(draweeView.getController())
                .setUri(LoadImageUtil.Builder().load(url).getImageLoadUri())
                .build();
        draweeView.setController(controller);
        parent.addView(draweeView);
    }

    protected BaseViewHolder createPropertyView(ViewGroup parent, String title, String text) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_info_layout, parent, false);
        parent.addView(view);
        BaseViewHolder holder = new BaseViewHolder(view);
        holder.setText(R.id.title_left, title == null ? "" : title);
        holder.setText(R.id.title_right, text == null ? "" : text);
        return holder;
    }

    public void bindData(ProductEntity entity) {
        layoutPromote.removeAllViews();

        title.setText(entity.getName());
        textDesc.setText(entity.brief);
        btn2.setEnabled(!entity.isOutShelf());
        Observable.just(entity.stock)
                .map(s -> {
                    return itemView.getResources().getString(R.string.text_max_number) + s;
                })
                .subscribe(benefitText::setText);

        Observable.from(entity.getIntroImages()).forEach(s -> createPictureView(layoutPic, s));

        Observable.from(entity.getProperties())
                .forEach(e -> {
                    createPropertyView(layoutInfo, e.title, e.value);
                });


        if (UserModel.getInstance().isLogin()) {
            textPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
            textPrice.setText(PriceUtil.formatRMB(entity.salePrice));
        } else {
            textPrice.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textPrice.setText(R.string.text_login_check);
            textPrice.setTypeface(Typeface.DEFAULT);
        }

    }


}
