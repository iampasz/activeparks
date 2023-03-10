package com.app.activeparks.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.city.Body;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class PhotosAdaper extends RecyclerView.Adapter<PhotosAdaper.ViewHolder> {

    private final List<String> list;
    private final LayoutInflater inflater;
    public SearchAdaperListener searchAdaperListener;

    public PhotosAdaper(Context context, List<String> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(holder.itemView.getContext()).load(list.get(position)).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.image);
        }
    }

    public interface SearchAdaperListener{
        void onInfo(Body item);
    }

    public PhotosAdaper setOnCliclListener(PhotosAdaper.SearchAdaperListener searchAdaperListener){
        this.searchAdaperListener = searchAdaperListener;
        return this;
    }

}
