package com.app.activeparks.ui.workout.adapter.plan;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.workout.adapter.list.TrainingAdapter;
import com.google.android.material.tabs.TabLayout;
import com.technodreams.activeparks.R;

import java.util.ArrayList;
import java.util.List;

public class PlanListAdaper extends RecyclerView.Adapter {

    private final List<WorkoutItem> list;
    private  List<WorkoutItem> listFilter;
    private final LayoutInflater inflater;
    private String type;
    private PlanListener planListener;

    public PlanListAdaper(Context context, List<WorkoutItem> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.listFilter = new ArrayList<>();
        this.weakFilter(0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new PlanHeaderViewHolder(inflater.inflate(R.layout.item_plan_select, parent, false));
        }else {
            return new PlanViewHolder(inflater.inflate(R.layout.item_plan, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
                ((PlanHeaderViewHolder) holder).tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        weakFilter(tab.parent.getSelectedTabPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                    }
                });
            ((PlanHeaderViewHolder) holder).create.setOnClickListener(v ->{
                planListener.onInfo(null);
            });
        }else {
            WorkoutItem item = listFilter.get(position -1);
            if (item.getTitle() != null) {
                ((PlanViewHolder) holder).title.setText(item.getTitle());
            }
            if (item.getDescription() != null) {
                ((PlanViewHolder) holder).description.setText(item.getDescription());
            }
            if (item.getStartTime() != null) {
                ((PlanViewHolder) holder).data.setText(item.getStartTime().substring(0, 5) + " - " + item.getFinishTime().substring(0, 5));
            }
            if (item.getExercises() != null) {
                ((PlanViewHolder) holder).list.setAdapter(new TrainingAdapter(inflater.getContext(), item.getExercises(), false));
            }

            ((PlanViewHolder) holder).edit.setOnClickListener(v ->{
                planListener.onInfo(item.getId());
            });
        }
    }

    public void weakFilter(int weak){
        listFilter.clear();
        if (list != null) {
            for (WorkoutItem item : list) {
                if (item.getWeekDay() == weak) {
                    listFilter.add(item);
                }
            }
        this.notifyDataSetChanged();

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listFilter.size() + 1;
    }

    public static class PlanHeaderViewHolder extends RecyclerView.ViewHolder{

        final Button create;
        private TabLayout tabLayout;

        public PlanHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            create = itemView.findViewById(R.id.create_workout_action);
            tabLayout = itemView.findViewById(R.id.select_week);
        }
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder{

        final TextView title, data, description, edit;
        private RecyclerView list;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.data);
            title = itemView.findViewById(R.id.title);
            edit = itemView.findViewById(R.id.edit);
            description = itemView.findViewById(R.id.description);
            list = itemView.findViewById(R.id.list);
        }
    }

    public interface PlanListener{
        void onInfo(String id);
    }

    public PlanListAdaper setListener(PlanListener planListener){
        this.planListener = planListener;
        return this;
    }

}
