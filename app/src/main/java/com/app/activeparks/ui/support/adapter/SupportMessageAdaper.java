package com.app.activeparks.ui.support.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.support.SupportItem;
import com.app.activeparks.data.model.support.SupportMessages;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class SupportMessageAdaper extends RecyclerView.Adapter<SupportMessageAdaper.ViewHolder> {

    private final List<SupportMessages> list;
    private final LayoutInflater inflater;

    private SupportAdaperListener supportAdaperListener;

    public SupportMessageAdaper(Context context, List<SupportMessages> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_support_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportMessages item = list.get(position);

        holder.name.setText(item.getCreatedBy().getFirstName() + " " + item.getCreatedBy().getLastName());
        holder.message.setText(item.getText());
        holder.data.setText(item.getCreatedAt());

        if (item.getCreatedBy() != null) {
            if (item.getCreatedBy().getPhoto() != null) {
                Glide.with(holder.itemView.getContext()).load(list.get(position).getCreatedBy().getPhoto()).into(holder.photo);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            supportAdaperListener.onInfo(item.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView name, message, data;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            data = itemView.findViewById(R.id.data);
            photo = itemView.findViewById(R.id.photo);
        }
    }

    public interface SupportAdaperListener{
        void onInfo(String id);
    }

    public SupportMessageAdaper setOnCliclListener(SupportAdaperListener supportAdaperListener){
        this.supportAdaperListener = supportAdaperListener;
        return this;
    }

}
