package com.app.activeparks.ui.result.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.notification.ItemNotification;
import com.technodreams.activeparks.R;

import java.util.List;

public class ResultAdaper extends RecyclerView.Adapter<ResultAdaper.ViewHolder> {

    private final List<ItemNotification> list;
    private final LayoutInflater inflater;

    public ResultAdaper(Context context, List<ItemNotification> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemNotification notification = list.get(position);

        if (notification.getEvent() == null) {
            holder.title.setText(notification.getMessage());
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

}
