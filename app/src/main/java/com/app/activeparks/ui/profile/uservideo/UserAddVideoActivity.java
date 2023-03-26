package com.app.activeparks.ui.profile.uservideo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;


import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentNotificationBinding;

import java.io.File;
import java.io.IOException;

public class UserAddVideoActivity extends AppCompatActivity {

    private UserVideoViewModel mViewModel;

    private ImageView imageVideo;
    private TextView url, name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        mViewModel =
                new ViewModelProvider(this, new UserVideoFactory(this)).get(UserVideoViewModel.class);

        url = findViewById(R.id.url_video);
        name = findViewById(R.id.name_video);
        imageVideo = findViewById(R.id.image_video);
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

        findViewById(R.id.photo_action).setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                String[] proj = {MediaStore.Images.Media.DATA};
                CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String result = cursor.getString(column_index);
                cursor.close();

                File file = new File(result);

                mViewModel.updateFile(file);

                try {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageVideo.setImageBitmap(bm);
                }  catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}