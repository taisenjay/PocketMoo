package com.taisenjay.pocketmoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.taisenjay.pocketmoo.BaseActivity;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.ImageAdapter;
import com.taisenjay.pocketmoo.adapter.StarsAdapter;
import com.taisenjay.pocketmoo.database.DBUtil;
import com.taisenjay.pocketmoo.loadingdrawable.LoadingView;
import com.taisenjay.pocketmoo.model.Movie;
import com.taisenjay.pocketmoo.model.StarSimple;
import com.taisenjay.pocketmoo.model.SuperLink;
import com.taisenjay.pocketmoo.retrofit.RetrofitCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class MovieDetailActivity extends BaseActivity implements View.OnClickListener{

    public static final String EXTRA_MOVIE_URL = "extra_url";
    public static final String EXTRA_MOVIE_ID = "extra_id";

    private String mMovieUrlCode,mMovieID;

    private String bigCoverUrl;
    private StringBuilder movieBaseInfo = new StringBuilder();

    private ImageView ivBigCover;
    private TextView tvBaseInfo;

//    private SuperLink movieMaker,moviePublisher,movieSeries;
    List<SuperLink> links = new ArrayList<>();
    List<String> linkTexts = new ArrayList<>();
    List<String> linkURLs = new ArrayList<>();

    private TextView tvSuper1,tvSuper2,tvSuper3,tvSuper4;
    List<TextView> tvSupers = new ArrayList<>();

    private StarsAdapter starAdapter;
    private RecyclerView rvMovieStars;

    private RecyclerView rvShortCuts;
    private ImageAdapter imageAdapter;

    private LoadingView mLoadingView;

    private Movie mMoviebean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mMovieUrlCode = getIntent().getStringExtra(EXTRA_MOVIE_URL);
        mMovieID = getIntent().getStringExtra(EXTRA_MOVIE_ID);

        mMoviebean = (Movie) getIntent().getSerializableExtra("bean");

        initView();

        if (mMovieUrlCode != null)
            getMovieDetail(mMovieUrlCode);
        else
            getMovieUrlCodeBefore(mMovieID);


    }

    private void initView() {
        ivBigCover = (ImageView) findViewById(R.id.iv_big_cover);
        tvBaseInfo = (TextView) findViewById(R.id.tv_base_info);
        tvSuper1 = (TextView) findViewById(R.id.tv_info_1);
        tvSuper2 = (TextView) findViewById(R.id.tv_info_2);
        tvSuper3 = (TextView) findViewById(R.id.tv_info_3);
        tvSuper4 = (TextView) findViewById(R.id.tv_info_4);
        tvSupers.add(tvSuper1);
        tvSupers.add(tvSuper2);
        tvSupers.add(tvSuper3);
        tvSupers.add(tvSuper4);

        mLoadingView = (LoadingView) findViewById(R.id.loading_drawable);
        mLoadingView.setVisibility(View.VISIBLE);

        rvMovieStars = (RecyclerView) findViewById(R.id.rv_movie_stars);
        rvShortCuts = (RecyclerView) findViewById(R.id.rv_movie_shortcuts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

        setSupportActionBar(toolbar);
        setTitle(mMovieID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivBigCover.setOnClickListener(this);

        rvMovieStars.setLayoutManager(new GridLayoutManager(this,3));
        starAdapter = new StarsAdapter(mActivity);
        rvMovieStars.setAdapter(starAdapter);
        rvMovieStars.setNestedScrollingEnabled(false);

        rvShortCuts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        new LinearSnapHelper().attachToRecyclerView(rvShortCuts);
        imageAdapter = new ImageAdapter(mActivity);
        rvShortCuts.setAdapter(imageAdapter);
    }

    private void initData(List<StarSimple> stars,List<String> imgUrls) {
        Glide.with(this).load(bigCoverUrl).placeholder(R.mipmap.img_default).into(ivBigCover);
        tvBaseInfo.setText(movieBaseInfo.toString());

        for (int i = 0 ; i < links.size();i++){
            final SuperLink link = links.get(i);
            TextView tv = tvSupers.get(i);
            tv.setText(link.name);
            Log.e("linkURL",link.url);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MovieDetailActivity.this,LinkResultActivity.class);
                    intent.putExtra(LinkResultActivity.EXTRA_LINK,link.url);
                    intent.putExtra("name",link.name);
                    startActivity(intent);
                }
            });
        }
        if (tvSupers.size() > 0){
            for(TextView tvGone : tvSupers) {
                if (tvGone.getText().toString().equals(""))
                    tvGone.setVisibility(View.GONE);
            }
        }

//        Log.e("star",stars.size() + "只");
        starAdapter.addAll(stars);
        imageAdapter.addAll(imgUrls);
    }

    private void getMovieDetail(String movieUrlCode) {
        Call<ResponseBody> call = apiStores.getMovieDetail(movieUrlCode);
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));

                    //movie cover info
                    bigCoverUrl = document.getElementsByClass("bigImage").attr("href");

                    Elements infoBox = document.getElementsByClass("col-md-3 info");

                    //movie base info
                    Elements infoElements = infoBox.get(0).getElementsByTag("p");
                    for(int i = 0 ; i < infoElements.size() ; i++){
                        Element info = infoElements.get(i);
                        if (i == 0){
                            movieBaseInfo.append(info.getElementsByClass("header").text())
                                    .append(info.getElementsByAttributeValue("style","color:#CC0000;").text())
                                    .append("\t\t\t\t");

                        }else if (i <= 2){

                                movieBaseInfo.append(info.text())
                                        .append(i == 1 ? "\n" : "\t\t\t\t");
                        }else{
                            if (info.text().startsWith("导演")){
                                    SuperLink linkDirector = new SuperLink(info.text()
                                            , info.getElementsByClass("a").attr("href"));
                                    links.add(linkDirector);
                            }else{
                                linkTexts.add(info.text());
                                Log.e("link",info.html());
                                String url = info.getElementsByTag("a").attr("href");
                                if (url !=null && !url.equals("")) {
                                    linkURLs.add(url);
//                                    Log.e("href",info.getElementsByTag("a").attr("href") + "bug");
                                }

                            }

                        }
                    }
                    Log.e("href", linkURLs.size() +"");

                    for (int i = 0 ; i < 6 ; i = i + 2){
                        SuperLink link = new SuperLink(linkTexts.get(i) + linkTexts.get(i+1),
                                linkURLs.get(i/2));
                        Log.e("linkR",link.toString());
                        links.add(link);
                    }

                    //movie stars info
                    Elements starElements = document.getElementsByClass("avatar-box");
                    List<StarSimple> starSimples = new ArrayList<StarSimple>();
                    for(Element element:starElements){
                        StarSimple star = new StarSimple();
                        star.homepage = element.attr("href");
                        star.name = element.getElementsByTag("span").text();
                        star.avatar = element.getElementsByTag("img").attr("src");
                        starSimples.add(star);
                    }

                    //movie shortcuts
                    Elements shortcutElements = document.getElementsByClass("sample-box");
                    List<String> shortcutsUrls = new ArrayList<String>();
                    for(Element element:shortcutElements){
                        String shortcut = element.attr("href");
                        shortcutsUrls.add(shortcut);
                    }


                    initData(starSimples,shortcutsUrls);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                toastShow(msg);
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onThrowable(Throwable t) {
                toastShow(t.getMessage());
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                mLoadingView.setVisibility(View.GONE);
            }
        });
    }

    private void getMovieUrlCodeBefore(String movieID){
        Call<ResponseBody> call = apiStores.getMoviesBySearch(movieID,1);
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));
                    Elements movieElements = document.getElementsByClass("movie-box");
                    String movieDetailUrl = movieElements.get(0).attr("href");
                    mMovieUrlCode = movieDetailUrl.substring(25,movieDetailUrl.length());
                    getMovieDetail(mMovieUrlCode);

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

            }
        });
        addCalls(call);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_big_cover:
                Intent intent = new Intent(this, BigImageActivity.class);
                intent.putExtra(BigImageActivity.EXTRA_IMAGE_URL,bigCoverUrl);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search_torrent:
                Intent intent = new Intent(this,SearchTorrentActivity.class);
                intent.putExtra(SearchTorrentActivity.EXTRA_FILE_NAME,mMovieID);
                startActivity(intent);
                break;
            case R.id.menu_add_to_mine:

                if(DBUtil.ifCodeInMovies(this,mMoviebean.identityCode)){
                    toastShow(getString(R.string.added));
                }else if(DBUtil.insertToMyMovies(this,mMoviebean) > -1){
                    toastShow(getString(R.string.insert_success));
                }else toastShow(getString(R.string.insert_faile));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
