package com.example.littlekids8.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.littlekids8.wanandroid.ArticlesAdapter;

import com.example.littlekids8.wanandroid.gson.Article;

import java.util.List;

public class FavorArticleAdapter extends RecyclerView.Adapter<FavorArticleAdapter.ViewHolder>{

    private List<Article> articles;
    private Context context;

    public FavorArticleAdapter(Context mContext, List<Article> articles){
        this.context = mContext;
        this.articles = articles;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView title;
        TextView niceData;
        View datasView;
        String url;

        public ViewHolder(View view){
            super(view);
            datasView = view;
            author = (TextView)view.findViewById(R.id.author);
            title = (TextView)view.findViewById(R.id.title);
            niceData = (TextView)view.findViewById(R.id.niceDate);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.datasView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WebViewPage.class);
                int position = viewHolder.getAdapterPosition();
                intent.putExtra("article",articles.get(position));
                intent.putExtra("type",ArticlesAdapter.ARTICLE);
                context.startActivity(intent);
                }
         });
         return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Article article = articles.get(position);
        holder.author.setText(article.getAuthor());
        holder.niceData.setText(article.getNiceDate());
        holder.title.setText(article.getTitle());
        holder.url= article.getLink();
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
