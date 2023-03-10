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

import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class PointListAdaper extends RecyclerView.Adapter<PointListAdaper.ViewHolder> {

    private final List<RoutePoint> list;
    private final LayoutInflater inflater;
    private EventsListener eventListener;

    public Boolean isCoordinator = false;

    public PointListAdaper(Context context, List<RoutePoint> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoutePoint point = list.get(position);

        holder.point.setText("" + (position + 1));

        holder.itemView.setOnClickListener(v -> {
            eventListener.onInfo(true);
        });

        holder.qrCode.setOnClickListener(v -> {
            eventListener.onShowQrCode(point.getEventId(), point.getQrCode());
        });

        if (isCoordinator == true){
            holder.qrCode.setVisibility(View.VISIBLE);
            holder.pointStatus.setVisibility(View.GONE);
        }

        holder.pointStatus.setText(point.getPassedPoints() ? "Пройдена точка" : "Наступна точка");

        if (position != 0 && list.get(position - 1).getPassedPoints() == false){
            holder.pointStatus.setVisibility(View.INVISIBLE);
        }else {
            holder.pointStatus.setVisibility(View.VISIBLE);
        }

        if (list.get(position).getTimePassed().length() > 11){
            holder.timePoint.setText(point.getTimePassed().substring(11));
        }else {
            holder.timePoint.setText("---");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView point, time, result, timePoint, pointStatus;
        final ImageView qrCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            point = itemView.findViewById(R.id.text_point);
            time = itemView.findViewById(R.id.text_time);
            result = itemView.findViewById(R.id.action_get_result);
            pointStatus = itemView.findViewById(R.id.point_status);
            timePoint = itemView.findViewById(R.id.time_point);

            qrCode = itemView.findViewById(R.id.qr_action);
        }
    }

    public interface EventsListener{
        void onInfo(Boolean status);
        void onShowQrCode(String eventId, String pointId);
    }

    public PointListAdaper setOnEventListener(EventsListener eventListener){
        this.eventListener = eventListener;
        return this;
    }

}
