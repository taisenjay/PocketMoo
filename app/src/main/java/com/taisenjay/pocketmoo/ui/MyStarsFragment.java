package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taisenjay.pocketmoo.BaseFragment;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.StarsAdapter;
import com.taisenjay.pocketmoo.database.DBUtil;
import com.taisenjay.pocketmoo.model.StarSimple;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/3
 * Created by a handsome boy with love
 */

public class MyStarsFragment extends BaseFragment{

    private PullLoadMoreRecyclerView rvMyStars;
    private StarsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        getMyStars();
    }

    private void initView(View view) {
        rvMyStars = (PullLoadMoreRecyclerView) view.findViewById(R.id.rv_hot_movies);
        rvMyStars.setStaggeredGridLayout(2);
        rvMyStars.setRefreshing(true);
        mAdapter = new StarsAdapter(mActivity);
        rvMyStars.setAdapter(mAdapter);
        rvMyStars.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getMyStars();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getMyStars();
            }
        });
    }
    
    private void getMyStars(){
        List<StarSimple> stars = DBUtil.getMyStars(getActivity(),page);
        if (page == 1)
            mAdapter.clear();
        mAdapter.addAll(stars);
        rvMyStars.setPullLoadMoreCompleted();
    }

}
