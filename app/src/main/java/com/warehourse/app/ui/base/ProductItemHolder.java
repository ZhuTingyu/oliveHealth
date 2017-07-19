package com.warehourse.app.ui.base;

import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.PriceUtil;
import com.biz.util.Utils;
import com.biz.widget.CustomDraweeView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.warehourse.app.R;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.ui.search.SearchActivity;
import com.warehourse.app.util.LoadImageUtil;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.other.OnClickableSpanListener;
import cn.iwgang.simplifyspan.other.SpecialGravity;
import cn.iwgang.simplifyspan.unit.SpecialClickableUnit;
import cn.iwgang.simplifyspan.unit.SpecialLabelUnit;

/**
 * Created by johnzheng on 3/11/16.
 */
@SuppressWarnings("deprecation")
public class ProductItemHolder extends BaseViewHolder implements OnClickableSpanListener{

    public CustomDraweeView icon, iconType;
    public TextView title;
    public TextView textTag;
    public TextView textPrice;
    public TextView textPromo;
    public TextView textPriceSuggest;
    private Drawable background =
            DrawableHelper.createShapeStrokeDrawable(R.color.color_transparent, R.color.color_d2d2d2, 1, 0);

    private String unLoginString;

    public ProductItemHolder(View itemView) {
        super(itemView);
        icon = findViewById(R.id.icon);
        if (icon!=null) {
            icon.setFailureImageScaleType(ScalingUtils.ScaleType.FIT_XY);
            icon.setPlaceholderScaleType(ScalingUtils.ScaleType.FIT_XY);
            icon.setOverlay(background);
        }
        iconType = findViewById(R.id.icon_type);
        if(iconType!=null){
            iconType.setActualImageScaleType(ScalingUtils.ScaleType.FIT_START);
            iconType.setPlaceholderId(R.color.color_transparent);
        }
        title = findViewById(R.id.title);
        textTag = findViewById(R.id.text_tag);
        textPrice = findViewById(R.id.text_price);
        textPromo = findViewById(R.id.text_promo);
        textPriceSuggest = findViewById(R.id.text_price_suggest);
        unLoginString = itemView.getResources().getString(R.string.text_login_check);
    }

    public void createTagView(String text) {
        TextView textView = new TextView(itemView.getContext());
        //textTag.addView(textView);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, Utils.dip2px(30));
        lp.rightMargin = Utils.dip2px(3);
        textView.setLayoutParams(lp);
        textView.setMaxWidth(Utils.dip2px(100));
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setTextColor(getColors(R.color.color_4a4a4a));
        textView.setPadding(Utils.dip2px(3), 0, Utils.dip2px(3), 0);
        textView.setBackgroundDrawable(DrawableHelper.newSelector(
                DrawableHelper.createShapeStrokeDrawable(R.color.color_transparent, R.color.color_divider, 5),
                DrawableHelper.createShapeStrokeDrawable(R.color.color_transparent_10, R.color.color_divider, 5)
        ));
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(e -> {

        });

    }

    public void createTagViews(List<String> list) {
        SimplifySpanBuild spanBuild = new SimplifySpanBuild(textTag.getContext(), textTag);
        int linkNorTextColor = getColors(R.color.color_4a4a4a);
        int linkPressBgColor = getColors(R.color.color_transparent_10);
        for (String s : list) {
            if (!TextUtils.isEmpty(s))
                spanBuild.appendMultiClickableSpecialUnit(
                        new SpecialClickableUnit(this)
                                .setNormalTextColor(linkNorTextColor).setPressBgColor(linkPressBgColor),
                        " ",
                        new SpecialLabelUnit(s.trim(),  linkNorTextColor, 15, Color.TRANSPARENT)
                                .showBorder(getColors(R.color.color_divider), 2).setLabelBgRadius(8)
                                .setPadding(10)
                                .setGravity(SpecialGravity.CENTER));

        }
        textTag.setText(spanBuild.build());
    }

    public void bindData(ProductEntity entity){
        LoadImageUtil.Builder().load(entity.getLogo()).build()
                .imageOptions(R.drawable.ic_product_default)
                .displayImage(icon);
        if(TextUtils.isEmpty(entity.getApartTagImage())) {
            iconType.setVisibility(View.GONE);
        }else {
            LoadImageUtil.Builder().load(entity.getApartTagImage()).origin(true).build()
                    .displayImage(iconType);
            iconType.setVisibility(View.VISIBLE);
        }
        title.setText(entity.name);
        createTagViews(entity.tags);
        textPrice.setText(UserModel.getInstance().isLogin()? PriceUtil.formatRMB(entity.salePrice): unLoginString);
        formatTags(textPromo, entity.getSupportPromotions());
    }

    @Override
    public void onClick(TextView tv, String clickText) {
        SearchActivity.startSearch(tv.getContext(), clickText);
    }

    private static void formatTags(TextView textView, List<String> tags){
        if(tags==null||tags.size()==0) return;
        SimplifySpanBuild spanBuild = new SimplifySpanBuild(textView.getContext(), textView);
        if (UserModel.getInstance().isLogin()) {
            for (String s : tags) {
                if(!TextUtils.isEmpty(s)) {
                    spanBuild.appendNormalText(" ");
                    spanBuild.appendSpecialUnit(new SpecialLabelUnit(s, Color.WHITE, 13, Color.RED)
                            .setLabelBgRadius(5)
                            .setPadding(10)
                            .setGravity(SpecialGravity.CENTER));
                }
            }
            spanBuild.appendNormalText("|");
        }
        CharSequence charSequence = spanBuild.build();
        textView.setText(charSequence);
    }
}
