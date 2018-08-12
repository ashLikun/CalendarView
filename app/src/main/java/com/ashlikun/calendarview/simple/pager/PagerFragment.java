package com.ashlikun.calendarview.simple.pager;

import android.support.v7.widget.LinearLayoutManager;

import com.ashlikun.calendarview.simple.Article;
import com.ashlikun.calendarview.simple.ArticleAdapter;
import com.ashlikun.calendarview.simple.R;
import com.ashlikun.calendarview.simple.base.fragment.BaseFragment;
import com.ashlikun.calendarview.simple.group.GroupItemDecoration;
import com.ashlikun.calendarview.simple.group.GroupRecyclerView;

public class PagerFragment extends BaseFragment {

    private GroupRecyclerView mRecyclerView;


    public static PagerFragment newInstance() {
        return new PagerFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pager;
    }

    @Override
    protected void initView() {
        mRecyclerView = (GroupRecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(mContext));
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    public boolean isScrollTop() {
        return mRecyclerView != null && mRecyclerView.computeVerticalScrollOffset() == 0;
    }
}
