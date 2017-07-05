package com.taisenjay.pocketmoo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.taisenjay.pocketmoo.ui.GoogMoviesFragment;
import com.taisenjay.pocketmoo.ui.HotMoviesFragment;
import com.taisenjay.pocketmoo.ui.MyMoviesFragment;
import com.taisenjay.pocketmoo.ui.MyStarsFragment;
import com.taisenjay.pocketmoo.ui.SearchTorrentActivity;
import com.taisenjay.pocketmoo.ui.StarsFragment;
import com.taisenjay.pocketmoo.utils.CommonUtil;
import com.taisenjay.pocketmoo.utils.Constants;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.main_search);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);
        setTitle(getString(R.string.hot_movies));

        initSearchView();

        switchFragment(HotMoviesFragment.newInstance(HotMoviesFragment.Type.HOT,""));
    }

    private void initSearchView(){
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
                switchFragment(HotMoviesFragment.newInstance(HotMoviesFragment.Type.SEARCH,query));
                setTitle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void switchFragment(Fragment newFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, newFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_main_torrent_search:
                Intent intent = new Intent(this,SearchTorrentActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), R.string.exit_text, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_all_videos) {
            //change all to high ratings
//            switchFragment(HotMoviesFragment.newInstance(HotMoviesFragment.Type.ALL));
            switchFragment(new GoogMoviesFragment());
        } if (id == R.id.nav_hot_videos) {
            switchFragment(HotMoviesFragment.newInstance(HotMoviesFragment.Type.HOT,""));
        } else if (id == R.id.nav_girls) {
            switchFragment(new StarsFragment());
        } else if (id == R.id.nav_my_movies) {
            switchFragment(new MyMoviesFragment());
        } else if (id == R.id.nav_my_girls) {
            switchFragment(new MyStarsFragment());
        }
//        else if (id == R.id.nav_feedback) {
//            sendFeedback();
//            return true;
//        }
        else if (id == R.id.nav_update){
            //update
            return true;
        }
        setTitle(item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendFeedback(){
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setType("plain/text");
        data.putExtra(Intent.EXTRA_EMAIL, new String[]{"HunterJay@laputa.com"});
        data.putExtra(Intent.EXTRA_SUBJECT, "PocketMoo" + this.getString(R.string.feedback));
        data.putExtra(Intent.EXTRA_TEXT, CommonUtil.getSystemInfo(this));
        startActivity(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((boolean)CommonUtil.getSP(Constants.KEY_ABOVE_EIGHTEEN,false))
            return;
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warn_title))
                .setIcon(R.mipmap.icon_warn)
                .setMessage(getString(R.string.warn_content))
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonUtil.setSP(Constants.KEY_ABOVE_EIGHTEEN,false);
                        toastShow(getString(R.string.no_eighteen_tip));
                        MainActivity.this.finish();
                    }
                })
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommonUtil.setSP(Constants.KEY_ABOVE_EIGHTEEN,true);
                        return;
                    }
                })
                .setCancelable(false)
                .show();


    }
}
