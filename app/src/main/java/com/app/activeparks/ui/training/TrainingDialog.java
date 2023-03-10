package com.app.activeparks.ui.training;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.workout.adapter.list.ListAdapter;
import com.app.activeparks.util.InputFilterMinMax;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.technodreams.activeparks.R;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

public class TrainingDialog extends BottomSheetDialogFragment {


    public static TrainingDialog newInstance(String id) {
        TrainingDialog fragment = new TrainingDialog();

        Bundle bundle = new Bundle();
        bundle.putString("ID", id);
        fragment.setArguments(bundle);

        return fragment;
    }

    public TrainingViewModel mViewModel;
    private ImageView sendNotesAction, closed;
    private TextView header, titleWeek;
    private TabLayout tabLayout;
    private Button createAction, cancelAction, removeAction;
    private EditText title, hourceStart, minuteStart, hourceFinish, minuteFinish, notes;
    private Switch isOnce;
    private RecyclerView list;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        mViewModel =
                new ViewModelProvider(this, new TrainingModelFactory(getContext())).get(TrainingViewModel.class);

        String id = getArguments().getString("ID");
        mViewModel.workout(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_plan_add, container,
                false);

        list = view.findViewById(R.id.list_notes);

        header = view.findViewById(R.id.header);

        titleWeek = view.findViewById(R.id.title_week);

        title = view.findViewById(R.id.title);

        isOnce = view.findViewById(R.id.isOnce);

        tabLayout = view.findViewById(R.id.select_week);

        hourceStart = view.findViewById(R.id.hource_start);
        minuteStart = view.findViewById(R.id.minutes_start);

        hourceFinish = view.findViewById(R.id.hource_finish);
        minuteFinish = view.findViewById(R.id.minutes_finish);

        notes = view.findViewById(R.id.message);

        closed = view.findViewById(R.id.closed);
        sendNotesAction = view.findViewById(R.id.send_notes);

        createAction = view.findViewById(R.id.create_action);
        cancelAction = view.findViewById(R.id.cancel_action);
        removeAction = view.findViewById(R.id.remove_action);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isLetterOrDigit(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        hourceStart.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "23")});
        minuteStart.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "59")});
//        hourceFinish.addTextChangedListener(this);
//        minuteFinish.addTextChangedListener(this);

        mViewModel.getWorkout().observe(getViewLifecycleOwner(), v -> {
            header.setText("Редагувати тренування");
            title.setText(v.getTitle());
            isOnce.setSelected(v.getIsOnce());

            if (v.getStartTime().length() > 4) {
                hourceStart.setText(v.getStartTime().substring(0, 2));
                minuteStart.setText(v.getStartTime().substring(3, 5));
            }
            if (v.getFinishTime().length() > 4) {
                hourceFinish.setText(v.getFinishTime().substring(0, 2));
                minuteFinish.setText(v.getStartTime().substring(3, 5));
            }

            removeAction.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            titleWeek.setVisibility(View.GONE);

            ListAdapter adapter = new ListAdapter(getActivity(), v.getExercises());
            list.setAdapter(adapter);

            createAction.setText("Зберегти дані");
            createAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.workoutUpdate(title.getText().toString(), isOnce.isSelected(),
                            String.valueOf(tabLayout.getSelectedTabPosition()), hourceStart + ":" + minuteStart,
                            hourceFinish + ":" + minuteFinish);
                }
            });
        });

        ListAdapter adapter = new ListAdapter(getActivity(), mViewModel.getExercises());
        list.setAdapter(adapter);

        sendNotesAction.setOnClickListener(v -> {
            mViewModel.addExercises(notes.getText().toString());
            adapter.update();
            notes.setText("");
        });

        createAction.setOnClickListener(v -> {
            mViewModel.workoutAdd(title.getText().toString(),
                    isOnce.isSelected(),
                    String.valueOf(tabLayout.getSelectedTabPosition()),
                    hourceStart + ":" + minuteStart,
                    hourceFinish + ":" + minuteFinish);
        });

        closed.setOnClickListener(v -> {
            dismiss();
        });


        cancelAction.setOnClickListener(v -> {
            dismiss();
        });

        removeAction.setOnClickListener(v -> {
            mViewModel.delete();
        });

        return view;
    }


}