package com.app.activeparks.ui.profile.uservideo;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.data.model.dictionaries.BaseDictionaries;
import com.app.activeparks.data.model.dictionaries.ExerciseCategory;
import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.uservideo.UserVideo;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.storage.Preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserVideoViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<UserVideo> userVideoList = new MutableLiveData<>();
    private MutableLiveData<UserVideoItem> userVideoItem = new MutableLiveData<>();
    private MutableLiveData<Boolean> closed = new MutableLiveData<>();

    public Map<String, String> categoryId = new HashMap<>();
    public Map<String, String> exerciseDifficultyLevelId = new HashMap<>();
    public UserVideoItem mVideoItem;

    UserVideoViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
        getUserVideoList();
        setArray();
    }


    public LiveData<UserVideo> getUserVideo() {
        return userVideoList;
    }
    public LiveData<Boolean> getClosed() {
        return closed;
    }

    public LiveData<UserVideoItem> getUserVideoItem() {
        return userVideoItem;
    }

    public void getUserVideoList() {
        repository.getUserVideos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> userVideoList.setValue(result),
                        error -> {
                        });
    }

    public void createUserVideo() {
        repository.createUserVideo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            userVideoItem.setValue(result);
                            mVideoItem = result;
                        },
                        error -> {
                        });
    }

    public void getUserVideo(String id) {
        repository.getUserVideo(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            userVideoItem.setValue(result);
                            mVideoItem = result;
                        },
                        error -> {
                        });
    }

    public void updateUserVideo() {
        repository.updateUserVideo(mVideoItem.getId(), mVideoItem).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            userVideoItem.setValue(result);
                            closed.setValue(true);
                        },
                        error -> {
                            closed.setValue(true);
                        });
    }

    public void updateFile(File file) {
        repository.updateFile(file, "other_photo").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getUrl() != null){
                                mVideoItem.setMainPhoto(result.getUrl());
                            }
                        },
                        error -> {

                        }
                );
    }

    public void remove() {
        repository.deleteUserVideo(mVideoItem.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> closed.setValue(true),
                        error -> {
                            closed.setValue(true);
                        }
                );
    }

    public void sendUserVideo() {
        repository.sendUserVideo(mVideoItem.getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> closed.setValue(true),
                        error -> {
                            closed.setValue(true);
                        }
                );
    }

    public void setArray() {
        if (sharedPreferences.getDictionarie() != null) {
            for (ExerciseCategory district : sharedPreferences.getDictionarie().getExerciseCategories()) {
                this.categoryId.put(district.getId(), district.getTitle());
            }
            for (BaseDictionaries exerciseDifficultyLevelId : sharedPreferences.getDictionarie().getExerciseDifficultyLevels()) {
                this.exerciseDifficultyLevelId.put(exerciseDifficultyLevelId.getId(), exerciseDifficultyLevelId.getTitle());
            }
        }
    }

    public void setCategoryId(int selection) {
        String[] keys = categoryId.keySet().toArray(new String[categoryId.size()]);
        if (selection >= 0 && selection < keys.length) {
            mVideoItem.setCategoryId(keys[selection]);
        }
    }

    public void  setExerciseDifficultyLevelId(int selection){
        String[] keys = exerciseDifficultyLevelId.keySet().toArray(new String[exerciseDifficultyLevelId.size()]);
        if (selection >= 0 && selection < keys.length) {
            mVideoItem.setExerciseDifficultyLevelId(keys[selection]);
        }
    }

    public void setModelVideo(String url, String title, String description, int category, int level) {
        mVideoItem.setUrl(url != null ? url : mVideoItem.getUrl());
        mVideoItem.setTitle(title != null ? title : mVideoItem.getTitle());
        mVideoItem.setDescription(description != null ? description : mVideoItem.getDescription());
        setCategoryId(category);
        setExerciseDifficultyLevelId(level);
    }

}