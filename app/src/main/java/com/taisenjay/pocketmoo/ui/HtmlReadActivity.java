package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.taisenjay.pocketmoo.BaseActivity;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.loadingdrawable.LoadingView;
import com.taisenjay.pocketmoo.model.NewsSimple;
import com.taisenjay.pocketmoo.retrofit.RetrofitCallback;
import com.taisenjay.pocketmoo.utils.CommonUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class HtmlReadActivity extends BaseActivity {
    public static final String EXTRA_NEWS = "news";

    private NewsSimple mNews;

    private WebView wvContent;

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_read);

        mNews = (NewsSimple) getIntent().getSerializableExtra(EXTRA_NEWS);

        initView();

        getFullHtml();
    }

    private void initView() {
        wvContent = (WebView) findViewById(R.id.wv_content);
        CommonUtil.initWebView(wvContent);

        mLoadingView = (LoadingView) findViewById(R.id.loading_drawable);
        mLoadingView.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(mNews.title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        NativeExpressAdView adView = (NativeExpressAdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }

    private void getFullHtml(){
        Call<ResponseBody> call = apiStores.getLinkMovies(mNews.detailUrl);
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));

                    Element content = document.getElementsByClass("single-text").get(0);

                    String html = content.html();

                    Log.e("html",html);

                    CommonUtil.webViewLoadData(wvContent,"http://www.zhainanfulishe.net",
                            CommonUtil.makeHtmlImgFitScreen(html));

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
        addCalls(call);
    }


}
