package com.app.activeparks.ui.profile.uservideo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.activeparks.repository.Repository;
import com.app.activeparks.data.model.uservideo.UserVideo;
import com.app.activeparks.data.model.uservideo.UserVideoItem;
import com.app.activeparks.data.storage.Preferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserVideoViewModel extends ViewModel {

    private final Preferences sharedPreferences;
    private Repository repository;
    private MutableLiveData<UserVideo> userVideoList = new MutableLiveData<>();
    private MutableLiveData<UserVideoItem> userVideoItem = new MutableLiveData<>();
    public UserVideoItem mVideoItem;

    UserVideoViewModel(Preferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.repository = new Repository(sharedPreferences);
        getUserVideoList();
    }


    public LiveData<UserVideo> getUserVideo() {
        return userVideoList;
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
                        },
                        error -> {
                        });
    }

    public void sendUserVideo() {
        repository.sendUserVideo(mVideoItem.getId());
    }
}