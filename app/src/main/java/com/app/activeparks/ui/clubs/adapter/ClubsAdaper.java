package com.app.activeparks.ui.clubs.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class ClubsAdaper extends RecyclerView.Adapter<ClubsAdaper.ViewHolder> {

    private final List<ItemClub> list;
    private final LayoutInflater inflater;
    private ClubsListener clubsListener;

    public ClubsAdaper(Context context, List<ItemClub> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_club_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getName());

        Glide.with(holder.itemView.getContext()).load(list.get(position).getLogoUrl()).into(holder.logo);

        holder.itemView.setOnClickListener(v -> {
            clubsListener.onInfo(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView logo;
        final TextView title, city;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.image_club);
            title = itemView.findViewById(R.id.title);
            city = itemView.findViewById(R.id.city);
        }
    }

    public ClubsAdaper clear(){
        list.clear();
        return this;
    }

    public interface ClubsListener{
        void onInfo(ItemClub itemClub);
    }

    public ClubsAdaper setOnClubsListener(ClubsListener clubsListener){
        this.clubsListener = clubsListener;
        return this;
    }

}
