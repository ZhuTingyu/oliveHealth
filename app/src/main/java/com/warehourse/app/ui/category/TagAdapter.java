package com.warehourse.app.ui.category;


import com.biz.base.BaseActivity;
import com.biz.base.BaseArrayListAdapter;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.warehourse.app.R;
import com.warehourse.app.event.SearchFilterValuesEvent;
import com.warehourse.app.model.entity.ProductFilterItemEntity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * Created by johnzheng on 5/31/16.
 */
 class TagAdapter extends BaseArrayListAdapter<ProductFilterItemEntity> {
    private String keyCode;
    boolean isMore;
    private boolean isUePrefix;
    private String title;
    private TagMoreListener mTagMoreListener;

    public void setTagMoreListener(TagMoreListener mTagMoreListener) {
        this.mTagMoreListener = mTagMoreListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUePrefix(boolean uePrefix) {
        isUePrefix = uePrefix;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public boolean isMore() {
        return isMore;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public int getCount() {
        int size = getList().size();
        if (isMore) {
            if (size > 9) return 9;
            return size;

        } else {
            if (size > 6) return 6;
            return size;
        }
    }

    public TagAdapter(Context context) {
        super(context);
        mList = Lists.newArrayList();
    }

    private int getSelectItemCount() {
        int count = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isSelected) {
                count++;
            }
        }
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater(R.layout.tag_item, parent);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_tag);
        int size = getList().size();
        ProductFilterItemEntity searchFieldsValueInfo = getItem(position);
        if (isMore) {
            if (size > 9 && position == 9 - 1) {
                textView.setText(R.string.all);
                textView.setOnClickListener(v -> {
                    FilterKey2Fragment filterKey2Fragment = new FilterKey2Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(IntentBuilder.KEY_KEY, keyCode);
                    bundle.putParcelableArrayList(IntentBuilder.KEY_DATA, mList);
                    bundle.putBoolean(IntentBuilder.KEY_BOOLEAN, isUePrefix);
                    bundle.putString(IntentBuilder.KEY_TITLE, title);
                    filterKey2Fragment.setArguments(bundle);
                    ((BaseActivity) getContext()).getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out)
                            .add(R.id.drawer_right, filterKey2Fragment, FilterKey2Fragment.class.getName())
                            .commitAllowingStateLoss();
                });
                return convertView;
            }
        } else {
            if (size > 6 && position == 6 - 1) {
                textView.setText(R.string.see_move);
                textView.setOnClickListener(v -> {
                    setMore(true);
                    if(mTagMoreListener!=null)
                        mTagMoreListener.call(true);
                });
                return convertView;
            }
        }
        textView.setSelected(searchFieldsValueInfo.isSelected);
        textView.setText(searchFieldsValueInfo == null ? "" : searchFieldsValueInfo.label);
        textView.setOnClickListener(v -> {
            if (!searchFieldsValueInfo.isSelected && getSelectItemCount() >= 3) {
                Toast.makeText(getContext(), R.string.toast_three_more, Toast.LENGTH_SHORT).show();
                return;
            }
            textView.setSelected(!textView.isSelected());
            searchFieldsValueInfo.isSelected = textView.isSelected();
            textView.setTypeface(v.isSelected() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            EventBus.getDefault().post(new SearchFilterValuesEvent(keyCode, getList()));
        });
        return convertView;
    }

    public interface TagMoreListener {
        void call(boolean isMore);
    }
}
