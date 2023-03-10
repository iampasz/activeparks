package com.app.activeparks.ui.workout.adapter.journal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.workout.adapter.list.ListAdapter;
import com.technodreams.activeparks.R;

import java.util.List;

public class JournalListAdaper extends RecyclerView.Adapter<JournalListAdaper.ViewHolder> {

    private  List<WorkoutItem> list;
    private final LayoutInflater inflater;
    private JournalListener journalListener;

    public JournalListAdaper(Context context, List<WorkoutItem> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void updateWorkout(WorkoutItem list, int positipn) {
        this.list.set(positipn, list);
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_journal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (list.get(position).getTitle() != null) {
            holder.title.setText(list.get(position).getTitle());
        }
        if (list.get(position).getDescription() != null) {
            holder.description.setText(list.get(position).getDescription());
        }
        if (list.get(position).getStartTime() != null) {
            holder.data.setText(list.get(position).getStartTime().substring(0, 5) + " - " + list.get(position).getFinishTime().substring(0, 5));
        }
        if (list.get(position).getExercises() != null) {
            holder.list.setAdapter(new ListAdapter(inflater.getContext(), list.get(position).getExercises()));
        }

        holder.commentShow.setOnClickListener(v -> {
            if (holder.messagePanel.getVisibility() == View.VISIBLE){
                holder.messagePanel.setVisibility(View.GONE);
                holder.messageListPanel.setVisibility(View.GONE);
                holder.commentShow.setText("Розгорнути коментарі");
            }else{
                holder.messagePanel.setVisibility(View.VISIBLE);
                holder.messageListPanel.setVisibility(View.VISIBLE);
                holder.commentShow.setText("Схвовати коментарі");
                journalListener.getNotes(list.get(position).getId(), position);
            }
        });

        holder.sendMessage.setOnClickListener(v -> {
            if (holder.edit == true) {
                holder.edit = false;
                journalListener.sendMessage(list.get(position).getId(), list.get(position).getNotes().get(holder.cout).getId(), holder.message.getText().toString(), true);
            }else {
                journalListener.sendMessage(list.get(position).getId(), list.get(position).getNotes().get(holder.cout).getId(), holder.message.getText().toString(), false);
            }
            holder.message.setText("");
        });


        if (list.get(position).getNotes() != null && list.get(position).getNotes().size() > 0) {
            holder.coutMssage.setText(holder.cout + " / " + list.get(position).getNotes().size());

            holder.editMessage.setOnClickListener(v -> {
                if (holder.edit == true){
                    holder.edit = false;
                    holder.message.setText("");
                    holder.editMessage.setText("Редагувати");
                }else {
                    holder.edit = true;
                    holder.message.setText(list.get(position).getNotes().get(holder.cout).getTitle());
                    holder.editMessage.setText("Відмінити");
                }
            });
            holder.coutMssage.setText((holder.cout + 1) + " / " + list.get(position).getNotes().size());

            holder.leftAction.setOnClickListener(v -> {
                if (holder.cout > 0) {
                    holder.cout--;
                    holder.dataMessage.setText(list.get(position).getNotes().get(holder.cout).getCreatedAt());
                    holder.textMssage.setText(list.get(position).getNotes().get(holder.cout).getTitle());
                    holder.coutMssage.setText((holder.cout + 1) + " / " + list.get(position).getNotes().size());
                }
            });

            holder.rightAction.setOnClickListener(v -> {
                if (holder.cout < (list.get(position).getNotes().size()) - 1) {
                    holder.cout++;
                    holder.dataMessage.setText(list.get(position).getNotes().get(holder.cout).getCreatedAt());
                    holder.textMssage.setText(list.get(position).getNotes().get(holder.cout).getTitle());
                    holder.coutMssage.setText((holder.cout + 1) + " / " + list.get(position).getNotes().size());
                }
            });

            holder.dataMessage.setText(list.get(position).getNotes().get(holder.cout).getCreatedAt());
            holder.textMssage.setText(list.get(position).getNotes().get(holder.cout).getTitle());

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        int cout = 0;
        boolean edit = false;
        final LinearLayout messagePanel, messageListPanel;
        final ImageView leftAction, rightAction, sendMessage;
        final TextView title, description, data, commentShow, dataMessage, textMssage, coutMssage, editMessage;

        final EditText message;
        final RecyclerView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messagePanel = itemView.findViewById(R.id.message_panel);
            messageListPanel = itemView.findViewById(R.id.comment_list_panel);

            data = itemView.findViewById(R.id.data);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            list = itemView.findViewById(R.id.list);

            message = itemView.findViewById(R.id.message);


            coutMssage = itemView.findViewById(R.id.cout_message);
            dataMessage = itemView.findViewById(R.id.data_message);
            textMssage = itemView.findViewById(R.id.text_message);

            commentShow = itemView.findViewById(R.id.coments_action);

            leftAction = itemView.findViewById(R.id.left_action);
            rightAction = itemView.findViewById(R.id.right_action);
            sendMessage = itemView.findViewById(R.id.send_message);
            editMessage = itemView.findViewById(R.id.edit_action);
        }
    }

    public interface JournalListener {
        void onInfo(WorkoutItem workoutItem);

        void getNotes(String id, int position);
        void sendMessage(String id, String idNotes, String msg, boolean edit);
    }

    public JournalListAdaper setListener(JournalListener journalListener) {
        this.journalListener = journalListener;
        return this;
    }

}
