package com.warehourse.app.ui.category;


/**
 * Created by johnzheng on 5/30/16.
 */


import com.google.gson.reflect.TypeToken;

import com.biz.base.BaseFragment;
import com.biz.base.BaseViewHolder;
import com.biz.util.DrawableHelper;
import com.biz.util.GsonUtil;
import com.biz.util.IntentBuilder;
import com.biz.util.Maps;
import com.biz.util.RxUtil;
import com.biz.util.Utils;
import com.warehourse.app.R;
import com.warehourse.app.event.SearchFilterEvent;
import com.warehourse.app.event.SearchFilterValuesEvent;
import com.warehourse.app.model.entity.ProductFilterEntity;
import com.warehourse.app.model.entity.ProductFilterItemEntity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class FilterKeyFragment extends BaseFragment  {

    LinearLayout layout;
    TextView btn1, btn2;
    private ArrayList<ProductFilterEntity> fields;
    private String keyCode;
    private String sort;
    private Map<String, ViewHolder> map;

    private ArrayList<ProductFilterEntity> copyFields(ArrayList<ProductFilterEntity> list) {
        String json = GsonUtil.toJson(list);
        ArrayList<ProductFilterEntity> listOut = GsonUtil.fromJson(json, new TypeToken<ArrayList<ProductFilterEntity>>() {
        }.getType());
        return listOut;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        map = Maps.newHashMap();
        Bundle bundle = getArguments();
        if (bundle != null) {
            keyCode = bundle.getString(IntentBuilder.KEY_KEY);
            sort = bundle.getString(IntentBuilder.KEY_TYPE);
            fields = bundle.getParcelableArrayList(IntentBuilder.KEY_DATA);
        }
    }

    public void onEventMainThread(SearchFilterValuesEvent event) {
        if (event != null && !TextUtils.isEmpty(event.keyCode) && event.keyCode.contains(keyCode)) {
            if (!map.containsKey(event.keyCode)) return;
            ViewHolder viewHolder = map.get(event.keyCode);
            if (viewHolder != null) {
                viewHolder.textChange.setText(ProductFilterItemEntity.getLabels(event.fields));
                setTextChangeColor(viewHolder.textChange);
                if (event.isAll) {
                    //排序 处理方式
                    List<ProductFilterItemEntity> list = viewHolder.tagAdapter.getList();
                    for (ProductFilterItemEntity valueInfo1 : list) {
                        if (TextUtils.isEmpty(valueInfo1.value)) continue;
                        for (ProductFilterItemEntity valueInfo2 : event.fields) {
                            if (valueInfo1.value.equals(valueInfo2.value)) {
                                valueInfo1.isSelected = valueInfo2.isSelected;
                            }
                        }
                    }
                    viewHolder.tagAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void setTextChangeColor(TextView textChange) {
        if (!textChange.getContext().getString(R.string.all).equals(textChange.getText().toString())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textChange.setTextColor(
                        textChange.getContext().getResources().getColor(R.color.base_color
                                , textChange.getContext().getTheme()));
            } else {
                textChange.setTextColor(
                        textChange.getContext().getResources().getColor(R.color.base_color
                        ));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textChange.setTextColor(
                        textChange.getContext().getResources().getColor(R.color.color_515151
                                , textChange.getContext().getTheme()));
            } else {
                textChange.setTextColor(
                        textChange.getContext().getResources().getColor(R.color.color_515151
                        ));
            }
        }
    }

    public void setFields(ArrayList<ProductFilterEntity> fields) {
        this.fields = fields;
        initDataView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_1_layout, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            view.setPadding(0, Utils.dip2px(24), 0, 0);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout =  findViewById(R.id.layout);
        btn1 =  findViewById(R.id.btn_1);
        btn2 =  findViewById(R.id.btn_2);
        bindUi(RxUtil.click(btn1), o -> {
            ProductFilterEntity.clearSelectedAll(fields);
            if (fields != null && fields.size() > 0)
                for (ProductFilterEntity entity : fields) {
                    ViewHolder viewHolder = map.get(getKey(entity.label));
                    viewHolder.textChange.setText(ProductFilterItemEntity.getLabels(entity.filterItems));
                    setTextChangeColor(viewHolder.textChange);
                    viewHolder.tagAdapter.setList(entity.filterItems);
                }


        });
        bindUi(RxUtil.click(btn2), o -> {
            EventBus.getDefault().post(new SearchFilterEvent(keyCode, sort, fields));
        });

        initDataView();
    }

    private void initDataView(){
        if (getView()!=null) {
            layout.removeAllViews();
            getView().post(() -> {
                fields = copyFields(fields);
                if (fields != null && fields.size() > 0) {
                    for (ProductFilterEntity searchFieldsInfo : fields)
                        createTagView(searchFieldsInfo);
                }
            });
        }
    }

    public String getKey(String label) {
        return keyCode+label;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void createTagView(ProductFilterEntity entity) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tag_filter, layout, false);
        ViewHolder holder = new ViewHolder(view);
        holder.textChange.setText(ProductFilterItemEntity.getLabels(entity.filterItems));
        setTextChangeColor(holder.textChange);
        holder.textKeyword.setText(entity.label == null ? "" : entity.label);
        holder.tagAdapter.setTitle(entity.label);
        holder.tagAdapter.setList(entity.filterItems);
        if (entity.filterItems == null || entity.filterItems.size() <= 6) {
            holder.textChange.setEnabled(false);
            holder.textChange.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        String key = keyCode + entity.label;
        holder.tagAdapter.setKeyCode(key);
        holder.tagAdapter.setUePrefix(entity.usePrefix);
        map.put(key, holder);
        layout.addView(view);
    }




    class ViewHolder extends BaseViewHolder{
        TextView textKeyword;
        TextView textChange;
        GridView tagView;
        Drawable drawable, selectedDrawable;
        public TagAdapter tagAdapter;

        ViewHolder(View view) {
            super(view);
            textKeyword = findViewById(R.id.text_keyword);
            textChange =  findViewById(R.id.text_change);
            tagView =  findViewById(R.id.tagView_key);
            tagAdapter = new TagAdapter(view.getContext());
            tagAdapter.setTagMoreListener((b) -> {
                textChange.setSelected(b);
                textChange.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        textChange.isSelected() ? selectedDrawable : drawable, null);
            });
            drawable = DrawableHelper.getDrawableWithBounds(view.getContext(), R.drawable.ic_arrow_down);
            textChange.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            selectedDrawable = DrawableHelper.getDrawableWithBounds(view.getContext(), R.drawable.ic_arrow_up);
            textChange.setOnClickListener(v -> {
                v.setSelected(!tagAdapter.isMore());
                tagAdapter.setMore(!tagAdapter.isMore());
                textChange.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        v.isSelected() ? selectedDrawable : drawable, null);
            });
            tagView.setAdapter(tagAdapter);
        }
    }
}

