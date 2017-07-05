package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.taisenjay.pocketmoo.BaseActivity;
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

public class LinkResultActivity extends BaseActivity {
    public static final String EXTRA_LINK = "extra_link";

    private PullLoadMoreRecyclerView rvLinkMovies;
    private MoviesAdapter mAdapter;
    
    private String linkUrl,linkName;
    
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_result);

        linkUrl = getIntent().getStringExtra(EXTRA_LINK);
        linkName = getIntent().getStringExtra("name");

        initView();

        getLinkMovies();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(linkName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvLinkMovies = (PullLoadMoreRecyclerView) findViewById(R.id.rv_link_movies);
        rvLinkMovies.setStaggeredGridLayout(2);
        rvLinkMovies.setRefreshing(true);
        mAdapter = new MoviesAdapter(mActivity);
        rvLinkMovies.setAdapter(mAdapter);
        rvLinkMovies.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getLinkMovies();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getLinkMovies();
            }
        });
    }
    
    private void getLinkMovies(){
        if (linkUrl == null){
            rvLinkMovies.setPullLoadMoreCompleted();
            return;
        }
        Call<ResponseBody> call = apiStores.getLinkMovies(linkUrl);

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
                rvLinkMovies.setPullLoadMoreCompleted();
            }
        });
        addCalls(call);
    }

}
