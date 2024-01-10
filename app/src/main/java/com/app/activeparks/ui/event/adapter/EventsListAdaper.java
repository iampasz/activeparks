package com.app.activeparks.ui.event.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.DecimalFormat;
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

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemEvent item = list.get(position);

        holder.title.setText(item.getTitle() != null ? item.getTitle() : "Невідомо");

        if (item.getRoutePoints() != null && item.getRoutePoints().size() > 0) {
            holder.panelLocation.setVisibility(View.VISIBLE);
            holder.panelLocation.setOnClickListener(v -> {
                double lat = item.getRoutePoints().get(0).getLocation().get(0);
                double lon =item.getRoutePoints().get(0).getLocation().get(1);
                eventListener.onOpenMaps(lat, lon);
            });
        }else {
            holder.panelLocation.setVisibility(View.GONE);
        }

        if (item.getStartsAt() != null && item.getFinishesAt() != null ){
            String startsAt = item.getStartsAt().substring(11, item.getStartsAt().length() - 3);
            String finishesAt = item.getFinishesAt().substring(11, item.getFinishesAt().length() - 3);
            holder.time.setText(startsAt + " - " + finishesAt);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (item.getStartsAt().length() > 16) {
                Date date = format.parse(item.getStartsAt());
                assert date != null;
                holder.data.setText(new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (item.getDistanceToPoint() > 0){
            DecimalFormat df = new DecimalFormat("#.##");
            String roundedNumber = df.format(item.getDistanceToPoint());
            holder.location.setText("Місце проведення заходу (" + roundedNumber + "км.)");
        }else{
            holder.location.setText("Місце проведення заходу");
        }

        if (item.getStartAdressPoint() != null){
            holder.city.setText(item.getStartAdressPoint());
        }else {
            holder.city.setText("Показати на мапі");
        }


        holder.status.setText(item.getHoldingStatusText() != null ? item.getHoldingStatusText() : "Недіомо");

        if (!item.getHoldingStatusId().contains("0q8a6xc0-1nb4-1pr4-h5at-4sw3m0l387yp")) {
            holder.status.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.button_color));
            holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.white));
        }else {
            holder.status.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.button_gray));
            holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.text_color));
        }

        holder.description.setText(item.getShortDescription() != null ? item.getShortDescription() : "Недіомо");

        if (item.getImageUrl() != null) {
            Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).error(R.drawable.ic_prew).into(holder.logo);
        }

        holder.itemView.setOnClickListener(v -> {
            Log.i("LMLKLK","adapter");
            eventListener.onInfo(item);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView logo;
        final LinearLayout fon, panelLocation;
        final TextView title, time, city, location, data, status, description;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fon = itemView.findViewById(R.id.fon);
            panelLocation = itemView.findViewById(R.id.panel_location);

            logo = itemView.findViewById(R.id.photo);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.text_time);
            city = itemView.findViewById(R.id.text_location);
            location = itemView.findViewById(R.id.text_location_description);
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
