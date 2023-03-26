package com.app.activeparks.ui.people;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.model.workout.WorkoutItem;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.ui.event.EventActivity;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.profile.uservideo.UserAddVideoActivity;
import com.app.activeparks.ui.profile.uservideo.adapter.UserVideoAdapter;
import com.app.activeparks.ui.training.TrainingDialog;
import com.app.activeparks.ui.workout.adapter.journal.JournalListAdaper;
import com.app.activeparks.ui.workout.adapter.plan.PlanListAdaper;
import com.app.activeparks.util.ButtonSelect;
import com.app.activeparks.util.FragmentInteface;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserActivity extends AppCompatActivity {

    private PeopleViewModel viewModel;

    private ImageView photo, closed;

    private RecyclerView profileList;
    private ProgressBar profileFilling;
    private ButtonSelect clubs, result, journal, plan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_people);
        viewModel = new ViewModelProvider(this, new PeopleModelFactory(this)).get(PeopleViewModel.class);


        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        photo = findViewById(R.id.photo);

        TextView name = findViewById(R.id.name);
        TextView login = findViewById(R.id.text_login);
        TextView sex = findViewById(R.id.sex);
        TextView birthday = findViewById(R.id.birthday);
        TextView time = findViewById(R.id.time);
        TextView adress = findViewById(R.id.adress);
        TextView create = findViewById(R.id.create);
        TextView about = findViewById(R.id.about);
        TextView height = findViewById(R.id.height);
        TextView weight = findViewById(R.id.weight);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);

        profileList = findViewById(R.id.profile_list);

        profileFilling = findViewById(R.id.profileFilling);

        closed = findViewById(R.id.closed);

        clubs = findViewById(R.id.clubs_action);
        result = findViewById(R.id.result_action);
        journal = findViewById(R.id.journal_action);
        plan = findViewById(R.id.plan_action);

        viewModel.getUser().observe(this, user -> {
            try {
                name.setText(user.getFirstName() + " " + user.getLastName());
                login.setText(user.getNickname());
                if (user.getSex() != null) {
                    sex.setText(user.getSex().equals("male") ? "Чоловік" : user.getSex() == "female" ? "Жінка" : "Невідомо");
                }

                if (user.getAge() != 0){
                    if (user.getAge() > 50){
                        birthday.setText(user.getSex() == "male" ? "Зрілий" : user.getSex() == "female" ? "Зріла" : "Невідомо");
                    }else{
                        birthday.setText(user.getSex() == "male" ? "Молодий" : user.getSex() == "female" ? "Молода" : "Невідомо");
                    }
                }

                adress.setText(user.getCity());
                time.setText(user.getUpdatedAt().replace("-", "."));
                create.setText(user.getCreatedAt().replace("-", "."));
                about.setText(user.getAboutMe());

                if (user.getHeight() != null){
                    findViewById(R.id.icon_weight).setVisibility(View.VISIBLE);
                    findViewById(R.id.title_weight).setVisibility(View.VISIBLE);
                    height.setVisibility(View.VISIBLE);
                    height.setText(user.getHeight() + " cм");
                }

                if (user.getWeight() != null){
                    findViewById(R.id.icon_height).setVisibility(View.VISIBLE);
                    findViewById(R.id.title_height).setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    weight.setText(user.getWeight() + " кг");
                }

                phone.setText(user.getPhone());
                email.setText(user.getEmail());
                profileFilling.setProgress(user.getProfileFilling());
                Glide.with(this).load(user.getPhoto()).into(photo);
            } catch (Exception e) {
            }
        });



        closed.setOnClickListener(v -> {
            finish();
        });

        clubs.setOnClickListener(v -> {
            viewModel.clubs();
            replaceButton();
            clubs.on();
        });

        result.setOnClickListener(v -> {
            viewModel.result();
            replaceButton();
            result.on();
        });

        journal.setOnClickListener(v -> {
            viewModel.journal();
            replaceButton();
            journal.on();
        });

        plan.setOnClickListener(v -> {
            viewModel.plan();
            replaceButton();
            plan.on();
        });

        findViewById(R.id.phone).setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("", phone.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Скопійовано", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.email).setOnClickListener(v -> {
            ClipData clip = ClipData.newPlainText("", email.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Скопійовано", Toast.LENGTH_SHORT).show();
        });


        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://web.sportforall.gov.ua")));
            }
        };
        observeData();

        viewModel.user(getIntent().getStringExtra("id"));
        viewModel.clubs(getIntent().getStringExtra("id"));
    }



    private void observeData() {
        viewModel.getClubs().observe(this, clubs -> {
            if (clubs.size() > 0) {
                findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new ClubsAdaper(this, clubs).setOnClubsListener(new ClubsAdaper.ClubsListener() {
                @Override
                public void onInfo(ItemClub itemClub) {
                    startActivity(new Intent(getApplicationContext(), ClubActivity.class)
                            .putExtra("id", itemClub.getId()));
                }
            }));
        });

        viewModel.getEvents().observe(this, result -> {
            if (result.size() > 0) {
                findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new EventsListAdaper(this, result).setOnEventListener(new EventsListAdaper.EventsListener() {
                @Override
                public void onInfo(ItemEvent itemClub) {
                    startActivity(new Intent(getApplicationContext(), EventActivity.class).putExtra("id", itemClub.getId()));
                }

                @Override
                public void onOpenMaps(double lat, double lon) {

                }
            }));
        });

        viewModel.getResult().observe(this, result -> {
            if (result.getItems().size() > 0) {
                findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new EventsListAdaper(this, result.getItems()).setOnEventListener(new EventsListAdaper.EventsListener() {
                @Override
                public void onInfo(ItemEvent itemClub) {
                    startActivity(new Intent(getApplicationContext(), EventActivity.class).putExtra("id", itemClub.getId()));
                }

                @Override
                public void onOpenMaps(double lat, double lon) {

                }
            }));
        });

        viewModel.getUserVideo().observe(this, video -> {
            if (video.getItems().size() > 0) {
                findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new UserVideoAdapter(this, video.getItems()).setOnUserVideoListener(new UserVideoAdapter.UserVideoListener() {
                @Override
                public void onInfo(UserVideoItem itemClub) {
                    startActivity(new Intent(getApplicationContext(), UserAddVideoActivity.class).putExtra("id", itemClub.getId()));
                }

                @Override
                public void onCreate() {
                    startActivity(new Intent(getApplicationContext(), UserAddVideoActivity.class));
                }
            }));
        });

        viewModel.getJournal().observe(this, journal -> {
            if (journal.size() > 0) {
                findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            JournalListAdaper adaper = new JournalListAdaper(this, journal);
            profileList.setAdapter(adaper.setListener(new JournalListAdaper.JournalListener() {
                @Override
                public void onInfo(WorkoutItem workoutItem) {

                }

                @Override
                public void getNotes(String id, int position) {
                    viewModel.notes(id);
//                    viewModel.getNotes().observe(this, notes -> {
//                        adaper.updateWorkout(notes, position);
//                    });
                }

                @Override
                public void sendMessage(String id, String idNotes, String msg, boolean edit) {
                    viewModel.sendNotes(id, msg);
                }
            }));
        });

        viewModel.getPlan().observe(this, plan -> {
            if (plan.size() > 0) {
                findViewById(R.id.list_status).setVisibility(View.GONE);
            }
            profileList.setAdapter(new PlanListAdaper(this, plan).setListener(new PlanListAdaper.PlanListener() {
                @Override
                public void onInfo(String id) {
                    TrainingDialog dialog = TrainingDialog.newInstance(id);
                    dialog.show(getSupportFragmentManager(),
                            "training_dialog");
                }
            }));
        });
    }

    void replaceButton() {
        clubs.off();
        result.off();
        journal.off();
        plan.off();
        profileList.removeAllViewsInLayout();
        findViewById(R.id.list_status).setVisibility(View.VISIBLE);
    }


}