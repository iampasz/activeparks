package com.app.activeparks.ui.maps.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.technodreams.activeparks.R;

import java.util.ArrayList;
import java.util.List;

public class ParksAdaper extends RecyclerView.Adapter<ParksAdaper.ViewHolder> {

    private final List<ItemSportsground> list;
    private final LayoutInflater inflater;
    public ParksAdaperListener parksAdaperListener;

    public ParksAdaper(Context context, List<ItemSportsground> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_map_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemSportsground sportsground = list.get(position);
        holder.metr.setText(sportsground.getTitle());
        holder.title.setText(sportsground.getTitle());
        holder.metr.setText(round(sportsground.getDistanceToPoint(), 2)  + " км");

        holder.mapsAction.setOnClickListener(v -> {
            parksAdaperListener.onClick(sportsground.getLocation().get(0), sportsground.getLocation().get(1));
        });

        holder.itemView.setOnClickListener(v -> {
            parksAdaperListener.onInfoPark(sportsground);
        });

        holder.infoParks.setOnClickListener(v -> {
            parksAdaperListener.onInfoPark(sportsground);
        });
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView metr, title, description;
        final Button mapsAction;
        final ImageButton infoParks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            metr = itemView.findViewById(R.id.metr);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            mapsAction = itemView.findViewById(R.id.maps_action);
            infoParks = itemView.findViewById(R.id.info_park);
        }
    }

    public interface ParksAdaperListener{
        void onClick(Double lat, Double lon);
        void onInfoPark(ItemSportsground sportsground);
    }

    public ParksAdaper setOnCliclListener(ParksAdaperListener parksAdaperListener){
        this.parksAdaperListener = parksAdaperListener;
        return this;
    }

}
