package com.warehourse.app.ui.my.settings;

import com.biz.base.BaseFragment;
import com.biz.base.BaseRecyclerViewAdapter;
import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.IntentBuilder;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.warehourse.app.R;
import com.warehourse.app.event.CartAddEvent;
import com.warehourse.app.event.LoginEvent;
import com.warehourse.app.event.UserEvent;
import com.warehourse.app.model.UserModel;
import com.warehourse.app.ui.coupon.PrivateFragment;
import com.warehourse.app.ui.login.LoginActivity;
import com.warehourse.app.ui.web.WebViewActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

/**
 * Title: SettingsFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company:博智维讯信息技术有限公司
 * CreateTime:15/05/2017  10:20
 *
 * @author johnzheng
 * @version 1.0
 */

public class SettingsFragment extends BaseFragment {

    RecyclerView mRecyclerView;
    private SettingsViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView(R.id.list);
        mToolbar.setTitle(R.string.title_settings);
        SettingsAdapter adapter = new SettingsAdapter(getContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .margin(Utils.dip2px(16), Utils.dip2px(16)).color(getColors(R.color.color_divider)).build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new SettingsViewModel(this);
        initViewModel(viewModel);
    }

    @SuppressWarnings("deprecation")
    class SettingsAdapter extends BaseRecyclerViewAdapter<String> {

        protected SettingsAdapter(Context context) {
            super(context);
            setList(getResources().getStringArray(R.array.array_settings));
        }

        @Override
        public int getItemViewType(int position) {
            if (getItem(position).equals(getString(R.string.text_settings_exit))) {
                return 1;
            }
            return super.getItemViewType(position);
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                LinearLayout linearLayout = new LinearLayout(parent.getContext());
                linearLayout.setPadding(Utils.dip2px(40), Utils.dip2px(8), Utils.dip2px(40), Utils.dip2px(8));
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                Button button = new Button(parent.getContext());
                button.setText(R.string.text_settings_exit);
                button.setTextAppearance(getActivity(), R.style.Button);
                button.setTextColor(getColors(R.color.base_color));
                button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2px(46)));
                button.setBackgroundDrawable(DrawableHelper.newSelector(
                        DrawableHelper.createShapeStrokeDrawable(R.color.color_transparent, R.color.base_color, 0),
                        DrawableHelper.createShapeStrokeDrawable(R.color.base_color_10, R.color.base_color, 0)
                ));
                button.setId(R.id.title_right);
                linearLayout.addView(button);
                return new BaseViewHolder(linearLayout);
            }
            return new BaseViewHolder(inflater(R.layout.item_settings_layout, parent));
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.setTextView(R.id.title, getItem(position));
            if (getItem(position).equals(getString(R.string.text_settings_clear))) {
                viewModel.getCacheSize(s -> {
                    holder.setTextView(R.id.text, s);

                });
                setTitleRight(holder, false);
                holder.itemView.setOnClickListener(e -> {
                    ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setProgress(100);
                    dialog.setMessage(getString(R.string.text_progress_clear_cache));
                    dialog.show();
                    dialog.setCancelable(false);
                    e.postDelayed(() -> {
                        viewModel.clear(() -> {
                            viewModel.getCacheSize(s -> {
                                Snackbar.make(e, R.string.text_clear_success, Snackbar.LENGTH_SHORT).show();
                                holder.setTextView(R.id.text, s);
                            });
                            dialog.dismiss();
                        });
                    }, 1500);
                });
            }
            if (getItem(position).equals(getString(R.string.text_settings_private))) {
                setTitleRight(holder, true);

                holder.itemView.setOnClickListener(e -> {
                    startActivity(PrivateFragment.class);
                });
            }
            if (getItem(position).equals(getString(R.string.text_settings_protocol))) {
                setTitleRight(holder, true);

                holder.itemView.setOnClickListener(e -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("file:///android_asset/agreement.html"));
                    intent.setPackage(getActivity().getPackageName());
                    intent.setClass(getActivity(), WebViewActivity.class);
                    startActivity(intent);
                });
            }
            if (getItem(position).equals(getString(R.string.text_settings_version))) {
                setTitleRight(holder, false);
                holder.setTextView(R.id.text, getVersion());
                holder.itemView.setOnClickListener(e -> {

                });
            }
            if (getItem(position).equals(getString(R.string.text_settings_exit))) {
                bindUi(RxUtil.click(holder.findViewById(R.id.title_right)), o -> {
                    UserModel.getInstance().loginOut();

                    EventBus.getDefault().post(new CartAddEvent(-1,0));
                    EventBus.getDefault().post(new UserEvent(0));
                    EventBus.getDefault().post(new LoginEvent());
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(IntentBuilder.KEY_BOOLEAN_LOGIN_OUT, true);
                    ActivityCompat.startActivity(getActivity(), intent
                            ,
                            null
                    );
                    ActivityCompat.finishAffinity(getActivity());
                    NotificationManager managerCompat = (NotificationManager)
                            getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    managerCompat.cancelAll();
                });
            }


        }

        public void setTitleRight(BaseViewHolder holder, boolean isSingle) {
            TextView titleRight = holder.findViewById(R.id.text);
            if (isSingle) {
                titleRight.setText("");
                Drawable drawable = DrawableHelper.getDrawableWithBounds(titleRight.getContext(), R.drawable.ic_arrow_right_gray);
                titleRight.setCompoundDrawables(null, null, drawable, null);
            } else titleRight.setCompoundDrawables(null, null, null, null);
        }

        public String getVersion() {
            try {
                PackageManager manager = getActivity().getPackageManager();
                PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                String version = info.versionName;
                return version;
            } catch (Exception e) {
                return "";
            }
        }
    }
}
