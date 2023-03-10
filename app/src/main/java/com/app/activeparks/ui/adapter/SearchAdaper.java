package com.app.activeparks.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.city.Body;
import com.app.activeparks.data.model.city.City;
import com.app.activeparks.data.model.news.ItemNews;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.home.adapter.HorizontalAdaper;
import com.technodreams.activeparks.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdaper extends RecyclerView.Adapter<SearchAdaper.ViewHolder> {

    private  List<Body> list;
    private final LayoutInflater inflater;
    public SearchAdaperListener searchAdaperListener;

    public SearchAdaper(Context context, List<Body> list){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void updateSearch(List<Body> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(list.get(position).getCity());
        holder.description.setText(list.get(position).getSearch());
        holder.itemView.setOnClickListener(view -> {
            searchAdaperListener.onInfo(list.get(position));
        });
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

    public interface SearchAdaperListener{
        void onInfo(Body item);
    }

    public SearchAdaper setOnCliclListener(SearchAdaper.SearchAdaperListener searchAdaperListener){
        this.searchAdaperListener = searchAdaperListener;
        return this;
    }

}
