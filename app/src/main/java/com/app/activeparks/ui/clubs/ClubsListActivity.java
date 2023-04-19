package com.app.activeparks.ui.clubs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.util.ButtonSelect;
import com.technodreams.activeparks.R;

public class ClubsListActivity extends AppCompatActivity {


    private ClubsViewModel mViewModel;
    private RecyclerView listClubOwner, listClubMember;
    private ButtonSelect mClubOwner, mClubMember;

    private EditText searchText;
    private ClubsAdaper adaper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs);
        overridePendingTransition(R.anim.start, R.anim.end);

        mViewModel =
                new ViewModelProvider(this, new ClubsModelFactory(this)).get(ClubsViewModel.class);

        listClubOwner = findViewById(R.id.list_club_owner);
        listClubMember = findViewById(R.id.list_club_member);

        mClubOwner = findViewById(R.id.club_owner_action);
        mClubMember = findViewById(R.id.club_member_action);

        searchText = findViewById(R.id.search_text);

        mViewModel.getClubsCreator().observe(this, clubs -> {
            if (clubs.size() > 0) {
                findViewById(R.id.list_null_two).setVisibility(View.GONE);
            }
            listClubOwner.setAdapter(new ClubsAdaper(this, clubs).setOnClubsListener(new ClubsAdaper.ClubsListener() {
                @Override
                public void onInfo(ItemClub itemClub) {
                    startActivity(new Intent(ClubsListActivity.this, ClubActivity.class).putExtra("id", itemClub.getId()));
                }
            }));
        });

        mViewModel.getClubsParticipant().observe(this, clubs -> {
            if (clubs.size() > 0) {
                findViewById(R.id.list_null_two).setVisibility(View.GONE);
            }
            listClubMember.setAdapter(new ClubsAdaper(this, clubs).setOnClubsListener(new ClubsAdaper.ClubsListener() {
                @Override
                public void onInfo(ItemClub itemClub) {
                    startActivity(new Intent(ClubsListActivity.this, ClubActivity.class).putExtra("id", itemClub.getId()));
                }
            }));
        });

        findViewById(R.id.closed).setOnClickListener((View v) -> {
            onBackPressed();
        });

        mClubOwner.setOnClickListener(v -> {
            if (mViewModel.owner == true) {
                mViewModel.owner = false;
                mClubOwner.off();
                findViewById(R.id.title_club_owner).setVisibility(View.GONE);
                findViewById(R.id.list_null).setVisibility(View.GONE);
                findViewById(R.id.list_club_owner).setVisibility(View.GONE);
            } else {
                mViewModel.owner = true;
                mClubOwner.on();
                findViewById(R.id.title_club_owner).setVisibility(View.VISIBLE);
                findViewById(R.id.list_null).setVisibility(View.VISIBLE);
                findViewById(R.id.list_club_owner).setVisibility(View.VISIBLE);
            }
            openSearch();
        });

        mClubMember.setOnClickListener(v -> {
            if (mViewModel.member == true) {
                mViewModel.member = false;
                mClubMember.off();
                mViewModel.getClubsParticipant();
                findViewById(R.id.title_club_member).setVisibility(View.GONE);
                findViewById(R.id.list_null_two).setVisibility(View.GONE);
                findViewById(R.id.list_club_member).setVisibility(View.GONE);
            } else {
                mViewModel.member = true;
                mClubMember.on();
                findViewById(R.id.title_club_member).setVisibility(View.VISIBLE);
                findViewById(R.id.list_null_two).setVisibility(View.VISIBLE);
                findViewById(R.id.list_club_member).setVisibility(View.VISIBLE);
            }
            openSearch();
        });

        findViewById(R.id.search_action).setOnClickListener(v -> {
            mViewModel.filterData(searchText.getText().toString());
        });

        openSearch();
    }

    void openSearch() {
        if (adaper != null) {
            adaper.clear();
            adaper.notifyDataSetChanged();
        }
        if (mViewModel.owner == false && mViewModel.member == false) {
            findViewById(R.id.search_panel).setVisibility(View.VISIBLE);
            findViewById(R.id.list_club_member).setVisibility(View.VISIBLE);
            mViewModel.getAllClubs();
            mViewModel.getClubsParticipant().observe(this, clubs -> {
                if (clubs == null) {
                    return;
                }
                if (clubs.size() > 0) {
                    findViewById(R.id.list_null_two).setVisibility(View.GONE);
                }
                adaper = new ClubsAdaper(this, clubs).setOnClubsListener(new ClubsAdaper.ClubsListener() {
                    @Override
                    public void onInfo(ItemClub itemClub) {
                        startActivity(new Intent(ClubsListActivity.this, ClubActivity.class).putExtra("id", itemClub.getId()));
                    }
                });
                listClubMember.setAdapter(adaper);
            });
        } else {
            findViewById(R.id.search_panel).setVisibility(View.GONE);
            mViewModel.getClubsCreatorList();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

}