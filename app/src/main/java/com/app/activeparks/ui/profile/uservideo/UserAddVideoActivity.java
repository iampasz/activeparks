package com.app.activeparks.ui.profile.uservideo;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;


import com.app.activeparks.util.CustomSpinnerAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.IOUtils;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentNotificationBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UserAddVideoActivity extends AppCompatActivity {

    private UserVideoViewModel mViewModel;

    private ImageView imageVideo;
    private TextView url, name, description;

    private Spinner category, level;

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

        category = findViewById(R.id.category);
        level = findViewById(R.id.level);

        String id = getIntent().getStringExtra("id");

        if (id != null){
            mViewModel.getUserVideo(id);
        }else {
            mViewModel.createUserVideo();
        }

        CustomSpinnerAdapter categoryAdapter = new CustomSpinnerAdapter(this, mViewModel.categoryId);
        category.setAdapter(categoryAdapter);
        CustomSpinnerAdapter levelAdapter = new CustomSpinnerAdapter(this, mViewModel.exerciseDifficultyLevelId);
        level.setAdapter(levelAdapter);

        findViewById(R.id.moderation_action).setOnClickListener(view -> {
            mViewModel.sendUserVideo();
        });

        findViewById(R.id.closed).setOnClickListener(view -> {
            mViewModel.mVideoItem.setUrl(url.getText().toString());
            mViewModel.mVideoItem.setTitle(name.getText().toString());
            mViewModel.mVideoItem.setDescription(description.getText().toString());
            mViewModel.setCategoryId(category.getSelectedItemPosition());
            mViewModel.setExerciseDifficultyLevelId(level.getSelectedItemPosition());
            mViewModel.updateUserVideo();
        });


        findViewById(R.id.remove_action).setOnClickListener(view -> {
            mViewModel.remove();
        });

        mViewModel.getUserVideoItem().observe(this, item -> {
            Glide.with(this).load(item.getMainPhoto()).error(R.drawable.ic_prew).error(R.drawable.ic_prew).into(imageVideo);
            url.setText(item.getUrl());
            name.setText(item.getTitle());
            description.setText(item.getDescription());
            category.setSelection(new ArrayList<>(mViewModel.categoryId.keySet()).indexOf(item.getCategoryId()));
            level.setSelection(new ArrayList<>(mViewModel.exerciseDifficultyLevelId.keySet()).indexOf(item.getExerciseDifficultyLevelId()));
        });

        findViewById(R.id.photo_action).setOnClickListener((View v) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 3);
        });

        mViewModel.getClosed().observe(this, type -> {
            if (type == true){
                finish();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();

                try {

                    File file = saveImageToFile(uri);

                    mViewModel.updateFile(file);

                    Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    imageVideo.setImageBitmap(bm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File saveImageToFile(Uri imageUri) {
        ContentResolver resolver = this.getContentResolver();
        File file = null;

        try {
            InputStream inputStream = resolver.openInputStream(imageUri);

            file = new File(this.getFilesDir(), "image.jpg");

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
        }

        return file;
    }

}