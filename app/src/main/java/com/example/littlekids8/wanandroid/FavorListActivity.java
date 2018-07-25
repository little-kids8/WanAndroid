package com.example.littlekids8.wanandroid;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.littlekids8.wanandroid.gson.Article;

import org.litepal.LitePal;

import java.util.List;

public class FavorListActivity extends AppCompatActivity {
    List<Article> articles;
    RecyclerView favorArticleList;
    LinearLayoutManager favorListLayoutManager;
    FavorArticleAdapter favorArticleAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor_list);

        Toolbar toolbar = (Toolbar)findViewById(R.id.favor_page_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("我的收藏");




        favorArticleList = (RecyclerView)findViewById(R.id.favor_recycle_view);
        favorListLayoutManager = new LinearLayoutManager(this);
        favorArticleList.setLayoutManager(favorListLayoutManager);

        /* articles = LitePal.order("id desc").find(Article.class);*/
       /* favorArticleAdapter = new FavorArticleAdapter(this,articles);
        favorArticleList.setAdapter(favorArticleAdapter);*/

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.favor_swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articles = LitePal.order("id desc").find(Article.class);
                favorArticleAdapter = new FavorArticleAdapter(FavorListActivity.this,articles);
                favorArticleList.setAdapter(favorArticleAdapter);
                Toast.makeText(FavorListActivity.this,"刷新完成",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        articles = LitePal.order("id desc").find(Article.class);
        favorArticleAdapter = new FavorArticleAdapter(FavorListActivity.this,articles);
        favorArticleList.setAdapter(favorArticleAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
