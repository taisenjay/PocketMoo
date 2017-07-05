package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.taisenjay.pocketmoo.BaseActivity;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.MoviesAdapter;
import com.taisenjay.pocketmoo.database.DBUtil;
import com.taisenjay.pocketmoo.model.Movie;
import com.taisenjay.pocketmoo.model.StarSimple;
import com.taisenjay.pocketmoo.recyclerview.EndlessRecyclerOnScrollListener;
import com.taisenjay.pocketmoo.recyclerview.ExStaggeredGridLayoutManager;
import com.taisenjay.pocketmoo.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.taisenjay.pocketmoo.recyclerview.HeaderSpanSizeLookup;
import com.taisenjay.pocketmoo.recyclerview.LoadingFooter;
import com.taisenjay.pocketmoo.recyclerview.RecyclerViewStateUtils;
import com.taisenjay.pocketmoo.recyclerview.RecyclerViewUtils;
import com.taisenjay.pocketmoo.recyclerview.SimpleTextHeader;
import com.taisenjay.pocketmoo.retrofit.RetrofitCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class StarHomePageActivity extends BaseActivity {

    public static final String EXTRA_STAR_DATA = "star_simple";

    private int page = 1;

    private StarSimple mStarSimple;
    private CircleImageView ivStarAvatar;
    private FloatingActionButton fabAdd;
//    private FlexboxLayout fbStarInfos;
    private RecyclerView rvStarMovies;
    private MoviesAdapter mAdapter;
    private HeaderAndFooterRecyclerViewAdapter mHAFAdapter;

    private List<String> infoList = new ArrayList<>();

    private String starUrlCode,starQuickAvatar;

//    private int mCurrentCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_home_page);

        mStarSimple = (StarSimple) getIntent().getSerializableExtra(EXTRA_STAR_DATA);
        starQuickAvatar = getIntent().getStringExtra("avatar");
        starUrlCode = mStarSimple.homepage.substring(24,mStarSimple.homepage.length());

        initView();

        getStarMain(starUrlCode,page);
    }

    private void initView() {
        ivStarAvatar = (CircleImageView) findViewById(R.id.iv_star_avatar);
        Log.d("图片不显示",starQuickAvatar);
        Glide.with(this).load(starQuickAvatar)
                .into(ivStarAvatar);
//        ViewCompat.setTransitionName(ivStarAvatar, Constants.KEY_SHARE_ANIM_AVATAR);

        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
//        fbStarInfos = (FlexboxLayout) findViewById(R.id.fb_star_infos);
        rvStarMovies = (RecyclerView) findViewById(R.id.rv_star_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(mStarSimple.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mAdapter = new MoviesAdapter(mActivity);
        mAdapter.setIndex(1);
        mHAFAdapter = new HeaderAndFooterRecyclerViewAdapter(mAdapter);
        rvStarMovies.setAdapter(mHAFAdapter);
        ExStaggeredGridLayoutManager gridLayoutManager = new ExStaggeredGridLayoutManager
                (2,StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup(
                (HeaderAndFooterRecyclerViewAdapter) rvStarMovies.getAdapter(),
                gridLayoutManager.getSpanCount()));
        rvStarMovies.setLayoutManager(gridLayoutManager);

        RecyclerViewUtils.setFooterView(rvStarMovies,new LoadingFooter(this));
        rvStarMovies.addOnScrollListener(new EndlessRecyclerOnScrollListener(){
            @Override
            public void onLoadNextPage(View view) {
                super.onLoadNextPage(view);
                LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(rvStarMovies);
                if(state == LoadingFooter.State.Loading) {
                    Log.d("network", "the state is Loading, just wait..");
                    return;
                }
                if (rvStarMovies.getAdapter().getItemCount() % 30 == 0) {
                    RecyclerViewStateUtils.setFooterViewState(StarHomePageActivity.this,
                            rvStarMovies, 0, LoadingFooter.State.Loading, null);
                    getStarMain(starUrlCode, ++page);
                }

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DBUtil.ifNameInStars(StarHomePageActivity.this,mStarSimple.name))
                    toastShow(getString(R.string.added));
                else if(DBUtil.insertToMyStars(StarHomePageActivity.this,mStarSimple) > -1){
                    toastShow(getString(R.string.insert_star_success));
                }else toastShow(getString(R.string.insert_star_faile));
            }
        });
    }

    private void initData(List<Movie> movieList){

        SimpleTextHeader header = new SimpleTextHeader(this);
        header.putTexts(infoList,this);
        RecyclerViewUtils.setHeaderView(rvStarMovies,header);

        if (page == 1)
            mAdapter.clear();
        mAdapter.addAll(movieList);
//        if (movieList.size() < 30)
//            RecyclerViewStateUtils.setFooterViewState(StarHomePageActivity.this,
//                    rvStarMovies, 0, LoadingFooter.State.TheEnd, null);
//        mCurrentCounter = mAdapter.getDataSize();

    }

    private void getStarMain(String code,int page){
        Call<ResponseBody> call = apiStores.getStarMovies(code,page);
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));

                    Elements infoBox = document.getElementsByClass("photo-info");
                    Elements infoElements = infoBox.get(0).getElementsByTag("p");
                    for(int i=0;i<infoElements.size() - 1;i++){
                        infoList.add(infoElements.get(i).text());
                    }

                    List<Movie> starMovies = new ArrayList<Movie>();
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
                        Log.e("img",movie.coverUrl);
                        starMovies.add(movie);
                    }
                    initData(starMovies);

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
//                rvStarMovies.setPullLoadMoreCompleted();

            }
        });
    }

}
