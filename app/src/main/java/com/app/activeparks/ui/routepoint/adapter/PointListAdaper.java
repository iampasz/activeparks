package com.app.activeparks.ui.routepoint.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.points.RoutePoint;
import com.technodreams.activeparks.R;

import java.util.List;

public class PointListAdaper extends RecyclerView.Adapter<PointListAdaper.ViewHolder> {

    private final List<RoutePoint> list;
    private final LayoutInflater inflater;
    private EventsListener eventListener;

    public Boolean isCoordinator;

    public PointListAdaper(Context context, List<RoutePoint> list, Boolean coordinator){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.isCoordinator = coordinator;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_route, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoutePoint point = list.get(position);

        if (position == 0){
            holder.point.setText("Старт");
        }else if (position == list.size() - 1){
            holder.point.setText("Фініш");
        }else {
            holder.point.setText("" + (position + 1));
        }

        holder.itemView.setOnClickListener(v -> eventListener.onInfo(point));

        holder.qrCode.setOnClickListener(v -> eventListener.onShowQrCode(point.getEventId(), point.getQrCode()));

        if (point.getPassedPoints()){
            holder.pointStatus.setVisibility(View.VISIBLE);
        }else {
            holder.pointStatus.setVisibility(View.INVISIBLE);
        }

        if (isCoordinator){
            holder.qrCode.setVisibility(View.VISIBLE);
            holder.pointStatus.setVisibility(View.GONE);
        }

        if (point.getTimePassed().length() > 11 && !point.getTimePassed().contains("Invalid date")){
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
        void onInfo(RoutePoint item);
        void onShowQrCode(String eventId, String pointId);
    }

    public PointListAdaper setOnEventListener(EventsListener eventListener){
        this.eventListener = eventListener;
        return this;
    }

}
