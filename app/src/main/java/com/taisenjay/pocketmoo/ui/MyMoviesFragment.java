package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taisenjay.pocketmoo.BaseFragment;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.MoviesAdapter;
import com.taisenjay.pocketmoo.database.DBUtil;
import com.taisenjay.pocketmoo.model.Movie;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/3
 * Created by a handsome boy with love
 */

public class MyMoviesFragment extends BaseFragment {

    private PullLoadMoreRecyclerView rvMyMovies;
    private MoviesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        getMyMovies();
    }

    private void initView(View view) {
        rvMyMovies = (PullLoadMoreRecyclerView) view.findViewById(R.id.rv_hot_movies);
        rvMyMovies.setStaggeredGridLayout(2);
        rvMyMovies.setRefreshing(true);
        mAdapter = new MoviesAdapter(mActivity);
        mAdapter.setIndex(2);
        rvMyMovies.setAdapter(mAdapter);
        rvMyMovies.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getMyMovies();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getMyMovies();
            }
        });
    }

    private void getMyMovies(){
        List<Movie> movies = DBUtil.getMyMovies(getActivity(),page);
//        Log.d("我的电影",movies.size() + "");
        if (page == 1)
            mAdapter.clear();
        mAdapter.addAll(movies);
        rvMyMovies.setPullLoadMoreCompleted();
    }

}
