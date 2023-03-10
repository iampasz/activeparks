package com.app.activeparks.ui.workout.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.workout.adapter.plan.PlanListAdaper;
import com.technodreams.activeparks.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<String> list;
    private final LayoutInflater inflater;

    public ListAdapter(Context context, List<String> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void update() {
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_exercises, parent, false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        holder.text.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        final TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.name);
        }
    }


}
