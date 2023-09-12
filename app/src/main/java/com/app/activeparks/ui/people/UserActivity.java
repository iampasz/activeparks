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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class UserActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private PeopleViewModel viewModel;
    private ImageView photo, closed;

    private TextView listStatus;
    private RecyclerView profileList;
    private ProgressBar profileFilling;
    private ButtonSelect clubs, event, plan;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_people);
        viewModel = new ViewModelProvider(this, new PeopleModelFactory(this)).get(PeopleViewModel.class);


        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        photo = findViewById(R.id.photo);

        TextView name = findViewById(R.id.name);
        TextView role = findViewById(R.id.role);
        TextView sex = findViewById(R.id.sex);
        TextView birthday = findViewById(R.id.birthday);
        TextView address = findViewById(R.id.address);
        TextView about = findViewById(R.id.about);
        TextView healt = findViewById(R.id.healt);
        TextView height = findViewById(R.id.height);
        TextView weight = findViewById(R.id.weight);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);

        listStatus = findViewById(R.id.list_status);

        profileList = findViewById(R.id.profile_list);

        profileFilling = findViewById(R.id.profileFilling);

        closed = findViewById(R.id.closed);

        clubs = findViewById(R.id.clubs_action);
        clubs.on();
        event = findViewById(R.id.event_action);
        plan = findViewById(R.id.plan_action);

        viewModel.getUser().observe(this, user -> {
            try {
                swipeRefreshLayout.setRefreshing(false);

                if (user.getFirstName().length() + user.getLastName().length() > 1) {
                    name.setVisibility(View.VISIBLE);
                    name.setText(user.getFirstName() + " " + user.getLastName());
                } else {
                    name.setText(user.getNickname());
                }

                Glide.with(this).load(user.getPhoto()).error(R.drawable.ic_prew).into(photo);


                role.setText(viewModel.isRole());

                if (user.getSex() != null) {
                    findViewById(R.id.layout_sex).setVisibility(View.VISIBLE);
                    sex.setText(user.getSex().equals("male") ? "Чоловік" : user.getSex().equals("female") ? "Жінка" : "Невідомо");
                }

                if (user.getAge() != null){
                    if (user.getAge() < 45){
                        birthday.setText("Молод" + (user.getSex() == null ? "(ий/a)" : user.getSex().equals("male") ? "ий" : "а"));
                    }else if (user.getAge()  < 59){
                        birthday.setText("Зріл" + (user.getSex() == null ? "(ий/a)" : user.getSex().equals("male") ? "ий" : "а"));
                    }else if (user.getAge() >= 60){
                        birthday.setText("Літн" + (user.getSex() == null ? "(ій/я)" : user.getSex().equals("male")  ? "ій" : "я"));
                    }
                }

                if (user.getCity().length() > 1) {
                    findViewById(R.id.layout_location).setVisibility(View.VISIBLE);
                    address.setText(user.getCity());
                }

                if (user.getAboutMe().length() > 1) {
                    findViewById(R.id.title_about).setVisibility(View.VISIBLE);
                    about.setVisibility(View.VISIBLE);
                    about.setText(user.getAboutMe());
                }

                if (user.getHeight() != null && !user.getHeight().isEmpty()) {
                    findViewById(R.id.item_height).setVisibility(View.VISIBLE);
                    height.setVisibility(View.VISIBLE);
                    height.setText(user.getHeight() + " cм");
                }

                if (user.getWeight() != null && !user.getWeight().isEmpty()) {
                    findViewById(R.id.item_weight).setVisibility(View.VISIBLE);
                    weight.setVisibility(View.VISIBLE);
                    weight.setText(user.getWeight() + " кг");
                }


                if (user.getPhone().length() > 1) {
                    findViewById(R.id.phone_item).setVisibility(View.VISIBLE);
                    phone.setText(user.getPhone());
                }

                email.setText(user.getEmail());
                profileFilling.setProgress(user.getProfileFilling());

                if (user.getPermissionUser() == true) {
                    plan.setVisibility(View.VISIBLE);
                }
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

        event.setOnClickListener(v -> {
            viewModel.event();
            replaceButton();
            event.on();
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
                        Uri.parse("https://ap.sportforall.gov.ua")));
            }
        };
        observeData();

        viewModel.user(getIntent().getStringExtra("id"));
        viewModel.clubs(getIntent().getStringExtra("id"));

        swipeRefreshLayout.setRefreshing(true);
    }


    private void observeData() {
        viewModel.getClubs().observe(this, clubs -> {
            if (clubs.size() > 0) {
                listStatus.setVisibility(View.GONE);
            } else {
                listStatus.setText("Користувач не вступив \n у жоден клуб");
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
                listStatus.setVisibility(View.GONE);
            }else{
                listStatus.setText("Користувач не вступив у жоден захід");
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
                listStatus.setVisibility(View.GONE);
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

        viewModel.getJournal().observe(this, journal -> {
            if (journal.size() > 0) {
                listStatus.setVisibility(View.GONE);
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
                listStatus.setVisibility(View.GONE);
            }
            profileList.setAdapter(new PlanListAdaper(this, plan).setListener(new PlanListAdaper.PlanListener() {
                @Override
                public void onInfo(String id) {
                    TrainingDialog dialog = TrainingDialog.newInstance(id, viewModel.mProfile.getId());
                    dialog.show(getSupportFragmentManager(),
                            "training_dialog");
                }
            }));
        });
    }

    void replaceButton() {
        clubs.off();
        event.off();
        plan.off();
        profileList.removeAllViewsInLayout();
        listStatus.setVisibility(View.VISIBLE);
    }


    @Override
    public void onRefresh() {
        viewModel.user(getIntent().getStringExtra("id"));
    }
}