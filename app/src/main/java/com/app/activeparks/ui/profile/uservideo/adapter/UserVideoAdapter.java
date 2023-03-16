package com.app.activeparks.ui.profile.uservideo.adapter;


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
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.ViewHolder> {

    private final List<UserVideoItem> list;
    private final LayoutInflater inflater;
    private UserVideoListener userVideoListener;


    public UserVideoAdapter(Context context, List<UserVideoItem> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user_video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());

        holder.itemView.setOnClickListener(view -> {
            userVideoListener.onInfo(list.get(position));
        });

        if (position == 0){
            holder.linerHeader.setVisibility(View.VISIBLE);
        }else{
            holder.linerHeader.setVisibility(View.GONE);
        }

        Glide.with(holder.itemView.getContext()).load(list.get(position).getMainPhoto()).error(R.drawable.ic_video).into(holder.imageVideo);

        holder.actionCreate.setOnClickListener(v -> {userVideoListener.onCreate();});
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView title, status;
        final ImageView imageVideo, actionCreate;
        final LinearLayout linerHeader;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.name_video);
            status = itemView.findViewById(R.id.status);
            linerHeader = itemView.findViewById(R.id.liner_header);
            actionCreate = itemView.findViewById(R.id.action_create);
            imageVideo = itemView.findViewById(R.id.image_video);
        }
    }

    public interface UserVideoListener{
        void onInfo(UserVideoItem itemClub);
        void onCreate();
    }

    public UserVideoAdapter setOnUserVideoListener(UserVideoListener userVideoListener){
        this.userVideoListener = userVideoListener;
        return this;
    }

}
