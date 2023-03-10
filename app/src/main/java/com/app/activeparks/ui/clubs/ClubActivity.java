package com.app.activeparks.ui.clubs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.ui.event.EventsListFragment;
import com.app.activeparks.ui.qr.QrCodeActivity;
import com.app.activeparks.ui.news.NewsFragment;
import com.app.activeparks.ui.participants.ParticipantsFragment;
import com.app.activeparks.util.ButtonSelect;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;

public class ClubActivity extends AppCompatActivity {


    private ClubsViewModel mViewModel;

    private ImageView mImageView, mHideDescription;

    private TextView mTitle, mUser, mDescription, mCreateAt, mPhone;
    private ButtonSelect news, event, people, qr, mApproved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_club);
        mViewModel =
                new ViewModelProvider(this, new ClubsModelFactory(this)).get(ClubsViewModel.class);

        mViewModel.getClubsDetail(getIntent().getStringExtra("id"));

        findViewById(R.id.closed).setOnClickListener((View v) -> {
            finish();
        });

        mImageView = findViewById(R.id.image_club);
        mHideDescription = findViewById(R.id.ic_hide_description);

        mTitle = findViewById(R.id.title);
        mUser = findViewById(R.id.text_users);
        mDescription = findViewById(R.id.text_description);
        mCreateAt = findViewById(R.id.text_data_create);
        mPhone = findViewById(R.id.text_phone);


        news = findViewById(R.id.news_action);
        event = findViewById(R.id.event_action);
        people = findViewById(R.id.people_action);
        qr = findViewById(R.id.qr_action);
        mApproved = findViewById(R.id.club_in_action);

        mViewModel.getEventDetails().observe(this, clubs -> {
            try {
                Glide.with(this).load(clubs.getLogoUrl()).into(mImageView);
                mTitle.setText(clubs.getName());
                mDescription.setText(clubs.getDescription());
                mCreateAt.setText(clubs.getCreatedAt().replace("-", "."));
                mUser.setText(""+clubs.getMemberAmount());
                mPhone.setText(clubs.getPhone());

                if (clubs.getClubUser() != null){
                    if(clubs.getClubUser().getIsApproved() == true){
                        mApproved.setText("Вийти");
                    }else{
                        mApproved.setText("Відмінити");
                    }
                }else {
                    mApproved.setText("Приєднатись");
                }

                if (clubs.getTelegramUrl() != null){
                    setView(findViewById(R.id.action_telegram), clubs.getTelegramUrl());
                }

                if (clubs.getYoutubeUrl() != null){
                    setView(findViewById(R.id.action_youtube), clubs.getYoutubeUrl());
                }

                if (clubs.getFacebookUrl() != null){
                    setView(findViewById(R.id.action_facebook), clubs.getFacebookUrl());
                }

                if (clubs.getInstagramUrl() != null){
                    setView(findViewById(R.id.action_instagram), clubs.getInstagramUrl());
                }

                mApproved.setEnabled(true);

            }catch (Exception e){}
        });

        setFragment(new NewsFragment(mViewModel.mId));

        news.setOnClickListener(v ->{
            setFragment(new NewsFragment(mViewModel.mId));
            replaceButton();
            news.on();
        });

        event.setOnClickListener(v ->{
            setFragment(new EventsListFragment(mViewModel.mId));
            replaceButton();
            event.on();
        });

        people.setOnClickListener(v ->{
            setFragment(new ParticipantsFragment(mViewModel.mId, mViewModel.admin, false));
            replaceButton();
            people.on();
        });

        mHideDescription.setOnClickListener(v ->{
            if (mDescription.getVisibility() == View.VISIBLE){
                mHideDescription.setImageResource(R.drawable.ic_down_button);
                mDescription.setVisibility(View.GONE);
            }else {
                mHideDescription.setImageResource(R.drawable.ic_top_button);
                mDescription.setVisibility(View.VISIBLE);
            }
        });

        if (mViewModel.admin == true){
            qr.setVisibility(View.VISIBLE);
            qr.setVisibility(View.GONE);
            qr.setOnClickListener(v ->{
                startActivity(new Intent(this, QrCodeActivity.class)
                        .putExtra("club", true)
                        .putExtra("clubId", mViewModel.mId));
            });
        }else {
            qr.setVisibility(View.GONE);
            mApproved.setVisibility(View.VISIBLE);
            mApproved.setOnClickListener(v ->{
                mViewModel.applyUser();
                mApproved.setEnabled(false);
            });
        }

    }

    void setFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.club_fragment, fragment)
                .commit();
    }

    void setView(View view, String url){
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(v ->{
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url)));
        });
    }

    void replaceButton(){
        news.off();
        event.off();
        people.off();
    }

}