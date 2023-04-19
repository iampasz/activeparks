package com.app.activeparks.ui.video;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.video.Files;
import com.app.activeparks.ui.video.adapter.VideoAdaper;
import com.technodreams.activeparks.R;

public class VideoActivity extends AppCompatActivity {

    private VideoViewModel mViewModel;
    private VideoView mVideoView;

    private WebView player;
    private FrameLayout frameLayout;
    private Button mBack, mMap;
    private RecyclerView listVideo;
    private ProgressBar mProgressBar;
    private LinearLayout mTopBarLinearLayout, mLinearLayout, mThemeLayout;
    private TextView mDescription, mTitle;
    private ImageView mButtonPlay, mButtonFullScrean, closed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        
        mVideoView = findViewById(R.id.videoview);

        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);

        mBack = findViewById(R.id.back_action);
        mMap = findViewById(R.id.map_action);

        listVideo = findViewById(R.id.list_video);
        mProgressBar = findViewById(R.id.progressBar);

        mTopBarLinearLayout = findViewById(R.id.top_bar_view);
        mLinearLayout = findViewById(R.id.linear_layout);
        mThemeLayout = findViewById(R.id.theme_layout);

        closed = findViewById(R.id.closed);

        frameLayout = findViewById(R.id.player_view);
        player = findViewById(R.id.youtube_player_view);

        mButtonPlay = findViewById(R.id.play);
        mButtonFullScrean = findViewById(R.id.full_screan);
        mButtonFullScrean = findViewById(R.id.full_screan);

        MediaController mc = new MediaController(this);
        mc.setAnchorView(mVideoView);
        mc.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(mc);
        mVideoView.requestFocus();

        mViewModel.getVideo(getIntent());


        mButtonPlay.setOnClickListener((View v) -> {
            mViewModel.mPause = !mViewModel.mPause;
            mButtonPlay.setImageDrawable(ContextCompat.getDrawable(this, mViewModel.mPause ? R.drawable.ic_pause : R.drawable.ic_play));
                if (mViewModel.mPause) {
                    mVideoView.start();
                } else {
                    mVideoView.pause();
                }
        });

        mButtonFullScrean.setOnClickListener((View v) -> {
            mViewModel.mFullScreen = !mViewModel.mFullScreen;
            mButtonFullScrean.setImageDrawable(ContextCompat.getDrawable(this, mViewModel.mFullScreen ? R.drawable.ic_baseline_fullscreen : R.drawable.ic_baseline_fullscreen_exit));
                if (mViewModel.mFullScreen) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mThemeLayout.setBackground(ContextCompat.getDrawable(this, R.color.black));
                } else {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mThemeLayout.setBackground(ContextCompat.getDrawable(this, R.color.background));
                }
        });

        mBack.setOnClickListener((View v)->{
            finish();
        });

        closed.setOnClickListener((View v)->{
            finish();
        });

        mMap.setOnClickListener((View v)->{
            Intent intent = new Intent();
            intent.putExtra("ID_MAP", 2);
            setResult(RESULT_OK, intent);
            finish();
        });

        mViewModel.getVideoModel().observe(this, videoModel-> {
            if (videoModel == null){
                Toast.makeText(this, "Жодного відео в даній категорії", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            try{
            mTitle.setText(videoModel.getItems().get(0).getTitle());
            mDescription.setText(videoModel.getItems().get(0).getDescription());

            Files files = videoModel.getItems().get(0).getFiles();
            if (files.getHigh() != null){
                Uri video = Uri.parse(files.getLow());
                mVideoView.setVideoURI(video);
                mVideoView.start();
            }else{
                Uri video = Uri.parse(files.getLow());
                mVideoView.setVideoURI(video);
                mVideoView.start();
            }


            VideoAdaper adapter = new VideoAdaper(this, videoModel.getItems())
                    .setOnClickListener(new VideoAdaper.VideoAdaperListener() {
                @Override
                public void onClick(String id, String categoryId, String exerciseDifficultyLevelId) {
                    mViewModel.getVideoRandom(id, categoryId, exerciseDifficultyLevelId);
                }
            });

            listVideo.setAdapter(adapter);

            mProgressBar.setVisibility(View.GONE);
            }catch (Exception e){}
        });

        mViewModel.getUsersVideoModel().observe(this, videoModel-> {
            if (videoModel == null){
                Toast.makeText(this, "Жодного відео в даній категорії", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            try{
                mTitle.setText(videoModel.getItems().get(0).getTitle());
                mDescription.setText(videoModel.getItems().get(0).getDescription());
                player.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                player.getSettings().setJavaScriptEnabled(true);

                player.loadUrl("https://youtube.com/embed/" + videoModel.getItems().get(0).getUrl());

                VideoAdaper adapter = new VideoAdaper(this, videoModel.getItems())
                        .setOnClickListener(new VideoAdaper.VideoAdaperListener() {
                            @Override
                            public void onClick(String id, String categoryId, String exerciseDifficultyLevelId) {
                                mViewModel.getVideoRandom(id, categoryId, exerciseDifficultyLevelId);
                            }
                        });

                listVideo.setAdapter(adapter);

                mProgressBar.setVisibility(View.GONE);
            }catch (Exception e){}
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int newOrientation = newConfig.orientation;

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            mTopBarLinearLayout.setVisibility(View.GONE);
            mLinearLayout.setVisibility(View.GONE);
            mTitle.setVisibility(View.GONE);
            mThemeLayout.setBackground(ContextCompat.getDrawable(this, R.color.black));
            mViewModel.mFullScreen = false;
        }else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            mTopBarLinearLayout.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.VISIBLE);
            mThemeLayout.setBackground(ContextCompat.getDrawable(this, R.color.background));
            mViewModel.mFullScreen = true;
        }

        mButtonFullScrean.setImageDrawable(ContextCompat.getDrawable(this, mViewModel.mFullScreen ? R.drawable.ic_baseline_fullscreen : R.drawable.ic_baseline_fullscreen_exit));
    }

}