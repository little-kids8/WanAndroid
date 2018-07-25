package com.example.littlekids8.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.littlekids8.wanandroid.gson.Banner;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

    final  static public int BANNER=0;
    private List<Banner> banners;
    private Context context;
    private LayoutInflater inflater;

    public BannerAdapter(Context context,List<Banner> banners){
        this.context = context;
        this.banners = banners;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,final int position) {
        View view = inflater.inflate(R.layout.banner_item,container,false);
        ImageView bannerImage = view.findViewById(R.id.banner_image);
        Glide.with(context).load(banners.get(position).getImagePath()).into(bannerImage);
        TextView bannerTitle = view.findViewById(R.id.banner_titel);
        bannerTitle.setText(banners.get(position).getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,WebViewPage.class);
                intent.putExtra("banner",banners.get(position));
                intent.putExtra("type",BANNER);
                context.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
        //super.destroyItem(container, position, object);
    }
}
