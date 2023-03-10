package com.app.activeparks.ui.home.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

public class HorizontalAdaperEvents extends RecyclerView.Adapter<HorizontalAdaperEvents.ViewHolder> {

    private final SportEvents list;
    private final LayoutInflater inflater;
    public ParksAdaperListener parksAdaperListener;

    public HorizontalAdaperEvents(Context context, SportEvents list){
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
        ItemEvent event = list.getItems().get(position);
        holder.title.setText(event.getTitle());
        Glide.with(holder.itemView.getContext()).load(event.getImageUrl()).into(holder.imageView);


        holder.itemView.setOnClickListener(v -> {
            parksAdaperListener.onInfo(event.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.getItems().size() > 5 ? 5 : list.getItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image_club);
        }
    }

    public interface ParksAdaperListener{
        void onInfo(String id);
    }

    public HorizontalAdaperEvents setOnCliclListener(ParksAdaperListener parksAdaperListener){
        this.parksAdaperListener = parksAdaperListener;
        return this;
    }

}
