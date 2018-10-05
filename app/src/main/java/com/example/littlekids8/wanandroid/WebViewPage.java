package com.example.littlekids8.wanandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.example.littlekids8.wanandroid.ArticlesAdapter;
import com.example.littlekids8.wanandroid.BannerAdapter;
import com.example.littlekids8.wanandroid.gson.Article;
import com.example.littlekids8.wanandroid.gson.Banner;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.sql.DataSource;

public class WebViewPage extends AppCompatActivity {

    private WebView webView;
     private ActionBar actionBar;
    private boolean isfavor;
    private boolean isBanner;
     private Banner banner;
     private Article article;
     String pageOriginUrl;
     private String requestUrl;
     private String currentUrl;
    private MenuItem item;
     private Article urlArticle;

    static String[] arr = {"a"};
    private ArrayList<String> favorUrl =new ArrayList<String>();


    @Override
    public void onBackPressed() {

        if(webView.canGoBack()){
            webView.goBack();

            /*newUrl = webView.getUrl();
            urlArticle = new Article();
            urlArticle.setLink(newUrl);
            urlArticle.setAuthor("网页");
            urlArticle.setTitle(newUrl);
            urlArticle.setItemId((int)(System.currentTimeMillis()/1000));*/
           /* Log.d("Favor",newUrl=webView.getUrl());
            Log.d("Favor",favorUrl.get(favorUrl.size()-1));
            if(((!webView.canGoBack())&&isfavor)||favorUrl.contains(newUrl))
                item.setIcon(R.drawable.ic_star_red_500_24dp);
            else
                item.setIcon(R.drawable.ic_star_border_red_500_24dp);*/

        }

        else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_page);

        Toolbar mActionbar = (Toolbar)findViewById(R.id.webview_toolbar) ;

        setSupportActionBar(mActionbar);

        actionBar = getSupportActionBar();


        webView = (WebView)findViewById(R.id.web_view);
        webView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        Intent intent = getIntent();

        pageOriginUrl = "http://www.baidu.com";

        if(intent.getIntExtra("type",-1)==BannerAdapter.BANNER) {
            isBanner=true;
            banner = (Banner)intent.getSerializableExtra("banner");
            pageOriginUrl=banner.getUrl();
        }
        else if(intent.getIntExtra("type",-1)==ArticlesAdapter.ARTICLE){
            isBanner=false;
            article = (Article)intent.getSerializableExtra("article");
            pageOriginUrl = article.getLink();

        }else {
            isBanner=false;
            Toast.makeText(this,"内部错误",Toast.LENGTH_SHORT).show();

            finish();
        }
        isfavor = isIdContain();
        if(isfavor){
            favorUrl.add(pageOriginUrl);
        }

        final String title = "Loading..";
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);



        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                actionBar.setTitle( view.getTitle());
                if(urlArticle!=null)
                    urlArticle.setTitle(view.getTitle());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                actionBar.setTitle( view.getTitle());
                /*if(urlArticle!=null)
                    urlArticle.setTitle(view.getTitle());*/

                currentUrl = webView.getUrl();
                Log.d("Favor",currentUrl);
                if (!favorUrl.isEmpty())
                Log.d("Favor",favorUrl.get(favorUrl.size()-1));
                if(((!webView.canGoBack())&&isfavor)||favorUrl.contains(currentUrl))
                    item.setIcon(R.drawable.ic_star_red_500_24dp);
                else
                    item.setIcon(R.drawable.ic_star_border_red_500_24dp);
                /*Toast.makeText(WebViewPage.this,"完成",Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //加载新的url时会调用
                super.shouldOverrideUrlLoading(view, request);
                requestUrl = request.getUrl().toString();
                actionBar.setTitle(" Loading...");

                urlArticle = new Article();

                urlArticle.setAuthor("网页");
                urlArticle.setTitle(currentUrl);
                urlArticle.setItemId((int)(System.currentTimeMillis()/1000));
                item.setIcon(R.drawable.ic_star_border_red_500_24dp);

                Toast.makeText(WebViewPage.this,currentUrl,Toast.LENGTH_SHORT).show();
                return false;
            }

        });
        webView.loadUrl(pageOriginUrl);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Toast.makeText(this,"finish",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        item=menu.findItem(R.id.favor);
        Log.d("favorite","onCreateOptionsMenu");
        if(isfavor)
            item.setIcon(R.drawable.ic_star_red_500_24dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        case R.id.favor:
            if(!isfavor&&(!webView.canGoBack())){//未收藏且为原始链接
                isfavor=true;
                if(isBanner){
                   banner.save();
                    Article bannerArticle = new Article();
                    bannerArticle.setNiceDate(getCurrentTime());
                    bannerArticle.setItemId(banner.getItemId());
                    bannerArticle.setTitle(banner.getTitle());
                    bannerArticle.setAuthor("首页Banner");
                    bannerArticle.setLink(banner.getUrl());
                    bannerArticle.save();

                    Log.d("remenber","已经入3");
                }
                else{
                    Article nnArticle = new Article();
                    nnArticle.setNiceDate(getCurrentTime());
                    nnArticle.setItemId(article.getItemId());
                    nnArticle.setTitle(article.getTitle());
                    nnArticle.setAuthor(article.getAuthor());
                    nnArticle.setLink(article.getLink());

                    Log.d("Favor","收藏"+article.getLink());
                    Log.d("Favor",Integer.toString(favorUrl.size()));
                    favorUrl.add(article.getLink());
                    Log.d("Favor",Integer.toString(favorUrl.size()));
                    nnArticle.save();



                    /*article.setNiceDate(getCurrentTime());
                     article.save();*/
                    Boolean ifSave =nnArticle.isSaved();

                    List<Article> articles = LitePal.findAll(Article.class);
                    for(Article mArticle : articles){
                        Log.d("remenber","***"+Integer.toString(mArticle.getItemId()));}
                    Log.d("remenber","**"+Boolean.toString(ifSave)+"收藏成功1+已经入4-----------------");
                }

                Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                item.setIcon(R.drawable.ic_star_red_500_24dp);
            }else if(isfavor&&!webView.canGoBack()){
                if(isBanner){
                    LitePal.deleteAll(Banner.class,"itemId=?",Integer.toString(banner.getItemId()));
                    LitePal.deleteAll(Article.class,"itemId=?",Integer.toString(banner.getItemId()));
                }
                else{
                    LitePal.deleteAll(Article.class,"itemId=?",Integer.toString(article.getItemId()));
                    if (favorUrl!=null)favorUrl.remove(article.getLink());
                }

                Toast.makeText(this,"已取消收藏",Toast.LENGTH_SHORT).show();
                isfavor=false;
                item.setIcon(R.drawable.ic_star_border_red_500_24dp);
                List<Article> articles = LitePal.findAll(Article.class);
                for(Article mArticle : articles){
                    Log.d("remenber","***"+Integer.toString(mArticle.getItemId()));}
                Log.d("remenber","已经入5+取消收藏");

            }else if(!isArticleItemIdContain(urlArticle)&&webView.canGoBack() ){

                    Article newUrlArticle = new Article();
                    newUrlArticle.setAuthor("网页");
                    newUrlArticle.setLink(webView.getUrl());
                    newUrlArticle.setTitle(webView.getTitle());
                    newUrlArticle.setNiceDate("收藏于"+getCurrentTime());
                    newUrlArticle.setItemId((int)(System.currentTimeMillis()/1000));
                    newUrlArticle.save();

                    Log.d("Favor","收藏"+newUrlArticle.getLink());
                    Log.d("Favor",Integer.toString(favorUrl.size()));
                    favorUrl.add(newUrlArticle.getLink());
                    Log.d("Favor",Integer.toString(favorUrl.size()));

                    item.setIcon(R.drawable.ic_star_red_500_24dp);
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();


            }else if(isArticleItemIdContain(urlArticle)&&webView.canGoBack()){
                LitePal.deleteAll(Article.class,"itemId=?",Integer.toString(urlArticle.getItemId()));
                if (favorUrl!=null)favorUrl.remove(urlArticle.getLink());
                item.setIcon(R.drawable.ic_star_border_red_500_24dp);
                Toast.makeText(this,"已取消收藏",Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(WebViewPage.this,"unkonwn Error",Toast.LENGTH_SHORT).show();

            break;
        }
            return true;
    }



    public boolean isArticleItemIdContain(Article article){
        if(article!=null) {
            int itemId = article.getItemId();
            List<Article> articles = LitePal.findAll(Article.class);
            for (Article mArticle : articles) {
                if (mArticle.getItemId() == itemId) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isIdContain(){
        if(isBanner) {
            int id = banner.getItemId();
            List<Banner> banners = LitePal.findAll(Banner.class);

            Log.d("remenber","已经入1");

            for (Banner mBanner : banners){

                Log.d("remenber",Integer.toString(id)+"***"+Integer.toString(mBanner.getItemId()));

                if(mBanner.getItemId()==id)
                    return true;
            }
        }
        else{
            int id = article.getItemId();
            List<Article> articles = LitePal.findAll(Article.class);

            Log.d("remenber","已经入2+检测收藏与否");

            for(Article mArticle : articles){
                Log.d("remenber",Integer.toString(id)+"***"+Integer.toString(mArticle.getItemId()));
                if(mArticle.getItemId()==id)
                    return true;
            }
        }
        return false;
    }

    public String getCurrentTime(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String currentTime = df.format(new Date());// new Date()为获取当前系统时间
       return currentTime;
    }
}
