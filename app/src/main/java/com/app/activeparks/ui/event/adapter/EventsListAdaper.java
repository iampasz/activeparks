package com.app.activeparks.ui.event.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsListAdaper extends RecyclerView.Adapter<EventsListAdaper.ViewHolder> {

    private final List<ItemEvent> list;
    private final LayoutInflater inflater;
    private EventsListener eventListener;

    public EventsListAdaper(Context context, List<ItemEvent> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle() != null ? list.get(position).getTitle() : "Недіомо");

        if (list.get(position).getSportsground().getTitle() != null){
            holder.city.setText(list.get(position).getSportsground().getTitle());
        }else{
            if (list.get(position).getRoutePoints() != null && list.get(position).getRoutePoints().size() > 0) {
                holder.panelLocation.setVisibility(View.VISIBLE);
                holder.city.setText("Показати на мапі");
                holder.panelLocation.setOnClickListener(v -> {
                    double lat = list.get(position).getRoutePoints().get(0).getLocation().get(0);
                    double lon = list.get(position).getRoutePoints().get(0).getLocation().get(1);
                    eventListener.onOpenMaps(lat, lon);
                });
            }else {
                holder.panelLocation.setVisibility(View.GONE);
            }
        }

        holder.time.setText(list.get(position).getStartsAt() != null ? list.get(position).getStartsAt().substring(11, list.get(position).getStartsAt().length()) : "Недіомо");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(list.get(position).getStartsAt());
            holder.data.setText( new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.status.setText(list.get(position).getHoldingStatusId() != null ? list.get(position).getHoldingStatusId() : "Недіомо");

        holder.description.setText(list.get(position).getShortDescription() != null ? list.get(position).getShortDescription() : "Недіомо");

        if (list.get(position).getImageUrl() != null) {
            Glide.with(holder.itemView.getContext()).load(list.get(position).getImageUrl()).into(holder.logo);
        }

        if (position != 0) {
            if (list.get(position).getStartsAt().substring(0, 10).equals(list.get(position - 1).getStartsAt().substring(0, 10))) {
                holder.data.setVisibility(View.GONE);
            } else {
                holder.data.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            eventListener.onInfo(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView logo;
        final LinearLayout fon, panelLocation;
        final TextView title, time, city, data, status, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fon = itemView.findViewById(R.id.fon);
            panelLocation = itemView.findViewById(R.id.panel_location);

            logo = itemView.findViewById(R.id.photo);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.text_time);
            city = itemView.findViewById(R.id.text_location);
            data = itemView.findViewById(R.id.data);
            status = itemView.findViewById(R.id.status);
            description = itemView.findViewById(R.id.description);
        }
    }

    public interface EventsListener{
        void onInfo(ItemEvent itemClub);
        void onOpenMaps(double lat, double lon);
    }

    public EventsListAdaper setOnEventListener(EventsListener eventListener){
        this.eventListener = eventListener;
        return this;
    }

}
