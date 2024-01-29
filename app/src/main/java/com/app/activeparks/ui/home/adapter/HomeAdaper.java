package com.app.activeparks.ui.home.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.data.model.news.ItemNewsOld;
import com.app.activeparks.data.model.news.News;
import com.app.activeparks.ui.adapter.PhotosAdaper;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeAdaper extends RecyclerView.Adapter<HomeAdaper.ViewHolder> {

    private final News list;
    private final LayoutInflater inflater;
    public ParksAdaperListener parksAdaperListener;

    public HomeAdaper(Context context, News list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemNewsOld news = list.getItems().get(position);

        holder.title.setText(news.getTitle() != null ? news.getTitle() : "Немає заголовку");


        if (news.getPhotos().size() > 0) {
            holder.galary.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            holder.galary.setAdapter(new PhotosAdaper(holder.itemView.getContext(), news.getPhotos()));
            holder.galary.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.distance.setVisibility(View.GONE);
        }else {
            Glide.with(holder.itemView.getContext()).load(news.getImageUrl()).error(R.drawable.ic_prew).into(holder.image);
            holder.galary.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(news.getPublishedAt());
            assert date != null;
            holder.distance.setText( new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(v -> parksAdaperListener.onInfo(news));
    }

    @Override
    public int getItemCount() {
        return Math.min(list.getItems().size(), 5);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title, distance;
        final ImageView image;
        final ViewPager2 galary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            distance = itemView.findViewById(R.id.distance);
            image = itemView.findViewById(R.id.image_club);
            galary = itemView.findViewById(R.id.galary);
        }
    }

    public interface ParksAdaperListener{
        void onInfo(ItemNewsOld news);
    }

    public HomeAdaper setOnCliclListener(ParksAdaperListener parksAdaperListener){
        this.parksAdaperListener = parksAdaperListener;
        return this;
    }

}
