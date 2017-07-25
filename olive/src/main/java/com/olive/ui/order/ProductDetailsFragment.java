package com.olive.ui.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.Lists;
import com.biz.util.ToastUtils;
import com.biz.util.Utils;
import com.biz.widget.banner.ConvenientBanner;
import com.biz.widget.recyclerview.XRecyclerView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.olive.R;
import com.olive.model.entity.ProductEntity;
import com.olive.ui.adapter.ProductAdapter;
import com.olive.ui.holder.ImageHolderView;
import com.olive.util.LoadImageUtil;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/7/24.
 */

public class ProductDetailsFragment extends BaseFragment {

    private XRecyclerView recyclerView;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.text_product_details));
        initHeadView((ViewGroup) view,new ProductEntity());
        initView(view);
        recyclerView.setFocusable(false);
    }

    private void initHeadView(ViewGroup view, ProductEntity product) {
        View headView = findViewById(view, R.id.head);
        BaseViewHolder headHolder = new BaseViewHolder(headView);
        headHolder.setText(R.id.tv_product_name,"食品名称");
        headHolder.setText(R.id.tv_product_advice,"建议：加大财政投入，进一步健全和完善保健品的监管保障机制。财政投入是保健品检验、检测设备的保障");
        headHolder.setText(R.id.tv_product_price,"¥ 1130.00 /瓶");
        headHolder.setText(R.id.tv_product_price_old,"¥ 1130.00 /瓶");
        headHolder.setText(R.id.tv_product_specification,"商品规格：225g/件");
        headHolder.setText(R.id.tv_product_sale_end_date,"该商品促销截止时间为2017年6月20日");
        headHolder.findViewById(R.id.btn_one_key_join).setOnClickListener(v -> {
            ToastUtils.showLong(getContext(),"一键加入");
        });

        ConvenientBanner banner = headHolder.findViewById(R.id.banner);
        View indicator = banner.findViewById(com.bigkoo.convenientbanner.R.id.loPageTurningPoint);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) indicator.getLayoutParams();
        lp.bottomMargin= Utils.dip2px(30);
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

    private void initView(View view) {
        recyclerView = findViewById(view, R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ProductAdapter(R.layout.item_cart_product_layout);
        adapter.setNewData(Lists.newArrayList("","","",""));
        recyclerView.setAdapter(adapter);
        LoadImageUtil.Builder()
                .load("http://img13.360buyimg.com/imgzone/jfs/t6517/304/1921907774/343777/df918f69/595a01f6Ne19fc737.jpg").http().build()
                .displayImage(findViewById(view, R.id.below_icon));
    }
}
