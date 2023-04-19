package com.app.activeparks.ui.video.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.video.Item;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class VideoAdaper extends RecyclerView.Adapter<VideoAdaper.ViewHolder> {

    private final List<Item> list;
    private final LayoutInflater inflater;
    private  VideoAdaperListener videoAdaperListener;

    public VideoAdaper(Context context, List<Item> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = list.get(position);

        holder.nameVideo.setText(item.getTitle());

        Glide.with(holder.itemView.getContext()).load(item.getMainPhoto()).error(R.drawable.ic_prew).into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            videoAdaperListener.onClick(item.getId(), item.getCategoryId(), item.getExerciseDifficultyLevelId());
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView nameVideo;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameVideo = itemView.findViewById(R.id.name_video);
            imageView = itemView.findViewById(R.id.image_video);
        }
    }

    public VideoAdaper setOnClickListener(VideoAdaperListener videoAdaperListener){
        this.videoAdaperListener  = videoAdaperListener;
        return this;
    }

    public interface VideoAdaperListener{
        void onClick(String id, String categoryId, String exerciseDifficultyLevelId);
    }


}
