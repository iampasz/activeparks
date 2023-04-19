package com.app.activeparks.ui.home.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.data.model.sportsgrounds.Sportsgrounds;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

public class ParkHorizontalAdaper extends RecyclerView.Adapter<ParkHorizontalAdaper.ViewHolder> {

    private final Sportsgrounds list;
    private final LayoutInflater inflater;
    public ParksAdaperListener parksAdaperListener;

    public ParkHorizontalAdaper(Context context, Sportsgrounds list){
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
        ItemSportsground event = list.getSportsground().get(position);
        holder.title.setText(event.getTitle());
        holder.distance.setVisibility(View.VISIBLE);
        holder.distance.setText("Дистанція: " + round(event.getDistanceToPoint(), 2)  + " км.");

        Glide.with(holder.itemView.getContext()).load(event.getPhoto()).error(R.drawable.ic_prew).into(holder.imageView);


        holder.itemView.setOnClickListener(v -> {
            parksAdaperListener.onInfo(event.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.getSportsground().size() > 5 ? 5 : list.getSportsground().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title, distance;
        final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            distance = itemView.findViewById(R.id.distance);
            imageView = itemView.findViewById(R.id.image_club);
        }
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public interface ParksAdaperListener{
        void onInfo(String id);
    }

    public ParkHorizontalAdaper setOnCliclListener(ParksAdaperListener parksAdaperListener){
        this.parksAdaperListener = parksAdaperListener;
        return this;
    }

}
