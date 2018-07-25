package com.example.littlekids8.wanandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.bumptech.glide.load.engine.Resource;
import com.example.littlekids8.wanandroid.gson.Article;
import com.example.littlekids8.wanandroid.gson.Banner;

public class ArticlesAdapter extends RecyclerView.Adapter{

    final static public int ARTICLE = 1;
    private List<Article> mDataslist;
    private Context context;
    private BannerAdapter bannerAdapter;
    private  Thread viewPagerThread;
    private BannerViewHolder bannerViewHolder;
    private final int BANNER_ITEM = 0;
    private final int ARTICLE_ITEM = 1;


    public ArticlesAdapter(Context mContext, List<Article> mDataslist,BannerAdapter bannerAdapter){
        this.context = mContext;
        this.mDataslist = mDataslist;
        this.bannerAdapter = bannerAdapter;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{
        ViewPager viewPager;

        public BannerViewHolder(View view){
            super(view);
            viewPager = (ViewPager)view.findViewById(R.id.banner);
        }
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder{
        TextView author;
        TextView title;
        TextView niceData;
        View datasView;
        String url;

        public ArticleViewHolder(View view){
            super(view);
            datasView = view;
            author = (TextView)view.findViewById(R.id.author);
            title = (TextView)view.findViewById(R.id.title);
            niceData = (TextView)view.findViewById(R.id.niceDate);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == BANNER_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.banner,parent,false);
            bannerViewHolder = new BannerViewHolder(view);
            /*viewPagerThread = new Thread(new Runnable() {
                @Override
                public void run() {


                    SystemClock.sleep(8000);
                    while (MainPageActivity.isfront){ //很多方法都需要用到activity才能用
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            if(bannerViewHolder.viewPager.getCurrentItem()<
                                    bannerViewHolder.viewPager.getAdapter().getCount()-1) {
                                //Log.d("viewpager",Integer.toString(bannerViewHolder.viewPager.getCurrentItem()));
                                bannerViewHolder.viewPager.setCurrentItem(bannerViewHolder.viewPager.getCurrentItem() + 1);
                            }
                            else{
                                bannerViewHolder.viewPager.setCurrentItem(0);
                            }

                        }
                    });
                        SystemClock.sleep(8000);
                    }

                }
            });*/


            return bannerViewHolder;
        }
        else if(viewType==ARTICLE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
            final ArticleViewHolder viewHolder = new ArticleViewHolder(view);
            viewHolder.datasView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,WebViewPage.class);
                    int position = viewHolder.getAdapterPosition()-1;
                    intent.putExtra("article",mDataslist.get(position));
                    intent.putExtra("type",ARTICLE);
                    context.startActivity(intent);
                }
            });
            return viewHolder;
        }
        return null;



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ArticleViewHolder){
            ArticleViewHolder viewHolder = (ArticleViewHolder)holder;
            Article article = mDataslist.get(position-1);
            viewHolder.author.setText(article.getAuthor());
            viewHolder.niceData.setText(article.getNiceDate());
            viewHolder.title.setText(article.getTitle());
            viewHolder.url= article.getLink();
        }
        else if(holder instanceof BannerViewHolder){
            bannerViewHolder =(BannerViewHolder)holder;
            bannerViewHolder.viewPager.setAdapter(bannerAdapter);
            bannerViewHolder.viewPager.setPageMargin((int) TypedValue.
                    applyDimension(TypedValue.COMPLEX_UNIT_DIP,10, Resources.getSystem().getDisplayMetrics()));
        }

    }

    @Override
    public int getItemCount() {
        return mDataslist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return BANNER_ITEM;
        else
            return ARTICLE_ITEM;
    }

    public void addList(List<Article> newDataslist){
        mDataslist.addAll(newDataslist);
    }

    public Thread getViewPagerThread() {
        viewPagerThread = new Thread(new Runnable() {
                @Override
                public void run() {


                    SystemClock.sleep(8000);
                    while (MainPageActivity.isfront){ //很多方法都需要用到activity才能用
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            if(bannerViewHolder.viewPager.getCurrentItem()<
                                    bannerViewHolder.viewPager.getAdapter().getCount()-1) {
                                //Log.d("viewpager",Integer.toString(bannerViewHolder.viewPager.getCurrentItem()));
                                bannerViewHolder.viewPager.setCurrentItem(bannerViewHolder.viewPager.getCurrentItem() + 1);
                            }
                            else{
                                bannerViewHolder.viewPager.setCurrentItem(0);
                            }

                        }
                    });
                        SystemClock.sleep(8000);
                    }

                }
            });
        return viewPagerThread;
    }
}
