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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        WorkoutItem item =  list.get(position);

        if (item.getTitle() != null) {
            holder.title.setText(item.getTitle());
        }

        if (item.getDescription() != null) {
            holder.description.setText(item.getDescription());
        }

        if (item.getStartsAt() != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(item.getStartsAt());
                holder.data.setText(new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (item.getStartsAt() != null) {
            holder.start.setText(item.getStartsAt().substring(11, item.getStartsAt().length() - 3));
        }

        if (item.getFinishesAt() != null) {
            holder.finish.setText(item.getFinishesAt().substring(11, item.getFinishesAt().length() - 3));
        }

        if (item.getExercises() != null) {
            holder.list.setAdapter(new ListAdapter(inflater.getContext(), item.getExercises()));
        }

        if (item.getActivityType().contains("sport_event")){
            holder.commentShow.setText("Відкрити захід");
            holder.commentShow.setOnClickListener(v -> {
                journalListener.onInfo(item);
            });
        }else {
            holder.commentShow.setText("Розгорнути коментарі");
            holder.commentShow.setOnClickListener(v -> {
                if (holder.messagePanel.getVisibility() == View.VISIBLE){
                    holder.messagePanel.setVisibility(View.GONE);
                    holder.messageListPanel.setVisibility(View.GONE);
                    holder.commentShow.setText("Розгорнути коментарі");
                }else{
                    holder.messagePanel.setVisibility(View.VISIBLE);
                    holder.messageListPanel.setVisibility(View.VISIBLE);
                    holder.commentShow.setText("Схвовати коментарі");
                    journalListener.getNotes(item.getId(), position);
                }
            });
        }





        holder.sendMessage.setOnClickListener(v -> {
                if (holder.edit == true) {
                    holder.edit = false;
                    journalListener.sendMessage(item.getId(), item.getNotes().get(holder.cout).getId(), holder.message.getText().toString(), true);
                } else {
                    journalListener.sendMessage(item.getId(), null, holder.message.getText().toString(), false);
                }
            holder.message.setText("");
        });


        if (item.getNotes() != null && item.getNotes().size() > 0) {
            holder.coutMssage.setText(holder.cout + " / " + item.getNotes().size());

            holder.editMessage.setOnClickListener(v -> {
                if (holder.edit == true){
                    holder.edit = false;
                    holder.message.setText("");
                    holder.editMessage.setText("Редагувати");
                }else {
                    holder.edit = true;
                    holder.message.setText(item.getNotes().get(holder.cout).getTitle());
                    holder.editMessage.setText("Відмінити");
                }
            });
            holder.coutMssage.setText((holder.cout + 1) + " / " + item.getNotes().size());

            holder.leftAction.setOnClickListener(v -> {
                if (holder.cout > 0) {
                    holder.cout--;
                    holder.dataMessage.setText(item.getNotes().get(holder.cout).getCreatedAt());
                    holder.textMssage.setText(item.getNotes().get(holder.cout).getTitle());
                    holder.coutMssage.setText((holder.cout + 1) + " / " + item.getNotes().size());
                }
            });

            holder.rightAction.setOnClickListener(v -> {
                if (holder.cout < (item.getNotes().size()) - 1) {
                    holder.cout++;
                    holder.dataMessage.setText(item.getNotes().get(holder.cout).getCreatedAt());
                    holder.textMssage.setText(item.getNotes().get(holder.cout).getTitle());
                    holder.coutMssage.setText((holder.cout + 1) + " / " + item.getNotes().size());
                }
            });

            holder.dataMessage.setText(item.getNotes().get(holder.cout).getCreatedAt());
            holder.textMssage.setText(item.getNotes().get(holder.cout).getTitle());

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
        final TextView title, description, data, start, finish, commentShow, dataMessage, textMssage, coutMssage, editMessage;

        final EditText message;
        final RecyclerView list;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messagePanel = itemView.findViewById(R.id.message_panel);
            messageListPanel = itemView.findViewById(R.id.comment_list_panel);

            data = itemView.findViewById(R.id.data);

            start = itemView.findViewById(R.id.start_text);
            finish = itemView.findViewById(R.id.finish_text);
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
