package com.app.activeparks.ui.home.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.ui.adapter.PhotosAdaper;
import com.app.activeparks.ui.park.ParkActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.technodreams.activeparks.R;

public class HorizontalAdaper extends RecyclerView.Adapter<HorizontalAdaper.ViewHolder> {

    private final News list;
    private final LayoutInflater inflater;
    public ParksAdaperListener parksAdaperListener;

    public HorizontalAdaper(Context context, News list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_news_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemNews news = list.getItems().get(position);

        holder.title.setText(news.getTitle() != null ? news.getTitle() : "Немає заголовку");


        if (news.getPhotos().size() > 0) {
            holder.galary.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            holder.galary.setAdapter(new PhotosAdaper(holder.itemView.getContext(), news.getPhotos()));
            holder.galary.setVisibility(View.VISIBLE);
            holder.tabLayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            new TabLayoutMediator(holder.tabLayout, holder.galary, (tab, pos) -> {
            }).attach();
        }else {
            Glide.with(holder.itemView.getContext()).load(news.getImageUrl()).into(holder.image);
            holder.galary.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            holder.tabLayout.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(v -> {
            parksAdaperListener.onInfo(news);
        });
    }

    @Override
    public int getItemCount() {
        return list.getItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title;
        final ImageView image;
        final ViewPager2 galary;
        final TabLayout tabLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image_club);
            galary = itemView.findViewById(R.id.galary);
            tabLayout = itemView.findViewById(R.id.list_tab);
        }
    }

    public interface ParksAdaperListener{
        void onInfo(ItemNews news);
    }

    public HorizontalAdaper setOnCliclListener(ParksAdaperListener parksAdaperListener){
        this.parksAdaperListener = parksAdaperListener;
        return this;
    }

}
