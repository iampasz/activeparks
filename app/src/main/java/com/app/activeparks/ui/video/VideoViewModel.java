package com.app.activeparks.ui.video;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.video.Video;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class VideoViewModel extends ViewModel {

    public boolean mPause = true, mFullScreen = false;

    private MutableLiveData<Video> mVideo;
    private MutableLiveData<Video> mUsersVideo;
    private MutableLiveData<ArrayList<String>> mLiveDataVideoList;
    private Repository repository;

    private int id;
    private String categoryId, exerciseDifficultyLevelId;

    public VideoViewModel() {
        mVideo = new MutableLiveData<>();
        mLiveDataVideoList = new MutableLiveData<>();
        repository = new Repository();
    }

    public LiveData<Video> getVideoModel() {
        return mVideo;
    }

    public LiveData<Video> getUsersVideoModel() {
        return mVideo;
    }


    public void getVideo(Intent bundle) {
        categoryId = bundle.getStringExtra("categoryId");
        id = bundle.getIntExtra("id", 0);
        exerciseDifficultyLevelId = bundle.getStringExtra("exerciseDifficultyLevelId");

        if (id == 0) {
            repository.getVideo("66e2ec16-03a1-4367-834a-d6b87ee709bd", categoryId, exerciseDifficultyLevelId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> mVideo.setValue(result),
                            error -> mVideo.setValue(null));
        } else if (id == 1) {
            repository.getVideoUsers("66e2ec16-03a1-4367-834a-d6b87ee709bd", categoryId, exerciseDifficultyLevelId).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> mUsersVideo.setValue(result),
                            error -> mUsersVideo.setValue(null));
        } else {
            mVideo.setValue(null);
        }
    }

    public void getVideoRandom(String id, String categoryId, String exerciseDifficultyLevelId) {
        repository.getVideoRandom(id, "66e2ec16-03a1-4367-834a-d6b87ee709bd", categoryId, exerciseDifficultyLevelId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mVideo.setValue(result),
                        error -> mVideo.setValue(null));
    }


}