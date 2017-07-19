package com.warehourse.app.ui.product;
/**
 * Created by johnzheng on 3/17/16.
 */

import com.biz.base.BaseFragment;
import com.biz.util.DialogUtil;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.warehourse.app.R;
import com.warehourse.app.event.CartAddEvent;
import com.warehourse.app.event.CartCountEvent;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.model.entity.ProductEntity;
import com.warehourse.app.model.entity.SalePromotionEntity;
import com.warehourse.app.ui.cart.CartAdapter;
import com.warehourse.app.ui.cart.CartFragment;
import com.warehourse.app.ui.home.BannerAdapter;
import com.warehourse.app.ui.home.HomeFragment;
import com.warehourse.app.ui.main.MainActivity;
import com.warehourse.app.ui.search.SearchActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


@SuppressWarnings("deprecation")
public class ProductDetailFragment extends BaseFragment {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public static void startDetail(Activity context, String id) {
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_ID, id)
                .startParentActivity(context, ProductDetailFragment.class, false);
    }

    public static void startDetail(Activity context, ProductEntity entity) {
        IntentBuilder.Builder()
                .putExtra(IntentBuilder.KEY_ID, entity.id)
                .putExtra(IntentBuilder.KEY_DATA, entity)
                .startParentActivity(context, ProductDetailFragment.class, false);
    }

    DetailViewHolder holder;
    private ProductDetailViewModel mViewModel;

    public void onEventMainThread(LoginEvent event) {
        refresh();
    }

    private void refresh() {
        setProgressVisible(true);
        mViewModel.detail(entity -> {
            setProgressVisible(false);
            setEntity(entity);
        });
    }

    public void onEventMainThread(UserEvent userEvent) {
        refresh();
    }


    public void onEventMainThread(CartCountEvent event) {
        if (event != null) {
            holder.cartText.setText("" + event.count);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mViewModel = new ProductDetailViewModel(this);
        initViewModel(mViewModel);
        EventBus.getDefault().register(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pull_up_viewpager_layout, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        holder = new DetailViewHolder(view);
        holder.viewpager.setEnabled(false);
        holder.appBarLayout.addOnOffsetChangedListener(
                (AppBarLayout appBarLayout, int verticalOffset) -> {
                    if (Math.abs(verticalOffset) < Utils.dip2px(320 - 56 - 30))
                        mToolbar.setTitle("");
                    else mToolbar.setTitle(holder.title.getText());
                }
        );
        setTitle(R.string.text_product_detail);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);


        if (DrawableHelper.LOLLIPOP_GE)
            getBaseActivity().getWindow().setStatusBarColor(getColors(R.color.color_transparent));

        holder.btnCart.setOnClickListener(e -> {
            MainActivity.startMainWithAnim(getActivity(), 2);

            getActivity().finish();
            getActivity().overridePendingTransition(0, R.anim.right_out);
        });

        holder.btnContact.setOnClickListener(e -> {
            UserModel.getInstance().createContactDialog(getContext());
        });

        refresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        mCompositeSubscription.clear();
        mCompositeSubscription.add(getBannerSubscription());
    }

    @Override
    public void onPause() {
        super.onPause();
        mCompositeSubscription.clear();
    }

    private void setEntity(ProductEntity entity) {
        holder.bindData(entity);

        holder.btn2.setOnClickListener(e -> {
            UserModel.getInstance().createLoginDialog(getBaseActivity(), () -> {
                int count = entity.minQuantity <= 0 ? 1 : entity.minQuantity;
                if (CartAdapter.checkEditCount(e, entity, count)) {
                    setProgressVisible(true);
                    mViewModel.addCart(entity.getProductId(), count, cartEntity -> {
                        setProgressVisible(false);
                        //DialogUtil.createDialogView(getBaseActivity(),R.string.text_add_cart_ok);
                    });
                }
            });
        });
        if (entity.getImages().size() > 0) {
            BannerAdapter bannerAdapter = new BannerAdapter(320);
            bannerAdapter.setStringList(entity.getImages());
            mCompositeSubscription.clear();
            mCompositeSubscription.add(getBannerSubscription());
            holder.viewpager.setAdapter(bannerAdapter);
            holder.viewpager.setEnabled(true);
        }

        List<SalePromotionEntity> promoList = entity.getSalePromotionDetail();
        if(promoList!=null&&promoList.size()>0) {
            for (int i = 0; i < promoList.size(); i++) {
                PromoteViewHolder promoHolder = new PromoteViewHolder(holder.layoutPromote,
                        promoList.get(i));
//                promoHolder.title.setSingleLine(true);
//                promoHolder.title.setMaxLines(1);
                if (i==0) {
                    promoHolder.icon.setImageResource(R.drawable.ic_more_horiz_black_24dp);
                    bindUi(RxUtil.click(promoHolder.icon), s -> {
                        createPromotionDialog(entity.salePromotionDetail);
                    });
                } else promoHolder.icon.setVisibility(View.GONE);
            }
        }
    }

    private Subscription getBannerSubscription() {
        return Observable.interval(HomeFragment.TIME, HomeFragment.TIME, TimeUnit.SECONDS)
                .filter(s -> holder.viewpager.getAdapter() != null)
                .subscribe(s -> {
                    int currentIndex = holder.viewpager.getActualCurrentPosition();
                    if (++currentIndex == holder.viewpager.getAdapter().getItemCount()) {
                        holder.viewpager.smoothScrollToPosition(0);
                    } else {
                        holder.viewpager.smoothScrollToPosition(currentIndex);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        holder.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void createPromotionDialog(List<SalePromotionEntity> list) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_promo_layout, null);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            SalePromotionEntity info = list.get(i);
            PromoteViewHolder promoHolder = new PromoteViewHolder(
                    (ViewGroup) view.findViewById(R.id.layout_promote), info);
            if (!TextUtils.isEmpty(info.id)) {
                RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) promoHolder.title.getLayoutParams();
                layoutParams.rightMargin=Utils.dip2px(10);
//                promoHolder.icon.setImageResource(R.drawable.ic_arrow_right_gray);
                promoHolder.icon.setBackgroundDrawable(null);
                promoHolder.icon.setVisibility(View.GONE);
                promoHolder.itemView.setTag(info);
                promoHolder.itemView.setOnClickListener(v -> {
//                    if (bottomSheetDialog != null) {
//                        bottomSheetDialog.dismiss();
//                    }
//                    SalePromotionEntity entity = (SalePromotionEntity) v.getTag();
//                    SearchActivity.startSearch(v.getContext(), entity.name);
                });
            } else {
                promoHolder.icon.setBackgroundDrawable(null);
                promoHolder.itemView.setBackgroundDrawable(null);
            }
        }
        view.findViewById(R.id.btn_close).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }
}

