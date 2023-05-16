package com.app.activeparks.ui.workout.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.ui.participants.adapter.UsersAdaper;
import com.technodreams.activeparks.R;

import java.util.List;

public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    private final List<String> list;
    private final LayoutInflater inflater;
    private Boolean edit = false;

    public TrainingAdapter(Context context, List<String> list, Boolean edit){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.edit = edit;
    }

    public void update() {
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TrainingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_exercises, parent, false);
        return new TrainingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingAdapter.ViewHolder holder, int position) {
        holder.text.setText(list.get(position));

        holder.remove.setVisibility(edit == true ? View.VISIBLE : View.GONE);

        holder.remove.setOnClickListener(v ->{
            listener.onRemove(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView text;
        final ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.name);
            remove = itemView.findViewById(R.id.remove_action);
        }
    }

    public TrainingAdapterListener listener;

    public interface TrainingAdapterListener{
        void onRemove(int  id);
    }

    public TrainingAdapter setOnListener(TrainingAdapterListener listener){
        this.listener = listener;
        return this;
    }

}
