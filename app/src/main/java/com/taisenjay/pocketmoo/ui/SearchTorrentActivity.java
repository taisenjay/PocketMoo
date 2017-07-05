package com.taisenjay.pocketmoo.ui;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.taisenjay.pocketmoo.BaseActivity;
import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.adapter.TorrentAdapter;
import com.taisenjay.pocketmoo.loadingdrawable.LoadingView;
import com.taisenjay.pocketmoo.model.Torrent;
import com.taisenjay.pocketmoo.retrofit.RetrofitCallback;
import com.taisenjay.pocketmoo.utils.DensityUtil;
import com.taisenjay.pocketmoo.utils.SpaceItemDecoration;
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

public class SearchTorrentActivity extends BaseActivity {

    public static final String EXTRA_FILE_NAME = "file_name";
    private String fileName;

    private Toolbar toolbar;
    private PullLoadMoreRecyclerView rvTorrents;
    private SearchView searchView;
    private TorrentAdapter mAdapter;

    private List<Torrent> mDatas = new ArrayList<>();

    private int page = 1;
//    private ImageView ivEmpty;

    private LoadingView mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_torrent);

        fileName = getIntent().getStringExtra(EXTRA_FILE_NAME);
        initView();

        if (fileName != null)
            getMovieTorrents(fileName);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rvTorrents = (PullLoadMoreRecyclerView) findViewById(R.id.rv_torrents);
//        ivEmpty = (ImageView) findViewById(R.id.iv_empty);
        searchView = (SearchView) findViewById(R.id.torrent_search);

        mLoadingView = (LoadingView) findViewById(R.id.loading_drawable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(fileName == null ? getString(R.string.tsearcher):fileName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initSearchView();

        rvTorrents.setRefreshing(true);
        mAdapter = new TorrentAdapter(this,mDatas);
        rvTorrents.setAdapter(mAdapter);
        rvTorrents.setLinearLayout();
        rvTorrents.setPullRefreshEnable(false);
        rvTorrents.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(this,5)));
        rvTorrents.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getMovieTorrents(fileName);
            }

            @Override
            public void onLoadMore() {
                page++;
                getMovieTorrents(fileName);
            }
        });

    }

    private void initSearchView(){
        if (fileName != null){
            searchView.setVisibility(View.GONE);
            return;
        }
        searchView.setIconifiedByDefault(true);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
//        searchView.onActionViewExpanded();写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
//        searchView.requestFocus();输入焦点
        searchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        searchView.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
//        searchView.setIconified(false);输入框内icon不显示
        searchView.requestFocusFromTouch();//模拟焦点点击事件

        searchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
        searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要
        // 事件监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                fileName = query;
                getMovieTorrents(fileName);
                setTitle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getMovieTorrents(String query) {

        Call<ResponseBody> call;
        if (page == 1) {
            call = apiStores.getMovieTorrents(query);
            mLoadingView.setVisibility(View.VISIBLE);
        }
        else
            call = apiStores.getMovieTorrents(query, page);
        call.enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    Document document = Jsoup.parse(new String(model.bytes(), "UTF-8"));
                    document.charset();

                    Log.e("btsow",document.html());

                    List<Torrent> torrents = new ArrayList<>();

                    //datasource:so
//                    Elements table = document.getElementsByClass("data-list");
//                    if (table == null || table.size() == 0)
//                        return;
//                    Elements rows  = table.get(0).getElementsByClass("row");
//                    for (Element row : rows) {
//                        Torrent torrent = new Torrent();
//                        torrent.address = row.getElementsByTag("a").attr("href");
//                        torrent.name = row.getElementsByTag("a").attr("title");
//                        torrent.date = row.getElementsByClass("col-sm-2 col-lg-2 hidden-xs text-right date").text();
//                        torrent.size = row.getElementsByClass("col-sm-2 col-lg-1 hidden-xs text-right size").text();
//                        torrents.add(torrent);
//                    }

//                    datasource:kitty
                    Elements table = document.select("#archiveResult");
                    Elements trs = table.get(0).getElementsByTag("tr");
                    if (trs.size() > 1)
                        trs.remove(0);
                    for (Element tr:trs){
                        Torrent torrent = new Torrent();
                        torrent.name = tr.getElementsByClass("name").text();
                        torrent.size = tr.getElementsByClass("size").text();
                        torrent.date = tr.getElementsByClass("date").text();
                        Elements links = tr.getElementsByClass("action").get(0).getElementsByTag("a");
                        torrent.address = links.get(1).attr("href");
                        torrents.add(torrent);
                    }
                    if (torrents.size() == 1 && torrents.get(0).name.startsWith("No result"))
                        torrents.clear();

                    if (page == 1)
                        mDatas.clear();
                    mDatas.addAll(torrents);
                    mAdapter.notifyDataSetChanged();



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                toastShow(code + " ; " + msg);
//                toastShow(getString(R.string.visit_too_frequent));
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onThrowable(Throwable t) {
                toastShow(t.getCause() + " ; " +t.getMessage());
//                toastShow(getString(R.string.visit_too_frequent));
                mLoadingView.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                rvTorrents.setPullLoadMoreCompleted();
                mLoadingView.setVisibility(View.GONE);
            }
        });

        addCalls(call);

    }

}
