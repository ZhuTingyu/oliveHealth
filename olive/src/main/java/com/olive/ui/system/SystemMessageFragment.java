package com.olive.ui.system;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biz.base.BaseFragment;
import com.biz.util.IntentBuilder;
import com.biz.util.Lists;
import com.biz.widget.recyclerview.XRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.olive.R;
import com.olive.ui.BaseErrorFragment;
import com.olive.ui.adapter.SystemMessageAdapter;

import java.util.List;

/**
 * Created by TingYu Zhu on 2017/9/7.
 */

public class SystemMessageFragment extends BaseErrorFragment {

    XRecyclerView recyclerView;
    SystemMessageAdapter adapter;
    List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_recyclerview, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.title_system_message);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SystemMessageAdapter();
        adapter.setNewData(list = Lists.newArrayList(getContext().getResources().getStringArray(R.array.array_system_message)));
        adapter.setOnItemClickListener(this::onItemClick);
        recyclerView.setAdapter(adapter);
    }

    private void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        String item = list.get(i);
        if(equalsString(item, R.string.text_service_agreement)){

        }else if(equalsString(item, R.string.text_version_message)){

        }else if(equalsString(item, R.string.text_about_us)){
            IntentBuilder.Builder().startParentActivity(getActivity(), AboutUsFragment.class);
        }
    }

    private boolean equalsString(String s, @StringRes int resId){
        return s.equalsIgnoreCase(getString(resId));
    }
}
