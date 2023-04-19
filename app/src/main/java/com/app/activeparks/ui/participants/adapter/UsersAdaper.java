package com.app.activeparks.ui.participants.adapter;


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
import com.app.activeparks.data.model.user.User;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.util.List;

public class UsersAdaper extends RecyclerView.Adapter<UsersAdaper.ViewHolder> {

    private final List<User> list;
    private final LayoutInflater inflater;

    private int applying = 0;

    public UsersAdaper(Context context, List<User> list, int applying ){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.applying = applying;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);

        if ((user.getFirstName() +  user.getLastName()).length() > 1) {
            holder.name.setText(user.getFirstName() + " " + user.getLastName());
        }else {
            holder.name.setText(user.getNickname());
        }

        holder.login.setText(user.getEmail());

        if (user.getSex() != null) {
            holder.sexLayout.setVisibility(View.VISIBLE);
            holder.sex.setText(user.getSex().contains("female") ? "Жінка" : "Чоловік");
        }

        if (user.getPhone() != null && user.getPhone().length() > 0) {
            holder.phoneLayout.setVisibility(View.VISIBLE);
            holder.phone.setText(user.getPhone());
        }

        holder.location.setText(user.getCity());

        if (user.getPhoto() != null) {
            Glide.with(holder.itemView.getContext()).load(user.getPhoto()).error(R.drawable.ic_prew).into(holder.photo);
        }

        holder.itemView.setOnClickListener(v -> {
            clubsListener.onInfo(list.get(position).getId());
        });

        if (applying == 0) {
            holder.applyingLayout.setVisibility(View.GONE);
            holder.remove.setVisibility(View.GONE);
        }else if (applying == 1) {
            holder.applyingLayout.setVisibility(View.GONE);
            holder.remove.setVisibility(View.VISIBLE);
        }else if (applying == 2) {
            holder.applyingLayout.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.GONE);
        }

        holder.remove.setOnClickListener(v -> {
            clubsListener.reject(list.get(position).getId());
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

        LinearLayout  sexLayout, phoneLayout, applyingLayout;
        ImageView photo, no, ok, remove;
        TextView name, login, sex, phone, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            login = itemView.findViewById(R.id.text_login);
            sex = itemView.findViewById(R.id.sex);
            phone = itemView.findViewById(R.id.phone);

            applyingLayout = itemView.findViewById(R.id.applying_layout);
            sexLayout = itemView.findViewById(R.id.sex_layout);
            phoneLayout = itemView.findViewById(R.id.phone_layout);

            location = itemView.findViewById(R.id.text_location);
            remove = itemView.findViewById(R.id.remove_action);
            no = itemView.findViewById(R.id.no);
            ok = itemView.findViewById(R.id.ok);
        }
    }


    public UsersListener clubsListener;

    public interface UsersListener{
        void onInfo(String  id);
        void approve(String id);
        void reject(String id);
    }

    public UsersAdaper setOnClubsListener(UsersListener clubsListener){
        this.clubsListener = clubsListener;
        return this;
    }

}
