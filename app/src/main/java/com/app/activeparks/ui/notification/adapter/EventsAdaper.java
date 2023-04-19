package com.app.activeparks.ui.notification.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

public class EventsAdaper extends RecyclerView.Adapter<EventsAdaper.ViewHolder> {

    private final SportEvents list;
    private final LayoutInflater inflater;
    public ParksAdaperListener parksAdaperListener;

    public Boolean type;

    public EventsAdaper(Context context, SportEvents list, Boolean type){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.type = type;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemEvent event = list.getItems().get(position);
        holder.title.setText(event.getTitle());
        holder.description.setText(event.getShortDescription());
        Glide.with(holder.itemView.getContext()).load(event.getImageUrl()).error(R.drawable.ic_prew).into(holder.imageView);

        holder.eventButton.setText(type == true ? "Оцінити захід" : "Перейти до заходу");

        holder.eventButton.setOnClickListener(v -> {
            parksAdaperListener.onInfo(event.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.getItems().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        final ImageView imageView;

        final Button eventButton;
        final TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.photo);
            eventButton = itemView.findViewById(R.id.action_get_result);
        }
    }

    public interface ParksAdaperListener{
        void onInfo(String id);
    }

    public EventsAdaper setOnCliclListener(ParksAdaperListener parksAdaperListener){
        this.parksAdaperListener = parksAdaperListener;
        return this;
    }

}
