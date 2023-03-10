package com.app.activeparks.ui.event.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.technodreams.activeparks.R;

import java.util.List;

public class MeetingListAdaper extends RecyclerView.Adapter<MeetingListAdaper.ViewHolder> {

    private final List<MeetingsModel.MeetingItem> list;
    private final LayoutInflater inflater;
    private MeetingListener eventListener;

    public Boolean isCoordinator = false;

    public MeetingListAdaper(Context context, List<MeetingsModel.MeetingItem> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeetingsModel.MeetingItem item = list.get(position);
        
        holder.data.setText(item.getStartTime().substring(0, 5));
        holder.data.setText(item.getStartTime().substring(0, 5));
        holder.lenght.setText(item.getLength() + " хв.");
        holder.user.setText(item.getParticipants() + " хв.");

        holder.itemView.setOnClickListener(v -> {
            eventListener.onInfo(list.get(position).getUrl());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView data, time, lenght, user;
        final ImageView qrCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.text_data);
            time = itemView.findViewById(R.id.text_time);
            lenght = itemView.findViewById(R.id.text_lenght);
            user = itemView.findViewById(R.id.text_user);

            qrCode = itemView.findViewById(R.id.qr_action);
        }
    }

    public interface MeetingListener{
        void onInfo(String url);
    }

    public MeetingListAdaper setOnMeetingListener(MeetingListener eventListener){
        this.eventListener = eventListener;
        return this;
    }

}
