package com.app.activeparks.ui.home.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (news.getPublishedAt() != null) {
                Date data = format.parse(news.getPublishedAt());
                holder.data.setText("Опубліковано: " + new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(data));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (news.getBody() != null) {
            holder.description.setVisibility(View.VISIBLE);
            String web = "<html><head><LINK href=\"https://ap.sportforall.gov.ua/images/index.css\" rel=\"stylesheet\"/></head><body>" + news.getBody() + "</body></html>";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.description.setText(Html.fromHtml(web, null, null));
            } else {
                holder.description.setText(Html.fromHtml(web));
            }
        }else{
            holder.description.setVisibility(View.GONE);
        }

        if (news.getPhotos().size() > 0) {
            holder.galary.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            holder.galary.setAdapter(new PhotosAdaper(holder.itemView.getContext(), news.getPhotos()));
            holder.galary.setVisibility(View.VISIBLE);
            holder.tabLayout.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);

            new TabLayoutMediator(holder.tabLayout, holder.galary, (tab, pos) -> {
            }).attach();
        }else {
            Glide.with(holder.itemView.getContext()).load(news.getImageUrl()).error(R.drawable.ic_prew).into(holder.image);
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

        final TextView title, data, description;
        final ImageView image;
        final ViewPager2 galary;
        final TabLayout tabLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            data = itemView.findViewById(R.id.data);
            description = itemView.findViewById(R.id.description);
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
