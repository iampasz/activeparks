package com.app.activeparks.ui.support.adapter;


import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.notification.ItemNotification;
import com.app.activeparks.data.model.support.SupportItem;
import com.app.activeparks.ui.notification.adapter.EventsAdaper;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SupportAdaper extends RecyclerView.Adapter<SupportAdaper.ViewHolder> {

    private final List<SupportItem> list;
    private final LayoutInflater inflater;

    private SupportAdaperListener supportAdaperListener;

    public SupportAdaper(Context context, List<SupportItem> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_support, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportItem item = list.get(position);


        holder.title.setText(item.getTopic());
        holder.status.setText(item.getStatusId());

        if (item.getCreatedBy() != null) {
            if (item.getCreatedBy().getPhoto() != null) {
                Glide.with(holder.itemView.getContext()).load(list.get(position).getCreatedBy().getPhoto()).error(R.drawable.ic_prew).into(holder.photo);
            }
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(item.getCreatedAt());
            holder.data.setText( new SimpleDateFormat("dd.MM.yyyy HH:mm", new Locale("uk", "UA")).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
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

        final TextView title, status, data;
        ImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            status = itemView.findViewById(R.id.status);
            data = itemView.findViewById(R.id.data);
            photo = itemView.findViewById(R.id.photo);
        }
    }

    public interface SupportAdaperListener{
        void onInfo(String id);
    }

    public SupportAdaper setOnCliclListener(SupportAdaperListener supportAdaperListener){
        this.supportAdaperListener = supportAdaperListener;
        return this;
    }

}
