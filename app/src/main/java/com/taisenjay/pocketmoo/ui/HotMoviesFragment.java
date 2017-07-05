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

public class HotMoviesFragment extends BaseFragment{

//    private int page = 1;
    private PullLoadMoreRecyclerView rvHotMovies;
    private MoviesAdapter mAdapter;

    private static final String TAG_TYPE = "type";

    private int viewType;
    private String query="";

    public static class Type{
        public static int ALL = 0;
        public static int HOT = 1;
        public static int SEARCH = 2;
    }

    public static HotMoviesFragment newInstance(int type,String query){
        HotMoviesFragment fragment = new HotMoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_TYPE,type);
        if (type == Type.SEARCH)
            bundle.putString("query",query);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewType = getArguments().getInt(TAG_TYPE);
        query = getArguments().getString("query");
        return inflater.inflate(R.layout.fragment_hot_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        getHotMovies();
    }



    private void initView(View view) {
        rvHotMovies = (PullLoadMoreRecyclerView) view.findViewById(R.id.rv_hot_movies);
        rvHotMovies.setStaggeredGridLayout(2);
        rvHotMovies.setRefreshing(true);
        mAdapter = new MoviesAdapter(mActivity);
        rvHotMovies.setAdapter(mAdapter);
        rvHotMovies.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getHotMovies();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getHotMovies();
            }
        });
    }

    private void getHotMovies(){
        Call<ResponseBody> call;

        if (viewType == Type.ALL)
            call = page == 1 ? apiStores.getAllMovies() : apiStores.getAllMovies(page);
        else if(viewType == Type.HOT)
            call = page == 1 ? apiStores.getHotMovies() : apiStores.getHotMovies(page);
        else
            call = apiStores.getMoviesBySearch(query,page);

//        if (page == 1) {
//            call = apiStores.getHotMovies();
//        } else {
//            call = apiStores.getHotMovies(page);
//        }
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));

                    List<Movie> datas = new ArrayList<Movie>();
                    Elements movieElements = document.getElementsByClass("movie-box");

                    for (Element element : movieElements ){
                        Movie movie = new Movie();
                        movie.detailUrl = element.attr("href");
                        movie.coverUrl = element.getElementsByTag("img").get(0).attr("src");

                        Elements dates = element.getElementsByTag("date");
                        if (dates != null && dates.size() >= 1) {
                            movie.identityCode = dates.get(0).text();
                            //can't get second "date" becasue of the "/"
//                            if (datas.size() == 2) {
//                                movie.publishTime = dates.get(1).text();
//                            }
                        }
                        String dateStr = element.getElementsByTag("span").toString();
                        movie.publishTime = dateStr.substring(dateStr.length() - 26,dateStr.length() - 15);

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
                rvHotMovies.setPullLoadMoreCompleted();
            }
        });
        addCalls(call);
    }



}
