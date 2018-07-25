package com.example.littlekids8.wanandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.littlekids8.wanandroid.gson.Banner;
import com.example.littlekids8.wanandroid.gson.BannerData;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import com.example.littlekids8.wanandroid.gson.Article;
import com.example.littlekids8.wanandroid.gson.ArticleListData;
import com.example.littlekids8.wanandroid.gson.DataBeans;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import com.example.littlekids8.wanandroid.util.HttpUtil;


public class MainPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    private SwipeRefreshLayout swipeRefreshLayout;
    private int articlePage;
    private String mainPageUrl;
    private RecyclerView mainPageRecyclerView ;
    private List<Article> articlesList;
    private boolean loading = false;
    private ArticlesAdapter articlesAdapter;
    private List<Banner> banners;
    private final String bannerUrl = "http://www.wanandroid.com/banner/json";
    private DrawerLayout mDrawerlayout;
    static public boolean isfront;
    private Thread bannerPlayThread;
    Toolbar toolbar;

    @Override
    protected void onResume() {
        super.onResume();
        isfront = true;
       if(articlesAdapter!=null){
           bannerPlayThread = articlesAdapter.getViewPagerThread();
           bannerPlayThread.start();
       }


    }

    @Override
    protected void onPause() {
        super.onPause();
        isfront = false;
        bannerPlayThread.interrupt();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_page);
        LitePal.initialize(super.getBaseContext());
        toolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.drawer_layout)==null)
            Toast.makeText(this,"mlayout",Toast.LENGTH_SHORT).show();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        LitePal.getDatabase();

        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,mDrawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        articlePage =0;

        mainPageRecyclerView = (RecyclerView)findViewById(R.id.main_page_view);
        mainPageRecyclerView.setLayoutManager(linearLayoutManager);
        mainPageRecyclerView.setAdapter(articlesAdapter);

        requestData(articlePage,true);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articlePage=0;
                requestData(articlePage,true);
                Toast.makeText(MainPageActivity.this,"刷新完成",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        mainPageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0){

                    //第一个可见item的位置 + 当前可见的item个数 >= item的总个数即为底部
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();


                    if(!loading&&visibleItemCount+pastVisibleItems+2>=totalItemCount){
                        loading=true;
                        articlePage++;
                        requestData(articlePage,false);
                    }
                }
            }
        });
        }


    public static DataBeans handleDataResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            String dataContent = jsonObject.toString();
            Log.d("Hello1",dataContent);
            return new Gson().fromJson(dataContent, DataBeans.class);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static BannerData handleBannerDataResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            String dataContent = jsonObject.toString();
            Log.d("Hello1",dataContent);
            return new Gson().fromJson(dataContent, BannerData.class);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void requestData(final int articlePage,boolean freshBanner){

       // final String mainPageUrl ="http://www.wanandroid.com/article/list/0/json";
        mainPageUrl ="http://www.wanandroid.com/article/list/"+articlePage+"/json";
        if(freshBanner){
            /**
             * 获取banner的数据
             */
            HttpUtil.sendOkHttpRequest(bannerUrl, new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseBannerText = response.body().string();
                    Log.d("banner",responseBannerText);
                    final BannerData bannerData = handleBannerDataResponse(responseBannerText);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bannerData !=null){
                                SharedPreferences.Editor editor = PreferenceManager.
                                        getDefaultSharedPreferences(MainPageActivity.this).
                                        edit();
                                editor.putString("bannerData",responseBannerText);
                                Log.d("banner","banenr线程执行");
                                editor.apply();


                                List<Banner> mBannerList = bannerData.getBannerList();

                                if(mBannerList==null){
                                    Toast.makeText(MainPageActivity.this,bannerData.getErrorCode(),Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(MainPageActivity.this,"banner数据为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    final String message = e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(MainPageActivity.this,"bannerError:"+message,Toast.LENGTH_LONG).show();
                        }
                    });

                }


            });

        }

        /**
         * 获取首页文章列表
         */
        HttpUtil.sendOkHttpRequest(mainPageUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();

                Log.d("Hello2",responseText);
                final DataBeans databeans = handleDataResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(databeans !=null){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(MainPageActivity.this).
                                    edit();
                            editor.putString("dataBeans",responseText);
                            Log.d("hello7","线程执行");
                            editor.apply();
                           refreshView();
                           ArticleListData articleListData = databeans.getArticleListData();

                           List<Article> mdatasList = articleListData.getArticles();
                           if(articlePage>articleListData.getPageCount()){
                               Toast.makeText(MainPageActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
                           }

                            if(articleListData==null){
                                Toast.makeText(MainPageActivity.this,databeans.getErrorCode(),Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainPageActivity.this,"数据为空",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final String message = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainPageActivity.this,message,Toast.LENGTH_LONG).show();
                    }
                });

            }


        });
    }

    public  void refreshView(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainPageActivity.this);
        String responseText = prefs.getString("dataBeans",null);
        String responseBannerText = prefs.getString("bannerData",null);
        DataBeans databeans = handleDataResponse(responseText);
        BannerData bannerData = handleBannerDataResponse(responseBannerText);
        ArticleListData articleListData = databeans.getArticleListData();


        if(articlesList!=null&&articlePage!=0){
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            articlesAdapter.addList(articleListData.getArticles());
            Log.d("hello7","bukong");


            articlesAdapter.notifyDataSetChanged();
            //直接调用这个可以告诉RecycleView数据的变化
           /* mainPageRecyclerView.setAdapter(articlesAdapter);


            mainPageRecyclerView.scrollToPosition(totalItemCount-visibleItemCount+1);*/
            loading=false;

        }else{
            Log.d("hello7","kong");
            banners = bannerData.getBannerList();
            articlesList = articleListData.getArticles();
            BannerAdapter bannerAdapter = new BannerAdapter(MainPageActivity.this,banners);
            articlesAdapter = new ArticlesAdapter(MainPageActivity.this,articlesList,bannerAdapter);
            bannerPlayThread = articlesAdapter.getViewPagerThread();
            bannerPlayThread.start();
            mainPageRecyclerView.setAdapter(articlesAdapter);

            }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerlayout==null)
                    Toast.makeText(this,"mlayout为空",Toast.LENGTH_SHORT).show();
                if (mDrawerlayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerlayout.closeDrawers();
                } else {
                    mDrawerlayout.openDrawer(Gravity.START);
                }

                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.favor_list) {
            item.setCheckable(false);
           // item.setChecked(false);
            Intent intent = new Intent(MainPageActivity.this,FavorListActivity.class);
            startActivity(intent);

        }   else if (id == R.id.nav_share) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
