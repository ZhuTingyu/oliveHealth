package com.warehourse.app.ui.category;

/**
 * Created by johnzheng on 5/30/16.
 */


import com.google.gson.reflect.TypeToken;

import com.biz.base.BaseFragment;
import com.biz.base.FragmentBackHelper;
import com.biz.util.GsonUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.util.SortListUtil;
import com.biz.util.Utils;
import com.biz.widget.WaveSideBar;
import com.warehourse.app.R;
import com.warehourse.app.event.SearchFilterValuesEvent;
import com.warehourse.app.model.entity.ProductFilterItemEntity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import rx.Observable;


public class FilterKey2Fragment extends BaseFragment implements FragmentBackHelper {

    private RecyclerView mRecyclerView;
    private WaveSideBar mWaveSideBar;

    private FilterKey2Adapter mAdapter;
    private String keyCode;
    private ArrayList<ProductFilterItemEntity> fields;
    private boolean isUsePrefix = false;
    private String title;
    private HashMap<String, Integer> mLetters = new HashMap<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            keyCode = bundle.getString(IntentBuilder.KEY_KEY);
            fields = bundle.getParcelableArrayList(IntentBuilder.KEY_DATA);
            isUsePrefix = bundle.getBoolean(IntentBuilder.KEY_BOOLEAN, false);
            title = bundle.getString(IntentBuilder.KEY_TITLE);
            if (fields == null) fields = Lists.newArrayList();
            fields=copyFields(fields);
            sortList();
        }
    }

    private void sortList() {
        SortListUtil<ProductFilterItemEntity> sort = new SortListUtil<ProductFilterItemEntity>();
        sort.Sort(fields, "getPrefix", "asc");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_filter_2_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = getView(R.id.list);
        mWaveSideBar = getView(R.id.side_bar);

        view.setPadding(0, Utils.getStatusBarHeight(getActivity()), 0, 0);

        mToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        mToolbar.getMenu().add(0, 2, 0, R.string.btn_confirm).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 2) {
                Observable.from(mAdapter.getData()).map(e->{return e.t;}).toList()
                        .subscribe(list->{
                            EventBus.getDefault().post(
                                    new SearchFilterValuesEvent(keyCode, new ArrayList<>(list), true));

                        });
                onBackPressed();
            }
            return false;
        });
        setTitle(title == null ? "" : title);

        mAdapter = new FilterKey2Adapter();
        if (isUsePrefix) {
            Observable.range(0, fields.size()).forEach(i->{
                mLetters.put(fields.get(i).getPrefix(), i);
            });
            mWaveSideBar.setOnSelectIndexItemListener((String index)-> {
                if (mLetters.containsKey(index.toUpperCase())) {
                    mRecyclerView.scrollToPosition(mLetters.get(index));
                }
            });
        } else {
            mWaveSideBar.setVisibility(View.GONE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.post(() -> {

            Observable.from(fields).map(e->{
                return  new ProductFilterItemSection(e);
            }).toList().subscribe(mAdapter::setNewData);
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .colorResId(R.color.color_eeeeee).showLastDivider().build());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public boolean onBackPressed() {
        if (this.isAdded()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .remove(this)
                    .commitAllowingStateLoss();
            return true;
        }
        return false;
    }
    private ArrayList<ProductFilterItemEntity> copyFields(ArrayList<ProductFilterItemEntity> list) {
        String json = GsonUtil.toJson(list);
        ArrayList<ProductFilterItemEntity> listOut =
                GsonUtil.fromJson(json, new TypeToken<ArrayList<ProductFilterItemEntity>>() {
        }.getType());
        return listOut;
    }
}
