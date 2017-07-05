package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taisenjay.pocketmoo.BaseFragment;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.StarsAdapter;
import com.taisenjay.pocketmoo.model.StarSimple;
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

public class StarsFragment extends BaseFragment {

    private PullLoadMoreRecyclerView rvStars;
    private StarsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stars, container, false);
        
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        getStars();
    }


    private void initView(View view) {
        rvStars = (PullLoadMoreRecyclerView) view.findViewById(R.id.rv_stars);
        rvStars.setStaggeredGridLayout(2);
        rvStars.setRefreshing(true);
        mAdapter = new StarsAdapter(mActivity);
        rvStars.setAdapter(mAdapter);
        rvStars.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getStars();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getStars();
            }
        });
    }

    private void getStars(){
        Call<ResponseBody> call;
        if (page == 1) {
            call = apiStores.getStars();
        } else {
            call = apiStores.getStars(page);
        }
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));

                    List<StarSimple> datas = new ArrayList<StarSimple>();
                    Elements starElements = document.getElementsByClass("avatar-box text-center");
                    for (Element starElement:starElements){
                        StarSimple star = new StarSimple();
                        star.homepage = starElement.attr("href");
                        star.avatar = starElement.getElementsByTag("img").attr("src");
                        star.name = starElement.getElementsByTag("span").text();
                        datas.add(star);
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
                rvStars.setPullLoadMoreCompleted();
            }
        });
        addCalls(call);
    }

}
