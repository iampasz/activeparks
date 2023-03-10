package com.app.activeparks.ui.profile.uservideo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentNotificationBinding;

public class UserAddVideoActivity extends AppCompatActivity {

    private FragmentNotificationBinding binding;
    private UserVideoViewModel mViewModel;

    private TextView url, name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        mViewModel =
                new ViewModelProvider(this, new UserVideoFactory(this)).get(UserVideoViewModel.class);

        url = findViewById(R.id.url_video);
        name = findViewById(R.id.name_video);
        description = findViewById(R.id.description_video);

        String id = getIntent().getStringExtra("id");

        if (id != null){
            mViewModel.getUserVideo(id);
        }else {
            mViewModel.createUserVideo();
        }

        findViewById(R.id.moderation_action).setOnClickListener(view -> {
            mViewModel.sendUserVideo();
        });

        findViewById(R.id.closed).setOnClickListener(view -> {
            mViewModel.mVideoItem.setUrl(url.getText().toString());
            mViewModel.mVideoItem.setTitle(name.getText().toString());
            mViewModel.mVideoItem.setDescription(description.getText().toString());
            mViewModel.updateUserVideo();
            finish();
        });

        mViewModel.getUserVideoItem().observe(this, item -> {
            url.setText(item.getUrl());
            name.setText(item.getTitle());
            description.setText(item.getDescription());
        });
    }

}