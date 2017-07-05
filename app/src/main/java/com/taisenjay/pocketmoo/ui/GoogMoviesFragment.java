package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taisenjay.pocketmoo.BaseFragment;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.MoviesAdapter;
import com.taisenjay.pocketmoo.model.Movie;
import com.taisenjay.pocketmoo.retrofit.RetrofitCallback;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Author : WangJian
 * Date   : 2017/7/1
 * Created by a handsome boy with love
 */

public class GoogMoviesFragment extends BaseFragment {

    private PullLoadMoreRecyclerView rvGoodMovies;
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

        getGoodMovies();
    }

    private void initView(View view) {
        rvGoodMovies = (PullLoadMoreRecyclerView) view.findViewById(R.id.rv_hot_movies);
        rvGoodMovies.setStaggeredGridLayout(2);
        rvGoodMovies.setRefreshing(true);
        mAdapter = new MoviesAdapter(mActivity);
        rvGoodMovies.setAdapter(mAdapter);
        rvGoodMovies.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getGoodMovies();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getGoodMovies();
            }
        });
    }
    
    private void getGoodMovies(){
        Call<ResponseBody> call = apiStores.getGoodMovies(page);
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));
                    Elements videoElements = document.getElementsByClass("video");

                    List<Movie> datas = new ArrayList<Movie>();
                    for (Element video : videoElements){
                        Movie movie = new Movie();
                        Element box = video.getElementsByTag("a").get(0);
                        movie.identityCode = box.attr("title").toString().split(" ")[0];
                        movie.coverUrl = box.getElementsByTag("img").attr("src");

                        datas.add(movie);
                    }
                    if (page == 1)
                        mAdapter.clear();
                    mAdapter.addAll(datas);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                toastShow(msg);
            }

            @Override
            public void onThrowable(Throwable t) {
                toastShow(t.getMessage());
            }

            @Override
            public void onFinish() {
                rvGoodMovies.setPullLoadMoreCompleted();
            }
        });
        addCalls(call);
    }
    
    
}
