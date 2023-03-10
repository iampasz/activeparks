package com.app.activeparks.ui.participants.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.user.User;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class UsersAdaper extends RecyclerView.Adapter<UsersAdaper.ViewHolder> {

    private final List<User> list;
    private final LayoutInflater inflater;
    private UsersListener clubsListener;

    public UsersAdaper(Context context, List<User> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());
        holder.login.setText(list.get(position).getNickname());

        holder.sex.setText(list.get(position).getSex() == "male" ? "Жінка" : "Чоловік");

        holder.location.setText(list.get(position).getCity());

        if (list.get(position).getPhoto() != null) {
            Glide.with(holder.itemView.getContext()).load(list.get(position).getPhoto()).into(holder.photo);
        }

        holder.itemView.setOnClickListener(v -> {
            clubsListener.onInfo(list.get(position));
        });

        holder.no.setOnClickListener(v -> {
            clubsListener.reject(list.get(position).getId());
        });

        holder.ok.setOnClickListener(v -> {
            clubsListener.approve(list.get(position).getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView photo, no, ok;
        TextView name, login, sex, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            login = itemView.findViewById(R.id.text_login);
            sex = itemView.findViewById(R.id.sex);
            location = itemView.findViewById(R.id.text_location);
            no = itemView.findViewById(R.id.no);
            ok = itemView.findViewById(R.id.ok);
        }
    }

    public interface UsersListener{
        void onInfo(User itemClub);
        void approve(String id);
        void reject(String id);
    }

    public UsersAdaper setOnClubsListener(UsersListener clubsListener){
        this.clubsListener = clubsListener;
        return this;
    }

}
