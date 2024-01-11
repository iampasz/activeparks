package com.app.activeparks.ui.clubs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.activeparks.ui.event.fragments.EventListFragment;
import com.app.activeparks.ui.news.NewsFragment;
import com.app.activeparks.ui.participants.ParticipantsFragment;
import com.app.activeparks.ui.qr.QrCodeActivity;
import com.app.activeparks.util.ButtonSelect;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClubActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private ClubsViewModel viewModel;

    private ImageView mImageView, mHideDescription;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch permissionUser;

    private TextView mTitle, subtitle, mUser, mDescription, mCreateAt, mPhone;
    private ButtonSelect news, event, people, qr, join;

    private SwipeRefreshLayout swipeRefreshLayout;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        overridePendingTransition(R.anim.start, R.anim.end);

        viewModel =
                new ViewModelProvider(this, new ClubsModelFactory(this)).get(ClubsViewModel.class);

        viewModel.getClubsDetail(getIntent().getStringExtra("id"));

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        findViewById(R.id.closed).setOnClickListener((View v) -> onBackPressed());

        mImageView = findViewById(R.id.image_club);
        mHideDescription = findViewById(R.id.ic_hide_description);

        mTitle = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        mUser = findViewById(R.id.text_users);
        mDescription = findViewById(R.id.text_description);
        mCreateAt = findViewById(R.id.text_data_create);
        mPhone = findViewById(R.id.text_phone);

        permissionUser = findViewById(R.id.switch_permission_user);


        news = findViewById(R.id.news_action);
        event = findViewById(R.id.event_action);
        people = findViewById(R.id.people_action);
        qr = findViewById(R.id.qr_action);
        join = findViewById(R.id.club_in_action);

        viewModel.getEventDetails().observe(this, clubs -> {
            try {
                Glide.with(this).load(clubs.getLogoUrl()).error(R.drawable.ic_prew).into(mImageView);
                mTitle.setText(clubs.getName());
                subtitle.setText(clubs.getTagline());
                mDescription.setText(clubs.getDescription());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = format.parse(clubs.getCreatedAt());
                    assert date != null;
                    mCreateAt.setText(new SimpleDateFormat("dd MMMM yyyy", new Locale("uk", "UA")).format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mUser.setText("" + clubs.getMemberAmount());
                mPhone.setText(clubs.getPhone());

                if (clubs.getTelegramUrl() != null) {
                    setView(findViewById(R.id.action_telegram), clubs.getTelegramUrl());
                }

                if (clubs.getYoutubeUrl() != null) {
                    setView(findViewById(R.id.action_youtube), clubs.getYoutubeUrl());
                }

                if (clubs.getFacebookUrl() != null) {
                    setView(findViewById(R.id.action_facebook), clubs.getFacebookUrl());
                }

                if (clubs.getInstagramUrl() != null) {
                    setView(findViewById(R.id.action_instagram), clubs.getInstagramUrl());
                }

                if (clubs.permissionUser() != null) {
                    permissionUser.setChecked(clubs.permissionUser());
                }

                if (clubs.getClubUser() != null) {
                    if (clubs.getClubUser().getIsCoordinator()) {
                        qr.setVisibility(View.VISIBLE);
                        qr.setOnClickListener(v -> startActivity(new Intent(this, QrCodeActivity.class)
                                .putExtra("club", true)
                                .putExtra("clubId", viewModel.mId)));
                        permissionUser.setVisibility(View.GONE);
                    }else{
                        join.setVisibility(View.VISIBLE);
                        join.setOnClickListener(v -> {
                            viewModel.applyUser();
                            join.setEnabled(false);
                        });
                        permissionUser.setVisibility(View.VISIBLE);
                    }

                    if (clubs.getClubUser().getIsApproved()) {
                        join.setText("Вийти");
                    } else {
                        join.setText("Відмінити");
                    }
                }else{
                    join.setVisibility(View.VISIBLE);
                    join.setText("Приєднатись");
                    join.setOnClickListener(v -> {
                        viewModel.applyUser();
                        join.setEnabled(false);
                    });
                }

                join.setEnabled(true);

            } catch (Exception ignored) {
            }
        });

        setFragment(new NewsFragment(viewModel.mId));

        news.setOnClickListener(v -> {
            setFragment(new NewsFragment(viewModel.mId));
            replaceButton();
            news.on();
        });

        event.setOnClickListener(v -> {
            setFragment(new EventListFragment());
            replaceButton();
            event.on();
        });

        people.setOnClickListener(v -> {
            setFragment(new ParticipantsFragment(viewModel.mId, viewModel.admin, false));
            replaceButton();
            people.on();
        });

        mHideDescription.setOnClickListener(v -> {
            if (mDescription.getVisibility() == View.VISIBLE) {
                mHideDescription.setImageResource(R.drawable.ic_down_button);
                mDescription.setVisibility(View.GONE);
            } else {
                mHideDescription.setImageResource(R.drawable.ic_top_button);
                mDescription.setVisibility(View.VISIBLE);
            }
        });

        permissionUser.setOnClickListener(v -> viewModel.setPermissionsRequest(permissionUser.isChecked()));

        findViewById(R.id.copy_action).setOnClickListener((View v) -> {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Хочу тебе запросити до клубу \"" + mTitle.getText().toString() + "\"");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Хочу тебе запросити до клубу \"" + mTitle.getText().toString() + "\"  \n\n" + "https://ap.sportforall.gov.ua/fc/" + viewModel.mId + " \n\nПриєднуйся до нас! Та оздоровлюйся разом з нами! \n\nРозроблено на завдання президента України для проекту “Активні парки” ");
            startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
        });
    }

    void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.club_fragment, fragment)
                .commit();
    }

    void setView(View view, String url) {
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(v -> {
            Uri src = Uri.parse(url);
            startActivity(new Intent(Intent.ACTION_VIEW, src));
        });
    }

    void replaceButton() {
        news.off();
        event.off();
        people.off();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        viewModel.getClubsDetail(getIntent().getStringExtra("id"));
    }
}