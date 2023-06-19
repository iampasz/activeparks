package com.app.activeparks.ui.park.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.data.model.parks.ParksItem;
import com.technodreams.activeparks.R;

import java.util.ArrayList;
import java.util.List;

public class ParkListAdaper extends RecyclerView.Adapter<ParkListAdaper.ViewHolder> {

    private final List<ParksItem> list;
    private final LayoutInflater inflater;

    private ParkListListener listener;

    public ParkListAdaper(Context context, List<ParksItem> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_parks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParksItem item = list.get(position);

        holder.title.setText(item.title);
        holder.description.setText(item.description);

        if (item.description.contains("https")) {
            holder.itemView.setOnClickListener(v -> {
                listener.onUrl(list.get(position).description);
            });
            holder.description.setTextColor(inflater.getContext().getResources().getColor(R.color.color_park));
        }else{
            holder.description.setTextColor(inflater.getContext().getResources().getColor(R.color.text_color));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }
    }

    public interface ParkListListener{
        void onUrl(String url);
    }

    public ParkListAdaper setListener(ParkListListener parkListListener){
        this.listener = parkListListener;
        return this;
    }

}
