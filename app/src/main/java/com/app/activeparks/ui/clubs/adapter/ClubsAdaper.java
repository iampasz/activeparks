package com.app.activeparks.ui.clubs.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.util.Statuses;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class ClubsAdaper extends RecyclerView.Adapter<ClubsAdaper.ViewHolder> {

    private final List<ItemClub> list;
    private final LayoutInflater inflater;
    private ClubsListener clubsListener;

    private Statuses statuses = new Statuses();

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
        ItemClub item = list.get(position);

        holder.title.setText(item.getName());
        holder.description.setText(item.getDescription());

        Glide.with(holder.itemView.getContext()).load(item.getLogoUrl()).error(R.drawable.ic_prew).into(holder.logo);

        if (item.isUser() != null) {
            if (item.isUser().contains("userIsMember")) {
                holder.status.setText("Ви учасник");
                holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
                holder.status.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.button_yellow));
            } else {
                holder.status.setText("Ви координатор");
                holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.text_color));
                holder.status.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.button_gray));
            }
        }else{
            holder.status.setText(statuses.title(item.getStatusId()));
            holder.status.setTextColor(inflater.getContext().getResources().getColor(statuses.textColor(item.getStatusId())));
            holder.status.setBackground(inflater.getContext().getResources().getDrawable(statuses.color(item.getStatusId())));
        }

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
        final Button status;
        final TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.image_club);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            status = itemView.findViewById(R.id.status);
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
